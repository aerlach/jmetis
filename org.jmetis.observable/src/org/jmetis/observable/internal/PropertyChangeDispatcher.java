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
package org.jmetis.observable.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/**
 * {@code PropertyChangeDispatcher}
 * 
 * @author aerlach
 */
public class PropertyChangeDispatcher implements IPropertyChangeDispatcher {

	private static final String NULL_PROPERTY_NAME = "";

	private static final int[] PRIME_NUMBERS = { 11, 17, 23, 31, 37, 43, 47,
			67, 79, 89, 97, 137, 163, 179, 197, 277, 311, 331, 359, 379, 397,
			433, 557, 599, 631, 673, 719, 761, 797, 877, 953, 1039 };

	private Object eventSource;

	private String[] propertyNames;

	private PropertyChangeListener[] propertyChangeListeners;

	private int numberOfPropertyChangeListeners;

	/**
	 * Constructs a new {@code PropertyChangeDispatcher} instance.
	 * 
	 */
	public PropertyChangeDispatcher(Object eventSource) {
		super();
		// this.eventSource = Assertions.mustNotBeNull("eventSource",
		// eventSource);
		this.eventSource = eventSource;
		propertyNames = new String[10];
		propertyChangeListeners = new PropertyChangeListener[11];
		Arrays.fill(propertyChangeListeners, NullPropertyChangeListener
				.defaultInstance());
	}

	protected int threshold() {
		return propertyNames.length / 2;
	}

	protected int growFactorFor(int slotsToInsert) {
		return propertyChangeListeners.length + slotsToInsert;
	}

	protected int primeCapacityFor(int capacity) {
		int index = Arrays.binarySearch(PropertyChangeDispatcher.PRIME_NUMBERS,
				capacity);
		return PropertyChangeDispatcher.PRIME_NUMBERS[index >= 0 ? index
				: -index - 1];
	}

	protected int hashCodeOf(String key) {
		if (key != null) {
			return key.hashCode();
		}
		return 0;
	}

	protected boolean isMatchFor(String key, String hashKey) {
		return key == hashKey || key.equals(hashKey);
	}

	protected void rehashCollisions(int hashIndex, int targetIndex,
			int numberOfCollisions) {
		int capacity = propertyNames.length;
		for (int i = targetIndex + (++numberOfCollisions << 1 - 1); i < capacity;) {
			String hashKey = propertyNames[i];
			if (hashKey == null) {
				propertyNames[targetIndex] = propertyNames[i];
				propertyChangeListeners[targetIndex] = propertyChangeListeners[i];
				return;
			}
			int realIndex = hashCodeOf(hashKey) & 0x7FFFFFFF & capacity - 1;
			if (realIndex <= targetIndex || realIndex > i) {
				propertyNames[targetIndex] = hashKey;
				propertyChangeListeners[targetIndex] = propertyChangeListeners[i];
				targetIndex = i;
			}
			i += ++numberOfCollisions << 1 - 1;
		}
		for (int i = 0; i < hashIndex;) {
			String hashKey = propertyNames[i];
			if (hashKey == null) {
				propertyNames[targetIndex] = propertyNames[i];
				propertyChangeListeners[targetIndex] = propertyChangeListeners[i];
				return;
			}
			int realIndex = hashCodeOf(hashKey) & 0x7FFFFFFF & capacity - 1;
			if (realIndex <= targetIndex || realIndex > i) {
				propertyNames[targetIndex] = hashKey;
				propertyChangeListeners[targetIndex] = propertyChangeListeners[i];
				targetIndex = i;
			}
			i += ++numberOfCollisions << 1 - 1;
		}
		propertyNames[targetIndex] = null;
		propertyChangeListeners[targetIndex] = NullPropertyChangeListener
				.defaultInstance();
	}

	protected void expandCapacity(int slotsToInsert) {
		int oldCapacity = propertyNames.length;
		int newCapacity = primeCapacityFor(oldCapacity
				+ growFactorFor(slotsToInsert));
		String[] newKeys = new String[newCapacity];
		PropertyChangeListener[] newValues = new PropertyChangeListener[newCapacity];
		for (int i = 0; i < oldCapacity; i++) {
			String hashKey = propertyNames[i];
			if (hashKey != null) {
				int hashIndex = hashCodeOf(hashKey) & 0x7FFFFFFF & newCapacity
						- 1;
				int numberOfCollisions = 0;
				int j;
				for (j = hashIndex; j < newCapacity && newKeys[j] != null; j++) {
					j += ++numberOfCollisions << 1 - 1;
				}
				if (j == newCapacity) {
					for (j = 0; j < hashIndex && newKeys[j] != null; j++) {
						j += ++numberOfCollisions << 1 - 1;
					}
				}
				newKeys[j] = hashKey;
				newValues[j] = propertyChangeListeners[i];
			}
		}
		propertyNames = newKeys;
		propertyChangeListeners = newValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * addPropertyChangeListener(java.lang.String,
	 * java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeDispatcher addPropertyChangeListener(
			String propertyName, PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListener != null) {
			if (propertyName == null) {
				propertyName = PropertyChangeDispatcher.NULL_PROPERTY_NAME;
			}
			do {
				int capacity = propertyNames.length;
				int hashIndex = hashCodeOf(propertyName) & 0x7FFFFFFF
						& capacity - 1;
				int numberOfCollisions = 0;
				for (int i = hashIndex; i < capacity;) {
					String key = propertyNames[i];
					if (key == null) {
						propertyNames[i] = propertyName;
						propertyChangeListeners[i] = propertyChangeListener;
						if (++numberOfPropertyChangeListeners > threshold()) {
							expandCapacity(1);
						}
						return this;
					}
					if (isMatchFor(propertyName, key)) {
						PropertyChangeListener value = propertyChangeListeners[i];
						if (value instanceof CompoundPropertyChangeListener) {
							((CompoundPropertyChangeListener) value)
									.addPropertyChangeListener(propertyChangeListener);
						} else {
							propertyChangeListeners[i] = new CompoundPropertyChangeListener(
									propertyChangeListener);
						}
						return this;
					}
					i += ++numberOfCollisions << 1 - 1;
				}
				for (int i = 0; i < hashIndex;) {
					String key = propertyNames[i];
					if (key == null) {
						propertyNames[i] = propertyName;
						propertyChangeListeners[i] = propertyChangeListener;
						if (++numberOfPropertyChangeListeners > threshold()) {
							expandCapacity(1);
						}
						return this;
					}
					if (isMatchFor(propertyName, key)) {
						PropertyChangeListener value = propertyChangeListeners[i];
						if (value instanceof CompoundPropertyChangeListener) {
							((CompoundPropertyChangeListener) value)
									.addPropertyChangeListener(propertyChangeListener);
						} else {
							propertyChangeListeners[i] = new CompoundPropertyChangeListener(
									propertyChangeListener);
						}
						return this;
					}
					i += ++numberOfCollisions << 1 - 1;
				}
				expandCapacity(1);
			} while (true);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeDispatcher addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		return this.addPropertyChangeListener(null, propertyChangeListener);
	}

	protected PropertyChangeListener propertyChangeListenerFor(
			String propertyName) {
		if (propertyName == null) {
			propertyName = PropertyChangeDispatcher.NULL_PROPERTY_NAME;
		}
		int capacity = propertyNames.length;
		int hashIndex = hashCodeOf(propertyName) & 0x7FFFFFFF & capacity - 1;
		int numberOfCollisions = 0;
		for (int i = hashIndex; i < capacity;) {
			String hashKey = propertyNames[i];
			if (hashKey == null) {
				return NullPropertyChangeListener.defaultInstance();
			}
			if (isMatchFor(propertyName, hashKey)) {
				return propertyChangeListeners[i];
			}
			i += ++numberOfCollisions << 1 - 1;
		}
		for (int i = 0; i < hashIndex;) {
			String hashKey = propertyNames[i];
			if (hashKey == null) {
				return NullPropertyChangeListener.defaultInstance();
			}
			if (isMatchFor(propertyName, hashKey)) {
				return propertyChangeListeners[i];
			}
			i += ++numberOfCollisions << 1 - 1;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, java.lang.Object,
	 * java.lang.Object)
	 */
	public void firePropertyChangeEvent(String propertyName, Object oldValue,
			Object newValue) {
		if (oldValue != newValue || oldValue != null
				&& !oldValue.equals(newValue)) {
			if (propertyName == null) {
				PropertyChangeListener propertyChangeListener = propertyChangeListenerFor(null);
				if (propertyChangeListener != NullPropertyChangeListener
						.defaultInstance()) {
					propertyChangeListener
							.propertyChange(new PropertyChangeEvent(
									eventSource, propertyName, oldValue,
									newValue));
				}
			} else {
				PropertyChangeListener propertyChangeListener = propertyChangeListenerFor(null);
				if (propertyChangeListener != NullPropertyChangeListener
						.defaultInstance()) {
					propertyChangeListener
							.propertyChange(new PropertyChangeEvent(
									eventSource, propertyName, oldValue,
									newValue));
				}
				propertyChangeListener = propertyChangeListenerFor(propertyName);
				if (propertyChangeListener != NullPropertyChangeListener
						.defaultInstance()) {
					propertyChangeListener
							.propertyChange(new PropertyChangeEvent(
									eventSource, propertyName, oldValue,
									newValue));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, boolean, boolean)
	 */
	public void firePropertyChangeEvent(String propertyName, boolean oldValue,
			boolean newValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, byte, byte)
	 */
	public void firePropertyChangeEvent(String propertyName, byte oldValue,
			byte newValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, char, char)
	 */
	public void firePropertyChangeEvent(String propertyName, char oldValue,
			char newValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, double, double)
	 */
	public void firePropertyChangeEvent(String propertyName, double oldValue,
			double newValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, float, float)
	 */
	public void firePropertyChangeEvent(String propertyName, float oldValue,
			float newValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, int, int)
	 */
	public void firePropertyChangeEvent(String propertyName, int oldValue,
			int newValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, long, long)
	 */
	public void firePropertyChangeEvent(String propertyName, long oldValue,
			long newValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * firePropertyChangeEvent(java.lang.String, short, short)
	 */
	public void firePropertyChangeEvent(String propertyName, short oldValue,
			short newValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * removePropertyChangeListener(java.lang.String,
	 * java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeDispatcher removePropertyChangeListener(
			String propertyName, PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListener != null) {
			if (propertyName == null) {
				propertyName = PropertyChangeDispatcher.NULL_PROPERTY_NAME;
			}
			int capacity = propertyNames.length;
			int hashIndex = hashCodeOf(propertyName) & 0x7FFFFFFF & capacity
					- 1;
			int numberOfCollisions = 0;
			for (int i = hashIndex; i < capacity;) {
				String hashKey = propertyNames[i];
				if (hashKey == null) {
					return this;
				}
				if (isMatchFor(propertyName, hashKey)) {
					PropertyChangeListener value = propertyChangeListeners[i];
					if (value instanceof CompoundPropertyChangeListener) {
						((CompoundPropertyChangeListener) value)
								.removePropertyChangeListener(propertyChangeListener);
					} else {
						propertyNames = null;
						propertyChangeListeners[i] = null;
						rehashCollisions(hashIndex, i, numberOfCollisions);
					}
					numberOfPropertyChangeListeners--;
					return this;
				}
				i += ++numberOfCollisions << 1 - 1;
			}
			for (int i = 0; i < hashIndex;) {
				String hashKey = propertyNames[i];
				if (hashKey == null) {
					return this;
				}
				if (isMatchFor(propertyName, hashKey)) {
					PropertyChangeListener value = propertyChangeListeners[i];
					if (value instanceof CompoundPropertyChangeListener) {
						((CompoundPropertyChangeListener) value)
								.removePropertyChangeListener(propertyChangeListener);
					} else {
						propertyNames = null;
						propertyChangeListeners[i] = null;
						rehashCollisions(hashIndex, i, numberOfCollisions);
					}
					numberOfPropertyChangeListeners--;
					return this;
				}
				i += ++numberOfCollisions << 1 - 1;
			}
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jmetis.observable.internal.IPropertyChangeDispatcher#
	 * removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public IPropertyChangeDispatcher removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		return this.removePropertyChangeListener(null, propertyChangeListener);
	}

	public static void main(String[] a) {
		if (false) {
			PropertyChangeListener p = new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println(evt.getPropertyName() + ": "
							+ evt.getOldValue() + ", " + evt.getNewValue());
				}
			};
			PropertyChangeDispatcher pd = new PropertyChangeDispatcher(
					new Object());
			pd.addPropertyChangeListener(p);
			System.out.println(pd.numberOfPropertyChangeListeners);
			pd.addPropertyChangeListener("test", p);
			System.out.println(pd.numberOfPropertyChangeListeners);
			pd.addPropertyChangeListener("test", p);
			System.out.println(pd.numberOfPropertyChangeListeners);
			System.out.println("fire change test");
			pd.firePropertyChangeEvent("test", "abc", "cba");
			System.out.println("fire change null");
			pd.firePropertyChangeEvent(null, "abc", "cba");
			pd.removePropertyChangeListener("test", p);
			System.out.println("fire change test");
			pd.firePropertyChangeEvent("test", "abc", "cba");
			System.out.println(pd.numberOfPropertyChangeListeners);
			pd.removePropertyChangeListener(p);
			System.out.println(pd.numberOfPropertyChangeListeners);
		}
		int size = 60;
		for (int i = 1; i < 1000; i++) {
			int i1 = i & size - 1;
			int i2 = i % size;
			if (i1 != i2 && i1 >= 60) {
				System.err.println(i + ": " + i1 + "/" + i2);
			}
		}
		long t1 = System.currentTimeMillis();
		for (int i = 1; i < 100000000; i++) {
			int i1 = i & size - 1;
			if (i1 > 60) {
				System.err.println();
			}
		}
		System.out.println(System.currentTimeMillis() - t1);

		t1 = System.currentTimeMillis();
		for (int i = 1; i < 100000000; i++) {
			int i1 = i % size;
			if (i1 > 60) {
				System.err.println();
			}
		}
		System.out.println(System.currentTimeMillis() - t1);

	}
}
