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
package org.jmetis.collections.observable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code KeyedCollection}
 * 
 * @author aerlach
 * @param <K>
 * @param <V>
 */
public class KeyedCollection<K, V> extends AbstractMap<K, V> implements
		IKeyedModel<K, V> {

	protected transient PropertyChangeSupport propertyChangeListeners;

	private final Map<K, V> observedMap;

	private Set<Map.Entry<K, V>> entrySet;

	/**
	 * Constructs a new {@code KeyedCollection} instance.
	 * 
	 * @param observedMap
	 */
	public KeyedCollection(Map<K, V> observedMap) {
		super();
		this.observedMap = Assertions.mustNotBeNull("observedMap", observedMap); //$NON-NLS-1$
	}

	protected synchronized PropertyChangeSupport propertyChangeListeners() {
		if (this.propertyChangeListeners == null) {
			this.propertyChangeListeners = new PropertyChangeSupport(this);
		}
		return this.propertyChangeListeners;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IKeyedModel#addPropertyChangeListener(java.beans
	 * .PropertyChangeListener)
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners == null) {
			this.propertyChangeListeners = new PropertyChangeSupport(this);
		}
		this.propertyChangeListeners
				.addPropertyChangeListener(propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IKeyedModel#addPropertyChangeListener(java.lang
	 * .String, java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String key,
			PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners == null) {
			this.propertyChangeListeners = new PropertyChangeSupport(this);
		}
		this.propertyChangeListeners.addPropertyChangeListener(key,
				propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IKeyedModel#removePropertyChangeListener(java.beans
	 * .PropertyChangeListener)
	 */
	public void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners != null) {
			this.propertyChangeListeners
					.removePropertyChangeListener(propertyChangeListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IKeyedModel#removePropertyChangeListener(java.lang
	 * .String, java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String key,
			PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners != null) {
			this.propertyChangeListeners.removePropertyChangeListener(key,
					propertyChangeListener);
		}
	}

	protected void fireElementChanged(String key, Object oldValue,
			Object newValue) {
		if (this.propertyChangeListeners != null) {
			this.propertyChangeListeners.firePropertyChange(key, oldValue,
					newValue);
		}
	}

	protected void fireElementChanged(K key, V oldValue, V newValue) {
		if (key == null) {
			this.fireElementChanged((String) null, oldValue, newValue);
		} else {
			this.fireElementChanged(String.valueOf(key), oldValue, newValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#clear()
	 */
	@Override
	public void clear() {
		final Iterator<K> iterator = this.keySet().iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.observedMap.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		return this.observedMap.containsValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#entrySet()
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		final Set<Map.Entry<K, V>> entrySet = this.entrySet;
		return entrySet != null ? entrySet : (this.entrySet = new EntrySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {
		return this.observedMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.observedMap.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		final V oldValue = this.observedMap.put(key, value);
		this.fireElementChanged(key, oldValue, value);
		return oldValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (final Entry<? extends K, ? extends V> entry : map.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#remove(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object key) {
		final V oldValue = this.observedMap.remove(key);
		this.fireElementChanged((K) key, oldValue, (V) null);
		return oldValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#size()
	 */
	@Override
	public int size() {
		return this.observedMap.size();
	}

	private class EntryIterator implements Iterator<Map.Entry<K, V>> {
		private final Iterator<Map.Entry<K, V>> realIterator;
		private Map.Entry<K, V> last;

		EntryIterator() {
			this.realIterator = KeyedCollection.this.observedMap.entrySet()
					.iterator();
		}

		public boolean hasNext() {
			return this.realIterator.hasNext();
		}

		public Map.Entry<K, V> next() {
			this.last = this.realIterator.next();
			return this.last;
		}

		public void remove() {
			if (this.last == null) {
				throw new IllegalStateException();
			}
			final Object toRemove = this.last.getKey();
			this.last = null;
			KeyedCollection.this.remove(toRemove);
		}
	}

	private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			final Map.Entry<K, V> e = (Map.Entry<K, V>) o;
			return KeyedCollection.this.containsKey(e.getKey());
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean remove(Object o) {
			if (o instanceof Map.Entry) {
				final K key = ((Map.Entry<K, V>) o).getKey();
				if (KeyedCollection.this.containsKey(key)) {
					this.remove(key);
					return true;
				}
			}
			return false;
		}

		@Override
		public int size() {
			return KeyedCollection.this.size();
		}

		@Override
		public void clear() {
			KeyedCollection.this.clear();
		}
	}

}
