package com.nmss.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nmss.client.Client;
import com.nmss.pojo.Stats;

public class StatsMonitor {

	private BlockingQueue<Stats> statsQueue;
	private Logger logger = LogManager.getLogger(Client.class);

	public StatsMonitor(BlockingQueue<Stats> statsQueue) {
		this.statsQueue = statsQueue;
	}

	public void monitor() {
		logger.info(Thread.currentThread().getName() + " Started ");
		while (true) {
			try {
				Stats stats = statsQueue.take();
				String json = Config.gson.toJson(stats);
				// cdrlogger.info(json);
				Utility.UDP_SEND(Config.statsCdrIp, Config.statsCdrPort, json, false);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
