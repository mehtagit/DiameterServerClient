package com.nmss;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nmss.client.Client;
import com.nmss.pojo.DiameterData;
import com.nmss.pojo.DiameterServer;
import com.nmss.pojo.Stats;
import com.nmss.util.Config;
import com.nmss.util.StatsMonitor;

public class DiameterClient {

	private static Logger logger = LogManager.getLogger(DiameterClient.class);

	private String filename;
	private List<DiameterServer> diameterServerList;
	private BlockingQueue<DiameterData> digestRequestQueue;
	private BlockingQueue<DiameterData> gbuRequestQueue;
	private BlockingQueue<DiameterData> responseQueue;

	private BlockingQueue<DiameterData> rootQueue;
	private BlockingQueue<Stats> statsQueue;

	private static Map<String, DiameterData> sessionMap;

	public DiameterClient(String filename) throws Exception {
		diameterServerList = new Config(filename).getConnectionList();
	}

	private DiameterClient() {

	}

	public void init() throws Exception {
		try {
			if (diameterServerList == null || diameterServerList.size() < 1)
				throw new Exception("Please provide property file");
			else {
				digestRequestQueue = new LinkedBlockingDeque<DiameterData>();
				gbuRequestQueue = new LinkedBlockingDeque<DiameterData>();
				rootQueue = new LinkedBlockingDeque<DiameterData>();
				responseQueue = new LinkedBlockingQueue<DiameterData>();
				statsQueue = new LinkedBlockingQueue<Stats>();
				sessionMap = new ConcurrentHashMap<>();

				StatsMonitor statsMonitor = new StatsMonitor(statsQueue);
				new Thread(statsMonitor::monitor, "Thread-DC-Stats").start();

				for (DiameterServer diameterServer : diameterServerList) {
					if (diameterServer.isDigestTrueGbuFalse()) {
						Client client = new Client(diameterServer, digestRequestQueue, responseQueue, statsQueue);
						new Thread(client::monitor, "Thread-DC-digest-" + diameterServer.getClientId()).start();
					} else {
						Client client = new Client(diameterServer, gbuRequestQueue, responseQueue, statsQueue);
						new Thread(client::monitor, "Thread-DC-gbu-" + diameterServer.getClientId()).start();
					}

				}

				new Thread(this::deciderGbuOrDigest, "Thread-DC-Decider").start();

			}
		} catch (Exception e) {
			throw e;
		}

	}

	public void put(DiameterData transactionData) throws Exception {
		rootQueue.put(transactionData);
	}

	public BlockingQueue<DiameterData> getResponseQueue() throws Exception {
		return responseQueue;
	}

	private void deciderGbuOrDigest() {
		logger.info(Thread.currentThread().getName() + " Started");
		while (true) {
			try {
				DiameterData diameterData = rootQueue.take();
				if (diameterData.getIsDigest()) {
					digestRequestQueue.put(diameterData);
				} else {
					gbuRequestQueue.put(diameterData);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static void addSession(DiameterData requestData) {
		if (requestData != null) {
			requestData.setDcNetworkTime(System.currentTimeMillis());
			sessionMap.put(requestData.getTid(), requestData);
			logger.debug("Added request to session " + requestData);
		}
	}

	public static void removeSession(DiameterData responseData) {

		if (responseData != null) {
			DiameterData requestData = sessionMap.remove(responseData.getTid());
			if (requestData == null) {
				responseData.setDcNetworkTime(-1);
				logger.debug("Not Found in session map, Not able to calculate time" + responseData);
				return;
			}
			responseData.setRequestType(requestData.getRequestType());
			responseData.setDcNetworkTime(System.currentTimeMillis() - requestData.getDcNetworkTime());
		}
	}
}
