/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tempometa.sandbox;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class GenerateHexCodes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String code = "00000000000000000000048A";
		Integer targetLength = code.length();
		System.out.println("Target length = " + targetLength);
		// TODO Auto-generated method stub
		for (int i = 0; i < 2000; i++) {
			String hexCode = Integer.toHexString(i + 1);
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < (targetLength-hexCode.length()); j++) {
				buffer.append("0");
			}
			buffer.append(hexCode);
			System.out.println((i + 1) + "," + buffer.toString());
		}
	}

}
