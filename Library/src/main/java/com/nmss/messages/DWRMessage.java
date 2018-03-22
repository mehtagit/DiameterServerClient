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
import dk.i1.diameter.AVP_Time;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.ProtocolConstants;
import dk.i1.diameter.Utils;

/**
 * @author Varun
 *
 *         Device WatchDog Request
 */
public class DWRMessage extends Message {

	public DWRMessage(String ORIGIN_HOST, String ORIGIN_REALM, String DESTINATION_HOST, String DESTINATION_REALM) {
		super.hdr.setRequest(true);
		super.hdr.command_code = ProtocolConstants.DIAMETER_COMMAND_DEVICE_WATCHDOG;
		super.hdr.application_id = 0;
		super.hdr.hop_by_hop_identifier = (new Random()).nextInt();

		int i = (int) (System.currentTimeMillis() / 1000L);
		super.hdr.end_to_end_identifier = i << 20 | (new Random()).nextInt() & 0xfffff;

		add(new AVP_UTF8String(ProtocolConstants.DI_ORIGIN_HOST, ORIGIN_HOST));
		add(new AVP_UTF8String(ProtocolConstants.DI_ORIGIN_REALM, ORIGIN_REALM));

		Utils.setMandatory_RFC3588(this);
		Utils.setMandatory_RFC4006(this);
	}

}
