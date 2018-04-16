package com.nmss.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nmss.DiameterClient;
import com.nmss.messages.CERMessage;
import com.nmss.messages.DPRMessage;
import com.nmss.messages.DWRMessage;
import com.nmss.messages.MARMessage;
import com.nmss.messages.UDRMessage;
import com.nmss.messages.ZxSsoOrDigestMessage;
import com.nmss.pojo.DiameterData;
import com.nmss.pojo.DiameterServer;
import com.nmss.pojo.MAA;
import com.nmss.pojo.Stats;
import com.nmss.util.CdrWriter;
import com.nmss.util.Config;

import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.ProtocolConstants;

public class Client {
	private Logger logger = LogManager.getLogger(Client.class);

	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private String serverIP;
	private int serverPort;
	private String localIP;
	private int localPort;
	private BlockingQueue<DiameterData> requestQueue;
	private BlockingQueue<DiameterData> responseQueue;
	private BlockingQueue<Stats> statsQueue;
	private DiameterServer diameterServer;
	private long lastRequestSend;
	private AtomicInteger window;
	private AtomicBoolean isMonitorThreadRunning;
	private Stats stats;

	public Client(String serverIP, int serverPort, String localIP, int localPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.localIP = localIP;
		this.localPort = localPort;
		this.window = new AtomicInteger(0);
		this.isMonitorThreadRunning = new AtomicBoolean(false);
		logger.debug(this + " object created successfully ");
	}

	public Client(DiameterServer diameterServer, BlockingQueue<DiameterData> requestQueue,
			BlockingQueue<DiameterData> responseQueue, BlockingQueue<Stats> statsQueue) {
		this.requestQueue = requestQueue;
		this.responseQueue = responseQueue;
		this.statsQueue = statsQueue;
		this.diameterServer = diameterServer;
		this.serverIP = diameterServer.getDestinationIP();
		this.serverPort = diameterServer.getDestinationPort();
		this.localIP = diameterServer.getOriginIp();
		this.localPort = diameterServer.getOriginPort();
		this.window = new AtomicInteger(0);
		this.isMonitorThreadRunning = new AtomicBoolean(false);
		this.lastRequestSend = System.currentTimeMillis();
		this.stats = new Stats(diameterServer.getOriginRelam());
		new Thread(this::statsMonitor, Thread.currentThread().getName() + "-Stats").start();
		logger.debug(this + " object created successfully ");
	}

	private void disConnect() {
		try {

			try {
				in.close();
				in = null;
			} catch (Exception e) {
				in = null;
			}
			;
			try {
				out.close();
				out = null;
			} catch (Exception e) {
				out = null;
			}
			try {
				clientSocket.shutdownInput();
			} catch (Exception e) {
			}
			try {
				clientSocket.shutdownOutput();
			} catch (Exception e) {
			}
			try {
				clientSocket.close();
			} catch (Exception e) {
			}

			logger.info("Disconnected from server " + this);
		} catch (Exception e) {
			logger.error(this + e.getMessage(), e);
		}
	}

	private boolean connect() {
		boolean isConnected = false;
		try {
			// clientSocket = new Socket(serverIP, serverPort,
			// InetAddress.getByName(localIP), localPort);
			clientSocket = new Socket(serverIP, serverPort);
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			logger.info("Connection Created " + this);
			isConnected = true;
		} catch (Exception e) {
			logger.error(this + e.getMessage(), e);
			isConnected = false;
		}
		return isConnected;
	}

	private boolean sendMessage(Message message) {
		try {
			out.write(message.encode());
			out.flush();
			return true;
		} catch (Exception e) {
			logger.error(this + e.getMessage(), e);
			return false;
		}
	}

	private Message readMessage() {
		byte length[] = new byte[3];
		byte version[] = new byte[1];
		byte data[] = new byte[20000];
		byte total_data[] = null;
		Message message = null;
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
			logger.error(this + e.getMessage(), e);
		}

		if (total_data != null) {
			message = new Message();
			message.decode(total_data);
		}
		return message;
	}

	private int readComplete(int total_length_to_receive, byte data[], int startIndex, int count) throws Exception {
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

	private String convertToHex(byte[] data) {
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

	private int convertToInt(String data) {
		return Integer.parseInt(data, 16);
	}

	public void monitor() {
		logger.info(Thread.currentThread().getName() + " Started ");
		if (!isMonitorThreadRunning.get()) {
			isMonitorThreadRunning.set(true);
			logger.info(this + " trying to make connection");
			try {
				while (!connect()) {
					TimeUnit.SECONDS.sleep(5);
					logger.info(this + " trying to make connection");
				}

				boolean isCerSuccess = false;
				while (!isCerSuccess) {

					CERMessage cerMessage = new CERMessage(diameterServer.getOriginIp(),
							diameterServer.getOriginRelam(), diameterServer.getDestinationIP(),
							diameterServer.getDestinationRelam(), diameterServer.getVendorId(),
							diameterServer.getProduct(), diameterServer.getAuthApp());
					sendMessage(cerMessage);
					logger.info("CER : " + cerMessage);
					Message cea = readMessage();
					logger.info("CEA : " + cea);
					try {
						AVP_Unsigned32 resultAvp = new AVP_Unsigned32(cea.find(ProtocolConstants.DI_RESULT_CODE));
						isCerSuccess = resultAvp.queryValue() == ProtocolConstants.DIAMETER_RESULT_SUCCESS;
					} catch (Exception e) {
						e.printStackTrace();
					}
					TimeUnit.SECONDS.sleep(5);
				}

				/* Monitor Thread is connected successfully no connection monitoring is off */
				isMonitorThreadRunning.set(false);
				// Start DWR Thread
				Thread dwrThread = new Thread(this::dwrTask, Thread.currentThread().getName() + "-DWR");
				dwrThread.setDaemon(true);
				dwrThread.start();

				new Thread(this::writeInToServerStream, Thread.currentThread().getName() + "-HSSWrite").start();
				new Thread(this::readFromServerStream, Thread.currentThread().getName() + "-HSSRead").start();

			} catch (Exception e) {
				logger.error(this + e.getMessage(), e);
			}
		}
	}

	private void dwrTask() {
		logger.info(Thread.currentThread().getName() + " Started ");
		while (true) {
			/* if connection breaks */
			if (isMonitorThreadRunning.get()) {
				logger.info("DWR Thread Stopped " + this);
				break;
			}

			try {
				if (((System.currentTimeMillis() - lastRequestSend) / 1000) > 10) {
					DWRMessage dwrMessage = new DWRMessage(diameterServer.getOriginIp(),
							diameterServer.getOriginRelam(), diameterServer.getDestinationIP(),
							diameterServer.getDestinationRelam());
					logger.info("Send DWR : " + dwrMessage);
					if (!sendMessage(dwrMessage)) {
						throw new Exception(
								Thread.currentThread().getName() + " Client Disconnected Going to reconnect");
					}

				}
				TimeUnit.SECONDS.sleep(diameterServer.getDwrTimeInSeconds());
			} catch (Exception e) {
				logger.error(this + e.getMessage(), e);

				/* if Connection Breaks start monitoring and reconnect */
				disConnect();
				new Thread(this::monitor).start();
				break;
			}
		}
	}

	private void writeInToServerStream() {
		logger.info(Thread.currentThread().getName() + " Started ");
		while (true) {
			DiameterData requestData = null;
			try {
				if (isMonitorThreadRunning.get()) {
					logger.info(Thread.currentThread().getName() + " Stopped ");
					break;
				}
				requestData = requestQueue.take();

				/* stats no of request incremented */
				stats.addRequest();

				logger.info("Request Received at DC" + requestData + ", Queue:" + requestQueue.size());

				Message message = null;
				boolean isProxy = false;
				if (Boolean.TRUE.equals(requestData.getIsDigest()))
					isProxy = Config.isDigestSLF;
				else
					isProxy = Config.isGbuSLF;

				switch (requestData.getRequestType()) {
				case MAR:

					message = new MARMessage(diameterServer.getOriginIp(), diameterServer.getOriginRelam(),
							diameterServer.getDestinationIP(), diameterServer.getDestinationRelam(),
							diameterServer.getVendorId(), diameterServer.getAuthApp(), requestData.getImpi(),
							requestData.getTid(), isProxy, requestData.getMar().getAuthenticationScheme(),
							requestData.getMar().getAuthorization());
					break;
				case UDR:
					message = new UDRMessage(diameterServer.getOriginIp(), diameterServer.getOriginRelam(),
							diameterServer.getDestinationIP(), diameterServer.getDestinationRelam(),
							diameterServer.getVendorId(), diameterServer.getAuthApp(), requestData.getImpi(),
							requestData.getTid(), isProxy, requestData.getUdr().getIMPU(),
							requestData.getUdr().getMsisdn(), requestData.getUdr().getDataReference(), null, null, null,
							null, null, null, null, null, null, null, null, null, null);
					break;
				case ZX_SSO:
					message = new ZxSsoOrDigestMessage(diameterServer.getOriginIp(), diameterServer.getOriginRelam(),
							diameterServer.getDestinationIP(), diameterServer.getDestinationRelam(),
							diameterServer.getVendorId(), diameterServer.getAuthApp(), requestData.getImpi(),
							requestData.getTid(), isProxy, requestData.getZx_sso().getIMPU());
					break;
				case ZX_DIGEST:
					message = new ZxSsoOrDigestMessage(diameterServer.getOriginIp(), diameterServer.getOriginRelam(),
							diameterServer.getDestinationIP(), diameterServer.getDestinationRelam(),
							diameterServer.getVendorId(), diameterServer.getAuthApp(), requestData.getImpi(),
							requestData.getTid(), isProxy, requestData.getZx_digest().getIMPU(),
							requestData.getZx_digest().getDigestUsername(), requestData.getZx_digest().getDigestRealm(),
							requestData.getZx_digest().getDigestNonce(), requestData.getZx_digest().getDigestResponse(),
							requestData.getZx_digest().getDigestAlgorithm(),
							requestData.getZx_digest().getDigestCNonce(), requestData.getZx_digest().getDigestQOP(),
							requestData.getZx_digest().getDigestNonceCount(),
							requestData.getZx_digest().getEricssonDigestHA2());
					break;
				default:
					break;
				}

				DiameterClient.addSession(requestData);
				if (!sendMessage(message)) {
					throw new Exception(Thread.currentThread().getName() + " Client Disconnected Going to reconnect");
				}
				logger.info("MAR : " + message);

				/* for DWR */
				this.lastRequestSend = System.currentTimeMillis();
				window.incrementAndGet();
				logger.debug("window " + window.get());

			} catch (Exception e) {
				logger.error(this + e.getMessage(), e);

				disConnect();
				/* if Connection Breaks start monitoring and reconnect */
				new Thread(this::monitor).start();
				break;
			}
		}
	}

	private void readFromServerStream() {
		logger.info(Thread.currentThread().getName() + " Started ");
		DiameterData responseData = null;
		while (true) {
			try {
				if (isMonitorThreadRunning.get()) {
					logger.info("Receiver Thread From Server to Client Stopped : " + this);
					break;
				}
				Message response = readMessage();
				if (response != null) {
					// System.out.println("I have read something from server......");

					switch (response.hdr.command_code) {
					case ProtocolConstants.DIAMETER_MULTIMEDIA_AUTHENTICATION_REQUEST:

						int resultCode = new AVP_Unsigned32(response.find(ProtocolConstants.DI_RESULT_CODE))
								.queryValue();
						responseData = parseMAA(response, resultCode);
						logger.info("MAA : " + responseData + ", Queue:" + responseQueue.size());
						putResponseBackInQueue(responseData);

						break;
					case ProtocolConstants.DIAMETER_USER_DATA_REQUEST:

						responseData = parseUDA(response);
						logger.info("UDA : " + responseData + ", Queue:" + responseQueue.size());
						putResponseBackInQueue(responseData);

						break;
					case ProtocolConstants.DIAMETER_ZX_SSO_MULTIMEDIA_AUTHENTICATION_REQUEST:

						responseData = parseZx(response);
						logger.info("ZxSsoOrDigest : " + responseData + ", Queue:" + responseQueue.size());
						putResponseBackInQueue(responseData);

						break;
					case ProtocolConstants.DIAMETER_COMMAND_CAPABILITIES_EXCHANGE:
						logger.info("CEA " + response);

						break;
					case ProtocolConstants.DIAMETER_COMMAND_DISCONNECT_PEER: // DPA
						logger.info("DPA " + response);
						break;
					case ProtocolConstants.DIAMETER_COMMAND_DEVICE_WATCHDOG: // DWA
						logger.info("DWA " + response);
						resultCode = new AVP_Unsigned32(response.find(ProtocolConstants.DI_RESULT_CODE)).queryValue();
						if (resultCode == 2001) {

						} else {
							DPRMessage dprMessage = new DPRMessage(diameterServer.getOriginIp(),
									diameterServer.getOriginRelam(), 2);
							logger.info("Send DPR " + dprMessage);
							sendMessage(dprMessage);
						}

						break;
					default:
						logger.warn("Some unknown response got : " + response.hdr.command_code);
						break;
					}
				} else {
					logger.warn("Message Response is received null " + this);
				}
			} catch (Exception e) {
				logger.error(this + e.getMessage(), e);
			}

		}
	}

	private void putResponseBackInQueue(DiameterData responseData) {
		try {
			/* Stats add for response */
			stats.addResponse(responseData.getResult());
			DiameterClient.removeSession(responseData);
			CdrWriter.write(responseData);
			window.decrementAndGet();
			responseQueue.put(responseData);
		} catch (Exception e) {
			logger.error(this + e.getMessage(), e);
		}
	}

	private void statsMonitor() {
		logger.info(Thread.currentThread().getName() + " Started ");
		while (true) {
			try {
				TimeUnit.MINUTES.sleep(Config.statsTimeInMinute);
				Stats oldStats = stats;
				stats = new Stats(diameterServer.getOriginRelam());
				statsQueue.put(oldStats);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private DiameterData parseUDA(Message response) {
		return parse(response);
	}

	private DiameterData parseZx(Message response) {
		return parse(response);
	}

	private DiameterData parse(Message response) {
		DiameterData responseData = new DiameterData();
		Integer resultCode = null;
		try {
			resultCode = new AVP_Unsigned32(response.find(ProtocolConstants.DI_RESULT_CODE)).queryValue();
			responseData.setResult(resultCode);
		} catch (Exception e) {
			resultCode = null;
		}
		return responseData;
	}

	private DiameterData parseMAA(Message response, int resultCode) {
		DiameterData responseData = new DiameterData();
		responseData.setMaa(new MAA());
		try {

			responseData.setImpi(new AVP_UTF8String(response.find(1)).queryValue());
			responseData.setTid(new AVP_UTF8String(response.find(ProtocolConstants.DI_SESSION_ID)).queryValue());
			if (resultCode == 2001) {
				// 612 = group
				responseData.setResult(resultCode);

				AVP sipAuthDataAVP = response.find(612);
				if (sipAuthDataAVP != null) {
					AVP_Grouped data = new AVP_Grouped(sipAuthDataAVP);
					AVP allData[] = data.queryAVPs();
					String iteamNumber = "not found";
					for (int i = 0; i < allData.length; i++) {
						if (allData[i].code == 608) {
							responseData.getMaa().setAuthenticationScheme(new String(allData[i].queryPayload()));
						} else if (allData[i].code == 609) {
							responseData.getMaa().setAuthenticate(new String(allData[i].queryPayload()));
						} else if (allData[i].code == 610) {
							responseData.getMaa().setAuthorization(new String(allData[i].queryPayload()));
						} else if (allData[i].code == 625) {
							responseData.getMaa().setConfidentialityKey(new String(allData[i].queryPayload()));
						} else if (allData[i].code == 626) {
							responseData.getMaa().setIntegrityKey(new String(allData[i].queryPayload()));
						} else {
							logger.info("something else avp is received [" + allData[i].code + "]");
						}

					}
				}

				AVP sipDigestAuthenticateAVP = response.find(635);
				if (sipDigestAuthenticateAVP != null) {
					AVP_Grouped data = new AVP_Grouped(sipDigestAuthenticateAVP);
					AVP allData[] = data.queryAVPs();
					String iteamNumber = "not found";
					for (int i = 0; i < allData.length; i++) {
						if (allData[i].code == 104) {
							responseData.getMaa().setDigestRealm(new String(allData[i].queryPayload()));
						} else if (allData[i].code == 111) {
							responseData.getMaa().setDigestAlgorithm(new String(allData[i].queryPayload()));
						} else if (allData[i].code == 110) {
							responseData.getMaa().setDigestQOP(new String(allData[i].queryPayload()));
						} else if (allData[i].code == 121) {
							responseData.getMaa().setDigestHA1(new String(allData[i].queryPayload()));
						} else {
							logger.info("something else avp is received [" + allData[i].code + "]");
						}

					}
				}
			} else {
				responseData.setResult(resultCode);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return responseData;
	}

	@Override
	public String toString() {
		return "Client [serverIP=" + serverIP + ", serverPort=" + serverPort + ", localIP=" + localIP + ", localPort="
				+ localPort + ", diameterServer=" + diameterServer + "]";
	}

}
