package org.scraper.util;

import org.apache.commons.codec.binary.StringUtils;

public class DummyUtils {
	public static String getMode() {
		String mode = System.getenv("MODE");
		return mode;
	}
	public static boolean isDev() {
		String mode = getMode();
		return StringUtils.equals(mode, "dev");
	}
}
