package com.nmss;

import java.util.concurrent.TimeUnit;

import com.nmss.pojo.NetworkData;
import com.nmss.pojo.TransactionData;

public class Main {

	public static void main(String[] args) {
		try {
			DiameterClient diameterClient = new DiameterClient(args[0]);
			diameterClient.init();

			new Thread(() -> {
				while (true) {
					try {
						NetworkData transactionData = diameterClient.getResponseQueue().take();
						//System.out.println("Recieved At Apache " + transactionData);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();

			for (int i = 1; i < 100; i++) {
				TransactionData transactionData = new TransactionData();
				transactionData.setIMPI("IMPI---"+i);
				transactionData.setTid(""+i);
				transactionData.setStartTime(System.currentTimeMillis());
				diameterClient.put(transactionData);
				TimeUnit.MILLISECONDS.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
