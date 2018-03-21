package com.nmss.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.nmss.messages.CERMessage;
import com.nmss.messages.MARMessage;
import com.nmss.pojo.DiameterServer;
import com.nmss.pojo.NetworkData;
import com.nmss.pojo.TransactionData;

import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.AVP_Time;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.ProtocolConstants;

public class Client {
	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private String serverIP;
	private int serverPort;
	private String localIP;
	private int localPort;
	private BlockingQueue<TransactionData> requestQueue;
	private BlockingQueue<NetworkData> responseQueue;
	private DiameterServer diameterServer;

	public Client(String serverIP, int serverPort, String localIP, int localPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.localIP = localIP;
		this.localPort = localPort;
	}

	public Client(DiameterServer diameterServer, BlockingQueue<TransactionData> requestQueue,
			BlockingQueue<NetworkData> responseQueue) {
		this.requestQueue = requestQueue;
		this.responseQueue = responseQueue;
		this.diameterServer = diameterServer;
		this.serverIP = diameterServer.getDestinationIP();
		this.serverPort = diameterServer.getDestinationPort();
		this.localIP = diameterServer.getOriginIp();
		this.localPort = diameterServer.getOriginPort();
	}

	public void disConnect() {
		try {
			// in.close();
			// out.close();
			clientSocket.shutdownInput();
			clientSocket.shutdownOutput();
			clientSocket.close();

			System.out.println("disconnected from  server....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean connect() {
		try {
			// clientSocket = new Socket(serverIP, serverPort,
			// InetAddress.getByName(localIP), localPort);
			clientSocket = new Socket(serverIP, serverPort);
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			System.out.println("connection created with server....");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendRequest(Message message) {
		try {
			out.write(message.encode());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Message readFromServer() {
		byte length[] = new byte[3];
		byte version[] = new byte[1];
		byte data[] = new byte[20000];
		byte total_data[] = null;
		try {

			while (in.read(data, 0, 1) == -1) {
				continue;
			}
			in.read(data, 1, 3);
			version[0] = data[0];
			length[0] = data[1];
			length[1] = data[2];
			length[2] = data[3];
			int total_length_to_receive = convertToInt(convertToHex(length));
			total_data = new byte[total_length_to_receive];
			int received_from_server = readComplete((total_length_to_receive - 4), data, 4, 0);
			int ll = received_from_server + 4;
			if (total_length_to_receive == ll) {
				System.arraycopy(data, 0, total_data, 0, total_length_to_receive);
			} else {
				int bytes_left_to_read = total_length_to_receive - (received_from_server + 4);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Message message = new Message();
		message.decode(total_data);
		return message;
	}

	public int readComplete(int total_length_to_receive, byte data[], int startIndex, int count) throws Exception {
		int readed = in.read(data, startIndex, total_length_to_receive);
		count = count + readed;
		if (readed == total_length_to_receive) {
			// BillingClient.LW.writeIntoLog(2," REQUEST MDN ["+MSISDN+"] I have readed
			// complete in one frame count ["+count+"]");
		} else {
			int bytes_left_to_read = total_length_to_receive - readed;
			// BillingClient.LW.writeIntoLog(2," REQUEST MDN ["+MSISDN+"] I have readed only
			// ["+readed+"] GOING TO READ AGAIN.. LEFT ["+bytes_left_to_read+"]");
			return readComplete(bytes_left_to_read, data, (readed + 4), count);
			// readComplete(data,(readed+1)bytes_left_to_read);
		}
		return count;
	}

	public String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public int convertToInt(String data) {
		return Integer.parseInt(data, 16);
	}

	public void start() {
		System.out.println(Thread.currentThread().getName() + " is Started");
		try {
			while (!connect()) {
				TimeUnit.SECONDS.sleep(5);
			}

			boolean isCerSuccess = false;
			while (!isCerSuccess) {

				CERMessage cerMessage = new CERMessage(diameterServer.getOriginIp(), diameterServer.getOriginRelam(),
						diameterServer.getDestinationIP(), diameterServer.getDestinationRelam(),
						diameterServer.getVendorId(), diameterServer.getProduct(), diameterServer.getAuthApp());
				sendRequest(cerMessage);
				System.out.println("Send CER");
				Message cea = readFromServer();
				System.out.println("Receive CER");
				try {
					AVP_Unsigned32 resultAvp = new AVP_Unsigned32(cea.find(ProtocolConstants.DI_RESULT_CODE));
					isCerSuccess = resultAvp.queryValue() == ProtocolConstants.DIAMETER_RESULT_SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
				}
				TimeUnit.SECONDS.sleep(5);
			}

			new Thread(this::readRequestQueue, Thread.currentThread().getName() + "_RequestThread").start();
			new Thread(this::putResponseQueue, Thread.currentThread().getName() + "_ResponseThread").start();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " is ended");
	}

	public void readRequestQueue() {
		System.out.println(Thread.currentThread().getName() + " is Started");
		while (true) {
			try {
				TransactionData transactionData = requestQueue.take();
				MARMessage mar = new MARMessage(diameterServer.getOriginIp(), diameterServer.getOriginRelam(),
						diameterServer.getDestinationIP(), diameterServer.getDestinationRelam(),
						diameterServer.getVendorId(), diameterServer.getAuthApp(), transactionData.getIMPI(),
						transactionData.getTid());
				sendRequest(mar);
				System.out.println("Send MAR " + transactionData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void putResponseQueue() 
	{
		System.out.println(Thread.currentThread().getName() + " is Started");
		while (true) 
		{
			try {
				Message response = readFromServer();
				System.out.println("I have read something from server......");
				int resultCode = new AVP_Unsigned32(response.find(ProtocolConstants.DI_RESULT_CODE)).queryValue();
				switch(response.hdr.command_code)
				{
				case 303:
					NetworkData responseData  = new NetworkData();
					String impi = new AVP_UTF8String(response.find(1)).queryValue();
					String tid = new AVP_UTF8String(response.find(ProtocolConstants.DI_SESSION_ID)).queryValue();
					if (resultCode == 2001)
					{
					//	612 = group
						responseData.setResult(true);
						String iteamNumber				="not found";
						AVP_Grouped data = new AVP_Grouped(response.find(612));
						AVP allData[] = data.queryAVPs();
						//allData.
						responseData.setAuthenticationScheme(new AVP_UTF8String(response.find(608)).queryValue());
						responseData.setAuthenticate(new AVP_UTF8String(response.find(609)).queryValue());
						responseData.setAuthorization(new AVP_UTF8String(response.find(610)).queryValue());
						responseData.setConfidentialityKey(new AVP_UTF8String(response.find(625)).queryValue());
						responseData.setIntegrityKey(new AVP_UTF8String(response.find(626)).queryValue());
					}
					else
					{
						responseData.setResult(false);
					}
					System.out.println("Received MAR " + responseData);
					responseQueue.put(responseData);	
					break;
				case 257: //CEA
					System.out.println("Received CEA " + resultCode);
					break;
				case 280: //CEA
					System.out.println("Received DWA " + resultCode);
					break;
				default:
					System.out.println("Some unknown response got : "+response.hdr.command_code);
					break;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
