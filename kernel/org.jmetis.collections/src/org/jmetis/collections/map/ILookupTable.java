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

/**
 * {@code ILookupTable}
 * 
 * @author era
 */
public interface ILookupTable<K, V> {

	/**
	 * Returns {@code true} if the receiver contains no key-value mappings.
	 * 
	 * @return {@code true} if the receiver contains no key-value mappings
	 */
	boolean isEmpty();

	/**
	 * Returns {@code true} if the receiver contains a mapping for the given
	 * {@code key}.
	 * 
	 * @param key
	 *            key whose presence in the receiver is to be tested
	 * @return {@code true} if the receiver contains a mapping for the given
	 *         {@code key}
	 * @throws ClassCastException
	 *             if the key is of an inappropriate type for the receiver
	 *             (optional)
	 * @throws NullPointerException
	 *             if the specified key is {@code null} and the receiver does
	 *             not permit {@code null} keys (optional)
	 */
	boolean containsKey(Object key);

	/**
	 * Returns the value to which the given {@code key} is mapped, or {@code
	 * null} if the receiver contains no mapping for the {@code key}.
	 * <p>
	 * If the receiver permits {@code null} values, then a return value of
	 * {@code null} does not <i>necessarily</i> indicate that the receiver
	 * contains no mapping for the {@code key}. The {@link #containsKey
	 * containsKey} operation may be used to distinguish these two cases.
	 * 
	 * @param key
	 *            the {@code key} whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or {@code null}
	 *         if the receiver contains no mapping for the {@code key}
	 * @throws ClassCastException
	 *             if the {@code key} is of an inappropriate type for the
	 *             receiver (optional)
	 * @throws NullPointerException
	 *             if the specified {@code key} is {@code null} and the receiver
	 *             does not permit {@code null} keys (optional)
	 */
	V get(Object key);

	/**
	 * Associates the given {@code value} with the given {@code key} in the
	 * receiver (optional operation).
	 * 
	 * @param key
	 *            {@code key} with which the given {@code value} is to be
	 *            associated
	 * @param value
	 *            {@code value} to be associated with the given {@code key}
	 * @return the previous {@code value} associated with {@code key}, or
	 *         {@code null}
	 * @throws UnsupportedOperationException
	 *             if the {@code put} operation is not supported by the receiver
	 * @throws ClassCastException
	 *             if the class of the given {@code key} or @code value}
	 *             prevents it from being stored in the receiver
	 * @throws NullPointerException
	 *             if the specified given {@code key} or @code value} is {@code
	 *             null} and the receiver does not permit {@code null} keys or
	 *             values
	 * @throws IllegalArgumentException
	 *             if some property of the given {@code key} or @code value}
	 *             prevents it from being stored in the receiver
	 */
	V put(K key, V value);

	/**
	 * Removes the mapping for a {@code key} from the receiver if it is present
	 * (optional operation).
	 * <p>
	 * Returns the value to which the receiver previously associated the {@code
	 * key}, or {@code null} if the receiver contained no mapping for the
	 * {@code key}.
	 * <p>
	 * 
	 * @param key
	 *            {@code key} whose mapping is to be removed from the receiver
	 * @return the previous value associated with {@code key}, or {@code null}
	 *         if there was no mapping for {@code key}
	 * @throws UnsupportedOperationException
	 *             if the {@code remove} operation is not supported by the
	 *             receiver
	 * @throws ClassCastException
	 *             if the {@code key} is of an inappropriate type for the
	 *             receiver (optional)
	 * @throws NullPointerException
	 *             if the {@code key} is {@code null} and the receiver does not
	 *             permit {@code null} keys (optional)
	 */
	V remove(Object key);

	/**
	 * Returns the number of key-value mappings in the receiver.
	 * 
	 * @return the number of key-value mappings in the receiver
	 */
	int size();

	/**
	 * Removes all of the mappings from the receiver (optional operation). The
	 * receiver will be empty after this call returns.
	 * 
	 * @throws UnsupportedOperationException
	 *             if the {@code clear} operation is not supported by the
	 *             receiver
	 */
	void clear();

}
