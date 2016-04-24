/*
 * Copyright 2016 Erwin Müller <erwin.mueller@deventm.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.types.external;

/**
 * Named binding address.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum BindingAddress {

	/**
	 * Loopback address {@code 127.0.0.1}
	 */
	loopback("127.0.0.1"),

	/**
	 * Local host address {@code 127.0.0.1}
	 */
	local("127.0.0.1"),

	/**
	 * All address {@code 0.0.0.0}
	 */
	all("0.0.0.0");

    private String address;

	private BindingAddress(String address) {
        this.address = address;
	}

    public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
        return address;
	}
}
