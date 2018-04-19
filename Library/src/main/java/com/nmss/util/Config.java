package com.nmss.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.nmss.pojo.DiameterServer;

public class Config {

	private List<DiameterServer> connectionList = new ArrayList<>();
	Logger logger = LogManager.getLogger(Config.class);
	public static String cdrIp;
	public static Integer cdrPort;

	public static String statsCdrIp;
	public static Integer statsCdrPort;

	public static Gson gson = new Gson();
	public static long statsTimeInMinute;

	public static Boolean isGbuSLF;

	public static Boolean isDigestSLF;

	public Config(String filename) throws Exception {
		//////////// Reading properties////////////////////////////////

		try (InputStream in = new FileInputStream(filename)) {
			Properties prop = new Properties();
			prop.load(in);

			// ***Application parameters***//
			int noOfConnection = Integer.parseInt(prop.getProperty("no-of-connection", "0"));
			cdrPort = Integer.parseInt(prop.getProperty("cdr-port", "1"));
			cdrIp = prop.getProperty("cdr-ip", "127.0.0.1");

			statsCdrPort = Integer.parseInt(prop.getProperty("stats-cdr-port", "1"));
			statsCdrIp = prop.getProperty("stats-cdr-ip", "127.0.0.1");
			statsTimeInMinute = Long.parseLong(prop.getProperty("stats-time-in-minutes", "1"));

			isGbuSLF = Boolean.parseBoolean(prop.getProperty("is-gbu-slf", "false"));
			isDigestSLF = Boolean.parseBoolean(prop.getProperty("is-digest-slf", "false"));
			// ***Application parameters***//
			if (noOfConnection < 1)
				throw new Exception("no-of-connection parameter in config cannot be null or empty");

			for (int i = 1; i <= noOfConnection; i++) {
				DiameterServer diameterServer = new DiameterServer();
				diameterServer.setDestinationIP(prop.getProperty("destination-ip-" + i));
				diameterServer.setClientId(prop.getProperty("client-id-" + i));
				diameterServer.setDestinationRelam(prop.getProperty("destination-relam-" + i));
				diameterServer.setOriginIp(prop.getProperty("origin-ip-" + i));
				diameterServer.setOriginRelam(prop.getProperty("origin-relam-" + i));
				diameterServer.setApplicationId(prop.getProperty("application-id-" + i));
				diameterServer.setProduct(prop.getProperty("product-" + i));
				diameterServer.setDigestTrueGbuFalse(
						Boolean.parseBoolean(prop.getProperty("is-digest_true-gbu_false-" + i, "false")));
				try {
					diameterServer.setDestinationPort(Integer.parseInt(prop.getProperty("destination-port-" + i)));
					diameterServer.setOriginPort(Integer.parseInt(prop.getProperty("origin-port-" + i)));
					diameterServer.setVendorId(Integer.parseInt(prop.getProperty("vendor-id-" + i)));
					diameterServer.setAuthApp(Integer.parseInt(prop.getProperty("auth-app-" + i)));
					diameterServer.setDwrTimeInSeconds(Integer.parseInt(prop.getProperty("dwr-time-in-seconds-" + i)));
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception(
							"destination-port , origin-port , vendor-id and auth-app in property file must be integer");
				}
				logger.info("Read Config " + diameterServer);
				connectionList.add(diameterServer);
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<DiameterServer> getConnectionList() {
		return connectionList;
	}

}
