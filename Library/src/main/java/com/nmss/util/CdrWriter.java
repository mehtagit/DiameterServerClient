package com.nmss.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CdrWriter {

	protected static Logger logger = LogManager.getLogger(CdrWriter.class);

	public static void write(Object cdr) {
		String json = Config.gson.toJson(cdr);
		// logger.info(json);
		Utility.UDP_SEND(Config.cdrIp, Config.cdrPort, json, false);
	}
}
