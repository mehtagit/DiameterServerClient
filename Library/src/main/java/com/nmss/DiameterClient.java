package com.nmss;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import com.nmss.client.Client;
import com.nmss.pojo.DiameterServer;
import com.nmss.pojo.NetworkData;
import com.nmss.pojo.TransactionData;
import com.nmss.util.Config;

public class DiameterClient {

	private String filename;
	private List<DiameterServer> diameterServerList;
	private BlockingQueue<TransactionData> requestQueue;
	private BlockingQueue<NetworkData> responseQueue;

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
				requestQueue = new LinkedBlockingDeque<TransactionData>();
				responseQueue = new LinkedBlockingQueue<NetworkData>();

				for (DiameterServer diameterServer : diameterServerList) {
					Client client = new Client(diameterServer, requestQueue, responseQueue);
					new Thread(client::start, "DiameterClient").start();
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public void put(TransactionData transactionData) throws Exception {
		requestQueue.put(transactionData);
	}

	public BlockingQueue<NetworkData> getResponseQueue() throws Exception {
		return responseQueue;
	}
}
