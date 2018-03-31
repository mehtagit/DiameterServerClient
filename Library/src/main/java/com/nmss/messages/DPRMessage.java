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
 *         Disconnect Peer Request
 */
public class DPRMessage extends Message {

	public DPRMessage(String ORIGIN_HOST, String ORIGIN_REALM, int DisconnectReason) {
		super.hdr.setRequest(true);
		super.hdr.command_code = ProtocolConstants.DIAMETER_COMMAND_DISCONNECT_PEER;
		super.hdr.application_id = ProtocolConstants.DIAMETER_APPLICATION_COMMON;
		super.hdr.hop_by_hop_identifier = (new Random()).nextInt();

		int i = (int) (System.currentTimeMillis() / 1000L);
		int state_id = i;
		super.hdr.end_to_end_identifier = i << 20 | (new Random()).nextInt() & 0xfffff;

		add(new AVP_UTF8String(ProtocolConstants.DI_ORIGIN_HOST, ORIGIN_HOST));
		add(new AVP_UTF8String(ProtocolConstants.DI_ORIGIN_REALM, ORIGIN_REALM));

		if (DisconnectReason == 0) {
			add(new AVP_Unsigned32(ProtocolConstants.DI_DISCONNECT_CAUSE,
					ProtocolConstants.DI_DISCONNECT_CAUSE_REBOOTING));
		} else if (DisconnectReason == 2) {
			add(new AVP_Unsigned32(ProtocolConstants.DI_DISCONNECT_CAUSE,
					ProtocolConstants.DI_DISCONNECT_CAUSE_DO_NOT_WANT_TO_TALK_TO_YOU));
		} else {
			add(new AVP_Unsigned32(ProtocolConstants.DI_DISCONNECT_CAUSE, ProtocolConstants.DI_DISCONNECT_CAUSE_BUSY));
		}
		Utils.setMandatory_RFC3588(this);
	}

}
