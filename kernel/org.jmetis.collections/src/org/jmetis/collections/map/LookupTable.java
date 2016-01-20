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
package org.jmetis.collections.map;

import java.util.Arrays;

/**
 * {@code LookupTable}
 * 
 * @author era
 */
public class LookupTable<K, V> implements ILookupTable<K, V> {

	private static final int DEFAULT_INITIAL_CAPACITY = 10;

	private static final float DEFAULT_THRESHOLD = 0.5F;

	private final float loadFactor;

	private int threshold;

	private Object[] keys;

	private Object[] values;

	private int size;

	/**
	 * Constructs a new {@code LookupTable} instance.
	 */
	public LookupTable(int initialCapacity, float loadFactor) {
		super();
		final int capacity = initialCapacity;
		this.loadFactor = loadFactor;
		this.threshold = (int) (capacity * loadFactor);
		this.keys = new Object[capacity];
		this.values = new Object[capacity];
	}

	/**
	 * Constructs a new {@code LookupTable} instance.
	 */
	public LookupTable() {
		this(LookupTable.DEFAULT_INITIAL_CAPACITY,
				LookupTable.DEFAULT_THRESHOLD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.collections.ILookupTable#isEmpty()
	 */
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Method returning the index in <tt>ProbingHashTable.table</tt> of a key,
	 * if present, or the index at which to insert it, if absent. Uses a version
	 * of double hashing.
	 * 
	 * @param key
	 *            the key to search for
	 * @return the position where the search terminates
	 */
	protected int indexFor(Object key, int index, int length) {
		int probe = 1;
		while (this.keys[index] != null && !this.keys[index].equals(key)) {
			index += probe;
			if (index == length) {
				index = 1;
			}
			probe += 2;
		}
		return index;
	}

	/**
	 * Applies a supplemental hash function to a given hashCode, which defends
	 * against poor quality hash functions. This is critical because HashMap
	 * uses power-of-two length hash tables, that otherwise encounter collisions
	 * for hashCodes that do not differ in lower bits. Note: Null keys always
	 * map to hash 0, thus index 0.
	 */
	protected int hashCodeOf(Object key) {
		int hashCode = key.hashCode();
		hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
		return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
	}

	/**
	 * Returns index for hash code h.
	 */
	protected int indexFor(Object key) {
		if (key == null) {
			return 0;
		}
		return this.indexFor(key, this.hashCodeOf(key) & this.keys.length - 1,
				this.keys.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.collections.ILookupTable#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return this.values[this.indexFor(key)] != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.collections.ILookupTable#get(java.lang.Object)
	 */
	public V get(Object key) {
		return (V) this.values[this.indexFor(key)];
	}

	protected Object addValue(Object oldValue, Object newValue) {
		return newValue;
	}

	/*
	 * rehashing method for when the table occupancy grows too large
	 */
	private void rehash() {
		final int capacity = this.keys.length * 2;
		final Object[] oldKeys = this.keys;
		final Object[] oldValues = this.values;
		this.keys = new Object[capacity];
		this.values = new Object[capacity];
		this.threshold = (int) (capacity * this.loadFactor);
		this.size = 0;
		for (int i = 0, n = this.keys.length; i < n; i++) {
			final Object value = oldValues[i];
			if (value != null) {
				this.put((K) oldKeys[i], (V) value);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.collections.ILookupTable#put(java.lang.Object,
	 * java.lang.Object)
	 */
	public V put(K key, V value) {
		final int index = this.indexFor(key);
		final Object oldValue = this.values[index];
		if (oldValue == null) {
			this.values[index] = value;
			this.size++;
		} else {
			this.values[index] = this.addValue(oldValue, value);
		}
		if (this.size >= this.threshold) {
			this.rehash();
		}
		return (V) oldValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.collections.ILookupTable#remove(java.lang.Object)
	 */
	public V remove(Object key) {
		final Object value = this.values[this.indexFor(key)];
		if (value != null) {
			this.size--;
		}
		return (V) value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.collections.ILookupTable#size()
	 */
	public int size() {
		return this.size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.collections.ILookupTable#clear()
	 */
	public void clear() {
		Arrays.fill(this.keys, null);
		Arrays.fill(this.values, null);
		this.size = 0;
	}

	public static void main(String[] a) {
		final ILookupTable<String, String> l = new LookupTable<String, String>();
		System.out.println(l.put("1", "a"));
		System.out.println(l.put("2", "b"));
		System.out.println(l.put("3", "c"));
		System.out.println(l.get("1"));
		System.out.println(l.get("2"));
		System.out.println(l.get("3"));
		System.out.println(l.remove("1"));
		System.out.println(l.put("2", "b"));
		System.out.println(l.put("3", "c"));
	}

}
