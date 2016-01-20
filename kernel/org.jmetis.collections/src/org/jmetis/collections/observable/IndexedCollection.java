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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code IndexedCollection}
 * 
 * @author era
 * @param <E>
 */
public class IndexedCollection<E> extends AbstractList<E> implements
		IIndexedModel<E> {

	protected static final Object[] EMPTY_ELEMENT_ARRAY = {};

	protected List<E> observedList;

	protected transient PropertyChangeSupport propertyChangeListeners;

	/**
	 * Constructs a new {@code IndexedCollection} instance.
	 * 
	 * @param observedCollection
	 */
	public IndexedCollection(Collection<E> observedCollection) {
		super();
		this.observedList = new ArrayList<E>(Assertions.mustNotBeNull(
				"observedCollection", //$NON-NLS-1$
				observedCollection));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IIndexedModel#addPropertyChangeListener(java.beans
	 * .PropertyChangeListener)
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListener != null) {
			if (this.propertyChangeListeners == null) {
				this.propertyChangeListeners = new PropertyChangeSupport(this);
			}
			this.propertyChangeListeners
					.addPropertyChangeListener(propertyChangeListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.observable.IIndexedModel#removePropertyChangeListener(java
	 * .beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		if (propertyChangeListener != null
				&& this.propertyChangeListeners != null) {
			this.propertyChangeListeners
					.removePropertyChangeListener(propertyChangeListener);
		}
	}

	protected void fireElementsChanged(int index, Object[] oldElements,
			Object[] newElements) {
		if (this.propertyChangeListeners != null) {
			this.propertyChangeListeners.fireIndexedPropertyChange("elements", //$NON-NLS-1$
					index, oldElements, newElements);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public E get(int index) {
		return this.observedList.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return this.observedList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#set(int, java.lang.Object)
	 */
	@Override
	public E set(int index, E element) {
		final E oldElement = this.observedList.set(index, element);
		this.fireElementsChanged(index, new Object[] { oldElement },
				new Object[] { element });
		return oldElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, E element) {
		this.observedList.add(index, element);
		this.fireElementsChanged(index, IndexedCollection.EMPTY_ELEMENT_ARRAY,
				new Object[] { element });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public E remove(int index) {
		final E oldElement = this.observedList.remove(index);
		this.fireElementsChanged(index, new Object[] { oldElement },
				IndexedCollection.EMPTY_ELEMENT_ARRAY);
		return oldElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		return this.addAll(this.size(), collection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		if (this.observedList.addAll(index, collection)) {
			this
					.fireElementsChanged(index,
							IndexedCollection.EMPTY_ELEMENT_ARRAY, collection
									.toArray());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#clear()
	 */
	@Override
	public void clear() {
		final Object[] oldElements = this.observedList.toArray();
		this.observedList.clear();
		this.fireElementsChanged(0, oldElements,
				IndexedCollection.EMPTY_ELEMENT_ARRAY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return this.observedList.containsAll(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] array) {
		return this.observedList.toArray(array);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return this.observedList.toArray();
	}

}
