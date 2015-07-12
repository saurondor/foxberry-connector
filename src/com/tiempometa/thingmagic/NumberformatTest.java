/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.thingmagic;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class NumberformatTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Integer shortValue = Integer.valueOf("9fff", 16);
		System.out.println("value:" + shortValue.shortValue());
		String accessPassword = "afafafaf";
		Integer password = Long.valueOf(accessPassword, 16).intValue();
		System.out.println(password);
		System.out.println(Integer.toHexString(password));

	}

}
