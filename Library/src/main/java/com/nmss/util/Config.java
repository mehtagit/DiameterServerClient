package com.nmss.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.nmss.pojo.DiameterServer;

public class Config {

	private List<DiameterServer> connectionList = new ArrayList<>();

	public Config(String filename) throws Exception {
		//////////// Reading properties////////////////////////////////

		try (InputStream in = new FileInputStream(filename)) {
			Properties prop = new Properties();
			prop.load(in);
			int noOfConnection = Integer.parseInt(prop.getProperty("no-of-connection", "0"));
			if (noOfConnection < 1)
				throw new Exception("no-of-connection parameter in config cannot be null or empty");

			for (int i = 1; i <= noOfConnection; i++) {
				DiameterServer diameterServer = new DiameterServer();
				diameterServer.setDestinationIP(prop.getProperty("destination-ip-" + i));

				diameterServer.setDestinationRelam(prop.getProperty("destination-relam-" + i));
				diameterServer.setOriginIp(prop.getProperty("origin-ip-" + i));
				diameterServer.setOriginRelam(prop.getProperty("origin-relam-" + i));
				diameterServer.setApplicationId(prop.getProperty("application-id-" + i));
				diameterServer.setProduct(prop.getProperty("product-" + i));
				try {
					diameterServer.setDestinationPort(Integer.parseInt(prop.getProperty("destination-port-" + i)));
					diameterServer.setOriginPort(Integer.parseInt(prop.getProperty("origin-port-" + i)));
					diameterServer.setVendorId(Integer.parseInt(prop.getProperty("vendor-id-" + i)));
					diameterServer.setAuthApp(Integer.parseInt(prop.getProperty("auth-app-" + i)));
				} catch (Exception e) {
					throw new Exception(
							"destination-port , origin-port , vendor-id and auth-app in property file must be integer");
				}
				System.out.println(diameterServer);
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
