/*
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
package org.jmetis.test.model;

import java.util.ArrayList;
import java.util.Collection;

import org.jmetis.observable.object.BeanModel;

/**
 * {@code Address}
 * 
 * @author aerlach
 */
public class Address extends BeanModel {

	private String city;

	private String code;

	private String street;

	private String number;

	private boolean main;

	/**
	 * Constructs a new {@code Address} instance.
	 */
	public Address() {
		super();
	}

	public static Collection<Address> createSampleData(String id) {
		Collection<Address> sampleData = new ArrayList<Address>();
		Address address = new Address();
		address.setCity("Linz" + id);
		address.setStreet("Hafenstrasse" + id);
		address.setNumber(id);
		sampleData.add(address);
		address = new Address();
		address.setCity("Linz" + id);
		address.setStreet("Bl�tenstrasse" + id);
		address.setNumber(id);
		address.setMain(true);
		sampleData.add(address);
		return sampleData;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.firePropertyChangeEvent("city", this.city, this.city = city);
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.firePropertyChangeEvent("street", this.street,
				this.street = street);
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.firePropertyChangeEvent("main", this.main, this.main = main);
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.firePropertyChangeEvent("number", this.number,
				this.number = number);
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.firePropertyChangeEvent("code", this.code, this.code = code); //$NON-NLS-1$
	}

}
