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
import java.util.Iterator;

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code CollectionDecorator} decorates another {@link Collection} to provide
 * additional behavior.
 * 
 * @author era
 */
public class CollectionDecorator<C extends Collection<E>, E> implements
		Collection<E> {

	protected C component;

	/**
	 * Constructs a new {@code CollectionDecorator} instance that wraps (not
	 * copies) the given {@code component}.
	 * 
	 * @param component
	 *            the {@link Collection} to decorate, must not be {@code null}
	 * @throws NullPointerException
	 *             if the given {@code component} is {@code null}
	 */
	public CollectionDecorator(C component) {
		super();
		this.component = Assertions.mustNotBeNull("component", //$NON-NLS-1$
				component);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(E element) {
		return this.component.add(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends E> collection) {
		return this.component.addAll(collection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		this.component.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object element) {
		return this.component.contains(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> collection) {
		return this.component.containsAll(collection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return this.component.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#iterator()
	 */
	public Iterator<E> iterator() {
		return this.component.iterator();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object element) {
		return this.component.remove(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> collection) {
		return this.component.removeAll(collection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> collection) {
		return this.component.retainAll(collection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#size()
	 */
	public int size() {
		return this.component.size();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		return this.component.toArray();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Collection#toArray(T[])
	 */
	public <T> T[] toArray(T[] array) {
		return this.component.toArray(array);
	}

}
