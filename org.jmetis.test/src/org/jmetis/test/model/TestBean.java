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

/**
 * {@code TestBean}
 * 
 * @author aerlach
 */
public class TestBean {

	private boolean booleanField;

	private byte byteField;

	private char charField;

	private double doubleField;

	private float floatField;

	private int intField;

	private long longField;

	private short shortField;

	private Object objectField;

	private String stringField;

	public TestBean(boolean booleanField, byte byteField, char charField,
			double doubleField, float floatField, int intField, long longField,
			short shortField, Object objectField, String stringField) {
		super();
		this.booleanField = booleanField;
		this.byteField = byteField;
		this.charField = charField;
		this.doubleField = doubleField;
		this.floatField = floatField;
		this.intField = intField;
		this.longField = longField;
		this.shortField = shortField;
		this.objectField = objectField;
		this.stringField = stringField;
	}

	public TestBean() {
		super();
	}

	public boolean isBooleanField() {
		return booleanField;
	}

	public void setBooleanField(boolean booleanField) {
		this.booleanField = booleanField;
	}

	public byte getByteField() {
		return byteField;
	}

	public void setByteField(byte byteField) {
		this.byteField = byteField;
	}

	public char getCharField() {
		return charField;
	}

	public void setCharField(char charField) {
		this.charField = charField;
	}

	public double getDoubleField() {
		return doubleField;
	}

	public void setDoubleField(double doubleField) {
		this.doubleField = doubleField;
	}

	public float getFloatField() {
		return floatField;
	}

	public void setFloatField(float floatField) {
		this.floatField = floatField;
	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}

	public long getLongField() {
		return longField;
	}

	public void setLongField(long longField) {
		this.longField = longField;
	}

	public short getShortField() {
		return shortField;
	}

	public void setShortField(short shortField) {
		this.shortField = shortField;
	}

	public Object getObjectField() {
		return objectField;
	}

	public void setObjectField(Object objectField) {
		this.objectField = objectField;
	}

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (booleanField ? 1231 : 1237);
		result = prime * result + byteField;
		result = prime * result + charField;
		long temp;
		temp = Double.doubleToLongBits(doubleField);
		result = prime * result + (int) (temp ^ temp >>> 32);
		result = prime * result + Float.floatToIntBits(floatField);
		result = prime * result + intField;
		result = prime * result + (int) (longField ^ longField >>> 32);
		result = prime * result
				+ (objectField == null ? 0 : objectField.hashCode());
		result = prime * result + shortField;
		result = prime * result
				+ (stringField == null ? 0 : stringField.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		TestBean other = (TestBean) obj;
		if (booleanField != other.booleanField) {
			return false;
		}
		if (byteField != other.byteField) {
			return false;
		}
		if (charField != other.charField) {
			return false;
		}
		if (Double.doubleToLongBits(doubleField) != Double
				.doubleToLongBits(other.doubleField)) {
			return false;
		}
		if (Float.floatToIntBits(floatField) != Float
				.floatToIntBits(other.floatField)) {
			return false;
		}
		if (intField != other.intField) {
			return false;
		}
		if (longField != other.longField) {
			return false;
		}
		if (objectField == null) {
			if (other.objectField != null) {
				return false;
			}
		} else if (objectField != this
				&& !objectField.equals(other.objectField)) {
			return false;
		}
		if (shortField != other.shortField) {
			return false;
		}
		if (stringField == null) {
			if (other.stringField != null) {
				return false;
			}
		} else if (!stringField.equals(other.stringField)) {
			return false;
		}
		return true;
	}

}
