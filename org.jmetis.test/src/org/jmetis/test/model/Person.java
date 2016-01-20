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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.jmetis.observable.object.BeanModel;

/**
 * {@code Person}
 * 
 * @author aerlach
 */
public class Person extends BeanModel {

	private String firstName;

	private String lastName;

	private Date dayOfBirth;

	private int age;

	private Address defaultAddress;

	private Collection<Address> addresses;

	private BigDecimal salary;

	private Gender gender;

	private boolean manager;

	/**
	 * Constructs a new {@code Person} instance.
	 */
	public Person() {
		super();
	}

	public static Collection<Person> createSampleData(String id) {
		Collection<Person> sampleData = new ArrayList<Person>();
		Person person = new Person();
		person.setFirstName("Max" + id);
		person.setLastName("Muster" + id);
		person.setDayOfBirth(new Date());
		person.setAddresses(Address.createSampleData("0"));
		person.setSalary(new BigDecimal("1500.00"));
		person.setGender(Gender.M);
		person.setManager(true);
		sampleData.add(person);
		person = new Person();
		person.setFirstName("Moritz" + id);
		person.setLastName("Muster" + id);
		person.setDayOfBirth(new Date());
		person.setAddresses(Address.createSampleData("1"));
		person.setSalary(new BigDecimal("1000.00"));
		person.setGender(Gender.M);
		person.setManager(false);
		sampleData.add(person);
		return sampleData;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firePropertyChangeEvent("firstName", this.firstName,
				this.firstName = firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.firePropertyChangeEvent("lastName", this.lastName,
				this.lastName = lastName);
	}

	public Date getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(Date dayOfBirth) {
		age = 0;
		this.firePropertyChangeEvent("dayOfBirth", this.dayOfBirth,
				this.dayOfBirth = dayOfBirth);
	}

	public Address getDefaultAddress() {
		if (defaultAddress == null) {
			Collection<Address> addresses = getAddresses();
			if (addresses != null) {
				for (Address address : addresses) {
					if (address.isMain()) {
						defaultAddress = address;
						break;
					}
				}
			}
		}
		return defaultAddress;
	}

	public Collection<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Collection<Address> addresses) {
		this.firePropertyChangeEvent("addresses", this.addresses,
				this.addresses = addresses);
	}

	public int getAge() {
		if (age == 0 && dayOfBirth != null) {
			Calendar today = Calendar.getInstance();
			Calendar dayOfBirth = Calendar.getInstance();
			dayOfBirth.setTime(this.dayOfBirth);
			age = Math.max(today.get(Calendar.YEAR)
					- dayOfBirth.get(Calendar.YEAR), 1);
		}
		return age;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.firePropertyChangeEvent("salary", this.salary,
				this.salary = salary);
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.firePropertyChangeEvent("gender", this.gender,
				this.gender = gender);
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.firePropertyChangeEvent("manager", this.manager,
				this.manager = manager);
	}

}
