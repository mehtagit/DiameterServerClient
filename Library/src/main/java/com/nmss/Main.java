package com.nmss;

import java.util.concurrent.TimeUnit;

import com.nmss.pojo.DiameterData;
import com.nmss.pojo.MAA;
import com.nmss.pojo.MAR;
import com.nmss.pojo.RequestType;

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

			for (int i = 1; i <= 50; i++) {
				DiameterData diameterData = new DiameterData();
				diameterData.setImpi("IMPI---" + i);
				diameterData.setTid(System.currentTimeMillis() + "");
				diameterData.setIsDigest(false);
				diameterData.setRequestType(RequestType.MAR);

				diameterClient.put(diameterData);

				TimeUnit.MILLISECONDS.sleep(500);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
