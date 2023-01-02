package com.nmss;

import java.util.concurrent.TimeUnit;

import com.nmss.pojo.DiameterData;
import com.nmss.pojo.MAA;
import com.nmss.pojo.MAR;
import com.nmss.pojo.RequestType;

public class Main {

    public static void main(String[] args) {
        try {

//			DiameterClient diameterClient = new DiameterClient(args[0]);

            DiameterClient diameterClient = new DiameterClient("clients.properties");
            diameterClient.init();

            new Thread(() -> {
                while (true) {
                    try {
                        DiameterData transactionData =
                                diameterClient.getResponseQueue().take(); //
                        System.out.println("Recieved At Apache " + transactionData);
                    } catch
                    (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            for (int i = 1; i <= 2; i++) {
                DiameterData diameterData = new DiameterData();
                diameterData.setImpi("IMPI---" + i);
                diameterData.setTid(System.currentTimeMillis() + "");
                diameterData.setIsDigest(false);
                diameterData.setRequestType(RequestType.ECR);

                diameterClient.put(diameterData);

                TimeUnit.MILLISECONDS.sleep(500);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
