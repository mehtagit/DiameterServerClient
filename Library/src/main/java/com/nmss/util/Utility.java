package com.nmss.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Utility {

	protected final static Logger logger = LogManager.getLogger(Utility.class);

	private static DatagramSocket clientSocket;

	static {
		try {
			clientSocket = new DatagramSocket();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void UDP_SEND(String IP, int PORT, String BUFF, boolean islength) {
		try {
			String strFinal = null;
			int len = BUFF.length();
			byte[] bt = null;
			if (islength) {
				if ((len - 5) >= 10000)
					strFinal = (len - 5) + "";
				else if ((len - 5) >= 1000)
					strFinal = "0" + (len - 5);
				else if ((len - 5) >= 100)
					strFinal = "00" + (len - 5);
				else if ((len - 5) >= 10)
					strFinal = "000" + (len - 5);
				else
					strFinal = "0000" + (len - 5);
				bt = BUFF.getBytes();
				byte[] bt1 = strFinal.getBytes();
				for (int i = 0; i < bt1.length; i++)
					bt[i] = bt1[i];
			} else {
				bt = BUFF.getBytes();
			}
			InetAddress IPAddress = InetAddress.getByName(IP);
			DatagramPacket sendPacket = new DatagramPacket(bt, bt.length);
			String byte_to_String = new String(bt);
			sendPacket.setData(bt);
			sendPacket.setAddress(IPAddress);
			sendPacket.setPort(PORT);
			clientSocket.send(sendPacket);
			logger.info("UDP_SEND:" + byte_to_String + ", IP:" + IP + ", PORT:" + PORT);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public String sendPost(String url, String data, String contextType) {
		return sendPost(url, data, contextType, 1000, 1000);
	}

	public String sendPost(String url, String data, String contextType, int readTimeOut, int connectTimeOut) {
		Exception E = null;
		DataOutputStream wr = null;
		BufferedReader in = null;
		int status = -10;
		String result = null;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("content-type", contextType);

			con.setDoOutput(true);
			con.setReadTimeout(readTimeOut);
			con.setConnectTimeout(connectTimeOut);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();

			status = con.getResponseCode();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			result = response.toString();
		} catch (SocketTimeoutException e) {
			// Read Time Out
			status = -20;
			E = e;
		} catch (ConnectException e) {
			// Connection Time Out
			status = -30;
			E = e;
		} catch (SocketException e) {
			// Connection Reset
			status = -40;
			E = e;
		} catch (Exception e) {
			e.printStackTrace();
			E = e;
		} finally {
			try {
				if (wr != null) {
					wr.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (E != null) {
			logger.info("sendPost - Status:Exception" + E.getMessage() + ", URL:" + url + ", XML:" + data);
			E.printStackTrace();
		} else
			logger.info("sendPost - Status:" + status + ", URL:" + url + ", DATA:" + data + ", Response: " + result);
		return result;
	}

	public String callUrl(String url) {
		return callUrl(url, 1000, 1000);
	}

	public String callUrl(String URL_Str, int readTimeOut, int connectTimeOut) {
		BufferedReader BR = null;
		Exception E = null;
		String result = null;
		int status = -200;
		try {
			URL url = new URL(URL_Str);
			HttpURLConnection theURLconn = (HttpURLConnection) url.openConnection();
			theURLconn.setConnectTimeout(readTimeOut);
			theURLconn.setReadTimeout(connectTimeOut);
			status = theURLconn.getResponseCode();
			BR = new BufferedReader(new InputStreamReader(theURLconn.getInputStream()));
			String show = "";
			String toprt = "";
			StringBuffer response = new StringBuffer();
			while ((show = BR.readLine()) != null) {
				response.append(show.trim());
			}
			result = response.toString();
		} catch (SocketTimeoutException e) {
			// Read Time Out
			status = -20;
			E = e;
		} catch (ConnectException e) {
			// Connection Time Out
			status = -30;
			E = e;
		} catch (SocketException e) {
			// Connection Reset
			status = -40;
			E = e;
		} catch (Exception e) {
			e.printStackTrace();
			E = e;
		} finally {
			try {
				BR.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (E != null) {
			logger.info("sendPost - Status:Exception" + E.getMessage() + ", URL:" + URL_Str);
			E.printStackTrace();
		} else
			logger.info("sendPost - Status:" + status + ", URL:" + URL_Str + ", Response: " + result);

		return result;
	}

	public String sendReceive(String tid, String sendData, String ip, int port) {
		DatagramSocket socket = null;
		String response = null;
		try {
			socket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(ip);
			sendData = sendData.replaceAll("<selfPort>", "" + socket.getLocalPort());
			DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), IPAddress, port);
			socket.send(sendPacket);
			logger.info("UDP_SEND:" + sendData + ", IP:" + ip + ", PORT:" + port);
			byte[] reciveData = new byte[1024];
			DatagramPacket recivePacket = new DatagramPacket(reciveData, reciveData.length);
			socket.setSoTimeout(10000);
			socket.receive(recivePacket);
			socket.close();
			response = new String(recivePacket.getData());

		} catch (SocketTimeoutException e) {
			response = "00000###" + tid + "#<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><RES><TID>" + tid
					+ "</TID><RESULT>FAIL</RESULT><REASON>RECEIVE TIME OUT (I was waiting for 10 seconds)</REASON></RES>#";
		} catch (Exception e) {
			response = "00000###" + tid + "#<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><RES><TID>" + tid
					+ "</TID><RESULT>FAIL</RESULT><REASON>" + e.getMessage() + "</REASON></RES>#";
			e.printStackTrace();
		} finally {
			if (socket != null)
				socket.close();
		}
		logger.info("Response Recieved from Ucip:" + response);
		return response;
	}

	public String sendReceive(String sendData, String ip, int port) {
		DatagramSocket socket = null;
		String result = null;
		try {
			socket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(ip);
			sendData = sendData.replaceAll("<resp_port>", "" + socket.getLocalPort());
			socket.setSoTimeout(10 * 1000);

			UDP_SEND(ip, port, sendData, true);

			byte[] reciveData = new byte[1024];
			DatagramPacket recivePacket = new DatagramPacket(reciveData, reciveData.length);
			socket.receive(recivePacket);
			socket.close();
			result = new String(recivePacket.getData());
			result = result.trim();

		} catch (Exception e) {
			if (socket != null)
				socket.close();
			logger.info("sendReceive - Status:Exception" + e.getMessage() + ", Packet:" + sendData);
			e.printStackTrace();
		}
		if (result != null)
			logger.info("sendReceive - Request:" + sendData + ", Response: " + result);
		return result;
	}

	public String getDateTime() {
		return dateformat.format(new Date()).replace(" ", "T");
	}

	public String getDateTime(int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, minutes);
		return dateformat.format(calendar.getTime());
	}

	public static void main(String[] args) {
		System.out.println(new Utility().getDateTime(3));
	}

	public String getTimeStampForIn(int hours) {
		LocalDateTime now = LocalDateTime.now();
		now = now.plusHours(hours);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
		String formatDateTime = (now.format(formatter).replaceAll(" ", "T")) + "+0530";
		return formatDateTime;
	}

	public String getDateByDays(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return dateformat.format(calendar.getTime());
	}
}
