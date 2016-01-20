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
package org.jmetis.collections.decorator;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code MapDecorator} decorates another {@link Map} to provide additional
 * behavior.
 * 
 * @author era
 */
public class MapDecorator<K, V> implements Map<K, V> {

	protected final Map<K, V> component;

	/**
	 * Constructs a new {@code MapDecorator} instance that wraps (not copies)
	 * the given {@code component}.
	 * 
	 * @param component
	 *            the {@link Map} to decorate, must not be {@code null}
	 * @throws NullPointerException
	 *             if the given {@code component} is {@code null}
	 */
	public MapDecorator(Map<K, V> component) {
		super();
		this.component = Assertions.mustNotBeNull("component", //$NON-NLS-1$
				component);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		this.component.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return this.component.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return this.component.containsValue(value);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#entrySet()
	 */
	public Set<Map.Entry<K, V>> entrySet() {
		return this.component.entrySet();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public V get(Object key) {
		return this.component.get(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return this.component.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set<K> keySet() {
		return this.component.keySet();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public V put(K key, V value) {
		return this.component.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends K, ? extends V> map) {
		this.component.putAll(map);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public V remove(Object key) {
		return this.component.remove(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#size()
	 */
	public int size() {
		return this.component.size();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection<V> values() {
		return this.component.values();
	}

}
