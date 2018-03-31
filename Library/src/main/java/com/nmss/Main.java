package com.nmss;

import java.util.concurrent.TimeUnit;

import com.nmss.pojo.DiameterData;

public class Main {

	public static void main(String[] args) {
		try {
			DiameterClient diameterClient = new DiameterClient(args[0]);
			diameterClient.init();

			/*
			 * new Thread(() -> { while (true) { try { NetworkData transactionData =
			 * diameterClient.getResponseQueue().take(); //
			 * System.out.println("Recieved At Apache " + transactionData); } catch
			 * (Exception e) { e.printStackTrace(); } } }).start();
			 */

			for (int i = 1; i <= 10000; i++) {
				DiameterData transactionData = new DiameterData();
				transactionData.setImpi("IMPI---" + i);
				transactionData.setTid(System.currentTimeMillis() + "");
				transactionData.setDcNetworkTime(System.currentTimeMillis());
				transactionData.setIsDigest(false);
				diameterClient.put(transactionData);
				TimeUnit.MILLISECONDS.sleep(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
