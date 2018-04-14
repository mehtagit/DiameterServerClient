/**
 * 
 */
package com.nmss.messages;

import java.net.InetAddress;
import java.util.Random;

import dk.i1.diameter.AVP;
import dk.i1.diameter.AVP_Address;
import dk.i1.diameter.AVP_Grouped;
import dk.i1.diameter.AVP_OctetString;
import dk.i1.diameter.AVP_UTF8String;
import dk.i1.diameter.AVP_Unsigned32;
import dk.i1.diameter.Message;
import dk.i1.diameter.ProtocolConstants;
import dk.i1.diameter.Utils;

/**
 * @author Varun
 *
 *         User Data Request
 */
public class UDRMessage extends Message {

	public UDRMessage(String ORIGIN_HOST, String ORIGIN_REALM, String DESTINATION_HOST, String DESTINATION_REALM,
			Integer vendor_id, Integer AUTH_APP, String IMPI, String tid, String IMPU, String msisdn,
			Integer dataReference, Integer featureListId, Integer featureList, String wildCardPublicIdentity,
			String wildCardImpu, String serverName, String serviceIndication, Integer identitySet,
			Integer requestedDomain, Integer currentLocation, String dsaiTag, Integer sessionPriority,
			Integer requestedNodes, Integer servingNodeIndication) {
		super.hdr.setRequest(true);
		super.hdr.command_code = ProtocolConstants.DIAMETER_USER_DATA_REQUEST;
		super.hdr.application_id = 0;
		super.hdr.hop_by_hop_identifier = (new Random()).nextInt();

		add(new AVP_UTF8String(ProtocolConstants.DI_SESSION_ID, tid));

		AVP authdata[] = new AVP[2];
		authdata[0] = new AVP_Unsigned32(ProtocolConstants.DI_AUTH_APPLICATION_ID, AUTH_APP);
		add(new AVP_Grouped(ProtocolConstants.DI_VENDOR_SPECIFIC_APPLICATION_ID, authdata));

		add(new AVP_Unsigned32(ProtocolConstants.DI_AUTH_SESSION_STATE, 1));
		add(new AVP_UTF8String(ProtocolConstants.DI_USER_NAME, IMPI));
		add(new AVP_UTF8String(ProtocolConstants.DI_ORIGIN_HOST, ORIGIN_HOST));
		add(new AVP_UTF8String(ProtocolConstants.DI_ORIGIN_REALM, ORIGIN_REALM));
		add(new AVP_UTF8String(ProtocolConstants.DI_DESTINATION_REALM, DESTINATION_REALM));
		InetAddress inetaddress = null;
		try {
			inetaddress = InetAddress.getByName(DESTINATION_HOST);
		} catch (Exception e) {
			System.out.println(e);
		}

		add(new AVP_Address(ProtocolConstants.DI_HOST_IP_ADDRESS, inetaddress));

		if (IMPU != null || msisdn != null) {
			AVP userIdentity[] = new AVP[1];
			if (IMPU != null)
				authdata[0] = new AVP_UTF8String(ProtocolConstants.DI_PUBLIC_IDENTITY, IMPU);
			if (msisdn != null)
				authdata[0] = new AVP_OctetString(ProtocolConstants.DI_MSISDN, msisdn.getBytes());
			add(new AVP_Grouped(ProtocolConstants.DI_USER_IDENTITY, userIdentity));
		}
		add(new AVP_Unsigned32(ProtocolConstants.DI_DATA_REFRENCE, dataReference));

		/*** Optional parameters ***/

		if (vendor_id != null && featureListId != null && featureList != null) {
			AVP supportFeature[] = new AVP[3];
			authdata[0] = new AVP_Unsigned32(ProtocolConstants.DI_VENDOR_ID, vendor_id);
			authdata[1] = new AVP_Unsigned32(ProtocolConstants.DI_FEATURE_LIST_ID, featureListId);
			authdata[2] = new AVP_Unsigned32(ProtocolConstants.DI_FEATURE_LIST, featureList);
			add(new AVP_Grouped(ProtocolConstants.DI_SUPPORTED_FEATURE, supportFeature));
		}

		if (wildCardPublicIdentity != null)
			add(new AVP_UTF8String(ProtocolConstants.DI_WILDCARD_PUBLIC_IDENTITY, wildCardPublicIdentity));
		if (wildCardImpu != null)
			add(new AVP_UTF8String(ProtocolConstants.DI_WILDCARD_IMPU, wildCardImpu));
		if (serverName != null)
			add(new AVP_UTF8String(ProtocolConstants.DI_SERVER_NAME, serverName));
		if (serviceIndication != null)
			add(new AVP_OctetString(ProtocolConstants.DI_SERVICE_INDICATION, serviceIndication.getBytes()));
		if (identitySet != null)
			add(new AVP_Unsigned32(ProtocolConstants.DI_IDENTITY_SET, identitySet));
		if (requestedDomain != null)
			add(new AVP_Unsigned32(ProtocolConstants.DI_REQUESTED_DOMAIN, requestedDomain));
		if (currentLocation != null)
			add(new AVP_Unsigned32(ProtocolConstants.DI_CURRENT_LOCATION, currentLocation));
		if (dsaiTag != null)
			add(new AVP_OctetString(ProtocolConstants.DI_DSAI_TAG, dsaiTag.getBytes()));
		if (sessionPriority != null)
			add(new AVP_Unsigned32(ProtocolConstants.DI_SESSION_PRIORITY, sessionPriority));
		if (servingNodeIndication != null)
			add(new AVP_Unsigned32(ProtocolConstants.DI_SERVING_NODE_INDICATION, servingNodeIndication));
		if (requestedNodes != null)
			add(new AVP_Unsigned32(ProtocolConstants.DI_REQUESTED_NODES, requestedNodes));

		int i = (int) (System.currentTimeMillis() / 1000L);
		super.hdr.end_to_end_identifier = i << 20 | (new Random()).nextInt() & 0xfffff;

		Utils.setMandatory_RFC3588(this);
	}

}
