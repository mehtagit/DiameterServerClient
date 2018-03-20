/**
 * 
 */
package com.nmss.messages;

import java.net.InetAddress;
import java.util.Random;

import dk.i1.diameter.AVP_Address;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.ProtocolConstants;
import dk.i1.diameter.Utils;
import dk.i1.diameter.node.Capability;

/**
 * @author Varun
 *
 *         Capability Exchange Request
 */
public class CERMessage extends Message {

	public CERMessage() {

	}

	public CERMessage(String ORIGIN_HOST, String ORIGIN_REALM, String DESTINATION_HOST, String DESTINATION_REALM,
			int vendor_id, String PRODUCT_NAME, int AUTH_APP) {

		super.hdr.setRequest(true);
		super.hdr.command_code = ProtocolConstants.DIAMETER_COMMAND_CAPABILITIES_EXCHANGE;
		super.hdr.application_id = ProtocolConstants.DIAMETER_APPLICATION_COMMON;
		super.hdr.hop_by_hop_identifier = (new Random()).nextInt();

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
		add(new AVP_Address(ProtocolConstants.DI_HOST_IP_ADDRESS, inetaddress));
		add(new AVP_Unsigned32(ProtocolConstants.DI_VENDOR_ID, vendor_id));
		add(new AVP_UTF8String(ProtocolConstants.DI_PRODUCT_NAME, PRODUCT_NAME));
		add(new AVP_Unsigned32(ProtocolConstants.DI_AUTH_APPLICATION_ID, AUTH_APP));
		Utils.setMandatory_RFC3588(this);
	}

}
