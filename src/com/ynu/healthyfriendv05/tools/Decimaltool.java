package com.ynu.healthyfriendv05.tools;

import java.text.DecimalFormat;

public class Decimaltool {
	public static String getDoublePrecision(double decimal) {
		String c = null;
		DecimalFormat df = new DecimalFormat("#.00");
		c = df.format(decimal);
		return c;
	}

	public static String getSinglePrecision(double decimal) {
		String c = null;
		DecimalFormat df = new DecimalFormat("#.0");
		c = df.format(decimal);
		return c;
	}
}