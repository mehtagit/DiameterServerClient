package dk.i1.diameter;

import java.util.Arrays;

/**
 * AVP containing arbitrary data of variable length.
 */
public class AVP_OctetString extends AVP {
	public AVP_OctetString(AVP avp) {
		super(avp);
	}

	public AVP_OctetString(int code, byte value[]) {
		super(code, value);
	}

	public AVP_OctetString(int code, int vendor_id, byte value[]) {
		super(code, vendor_id, value);
	}

	public byte[] queryValue() {
		return queryPayload();
	}

	public void setValue(byte value[]) {
		setPayload(value, 0, value.length);
	}

	@Override
	public String toString() {
		return "AVP[" + code + "," + Arrays.toString(queryValue()) + "]";
	}

}
