/**
 * 
 */
package com.nmss.messages;

import java.net.InetAddress;
import java.util.Date;
import java.util.Random;

import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Address;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.AVP_OctetString;
import dk.i1.diameter.AVP_Time;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.ProtocolConstants;
import dk.i1.diameter.Utils;

/**
 * @author Varun
 *
 *         Multimedia Authentication Request
 */
public class MARMessage extends Message {

	public MARMessage(String ORIGIN_HOST, String ORIGIN_REALM, String DESTINATION_HOST, String DESTINATION_REALM,
			int vendor_id, int AUTH_APP, String IMPI, String tid, boolean isProxy, String authenticationScheme,
			String authorization) {
		this(ORIGIN_HOST, ORIGIN_REALM, DESTINATION_HOST, DESTINATION_REALM, vendor_id, AUTH_APP, IMPI, tid, isProxy);
		if (authenticationScheme != null && authorization != null) {
			AVP group[] = new AVP[2];
			group[0] = new AVP_OctetString(ProtocolConstants.DI_SIP_AUTHENTICATION_SCHEME, authenticationScheme.getBytes());
			group[1] = new AVP_OctetString(610, authorization.getBytes());
			this.add(new AVP_Grouped(612, group));
		}
	}

	private MARMessage(String ORIGIN_HOST, String ORIGIN_REALM, String DESTINATION_HOST, String DESTINATION_REALM,
			int vendor_id, int AUTH_APP, String IMPI, String tid, boolean isProxy) {
		super.hdr.setRequest(true);
		super.hdr.command_code = ProtocolConstants.DIAMETER_MULTIMEDIA_AUTHENTICATION_REQUEST;
		super.hdr.application_id = 0;
		super.hdr.hop_by_hop_identifier = (new Random()).nextInt();

		if (isProxy)
			super.hdr.setProxiable(isProxy);

		int i = (int) (System.currentTimeMillis() / 1000L);
		super.hdr.end_to_end_identifier = i << 20 | (new Random()).nextInt() & 0xfffff;

		add(new AVP_UTF8String(ProtocolConstants.DI_ORIGIN_HOST, ORIGIN_HOST));
		add(new AVP_UTF8String(ProtocolConstants.DI_ORIGIN_REALM, ORIGIN_REALM));

		InetAddress inetaddress = null;
		try {
			inetaddress = InetAddress.getByName(DESTINATION_HOST);
		} catch (Exception e) {
			System.out.println(e);
		}
		add(new AVP_Unsigned32(ProtocolConstants.DI_AUTH_SESSION_STATE, 1));
		add(new AVP_Address(ProtocolConstants.DI_HOST_IP_ADDRESS, inetaddress));
		add(new AVP_UTF8String(ProtocolConstants.DI_DESTINATION_REALM, DESTINATION_REALM));
		add(new AVP_Unsigned32(ProtocolConstants.DI_AUTH_APPLICATION_ID, AUTH_APP));
		AVP authdata[] = new AVP[2];
		authdata[0] = new AVP_Unsigned32(ProtocolConstants.DI_AUTH_APPLICATION_ID, AUTH_APP);
		authdata[1] = new AVP_Unsigned32(ProtocolConstants.DI_VENDOR_ID, vendor_id);
		add(new AVP_Grouped(ProtocolConstants.DI_VENDOR_SPECIFIC_APPLICATION_ID, authdata));
		add(new AVP_UTF8String(1, IMPI));
		add(new AVP_UTF8String(ProtocolConstants.DI_SESSION_ID, tid));
		add(new AVP_Time(409, new Date()));

		Utils.setMandatory_RFC3588(this);
	}

}
