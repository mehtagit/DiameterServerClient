package com.nmss.server;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

import com.nmss.messages.CERMessage;

import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.AVP_OctetString;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.ProtocolConstants;

public class ServerRequestHandler {
	public Socket socket;
	private InputStream in;

	public void start() {
		while (true) {
			try {
				in = socket.getInputStream();
				DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
				byte[] request = readFromServer();

				Message cerMessage = new Message();
				cerMessage.decode(request);
				cerMessage.hdr.setRequest(false);
				AVP group[] = new AVP[5];
				group[0] = new AVP_OctetString(608, "32".getBytes());
				group[1] = new AVP_OctetString(609, "33".getBytes());
				group[2] = new AVP_OctetString(610, "34".getBytes());
				group[3] = new AVP_OctetString(625, "35".getBytes());
				group[4] = new AVP_OctetString(626, "36".getBytes());
				cerMessage.add(new AVP_Grouped(612, group));
				cerMessage.add(new AVP_Unsigned32(268, 2001));
				outToClient.write(cerMessage.encode());
				System.out.println("Send Response");
			} catch (Exception exception) {
				exception.printStackTrace();
			} finally {
				try {
					// socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public byte[] readFromServer() {
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
		return total_data;
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
}
