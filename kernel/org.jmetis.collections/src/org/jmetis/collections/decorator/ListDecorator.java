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
import java.util.List;
import java.util.ListIterator;

/**
 * {@code ListDecorator} decorates another {@link List} to provide additional
 * behavior.
 * 
 * @author era
 */
public class ListDecorator<E> extends CollectionDecorator<List<E>, E> implements
		List<E> {

	/**
	 * Constructs a new {@code ListDecorator} instance that wraps (not copies)
	 * the given {@code component}.
	 * 
	 * @param component
	 *            the {@link List} to decorate, must not be {@code null}
	 * @throws NullPointerException
	 *             if the given {@code component} is {@code null}
	 */
	public ListDecorator(List<E> component) {
		super(component);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, E element) {
		this.component.add(index, element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends E> collection) {
		return this.component.addAll(index, collection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#get(int)
	 */
	public E get(int index) {
		return this.component.get(index);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object object) {
		return this.component.indexOf(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object object) {
		return this.component.lastIndexOf(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#listIterator()
	 */
	public ListIterator<E> listIterator() {
		return this.component.listIterator();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator<E> listIterator(int index) {
		return this.component.listIterator(index);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#remove(int)
	 */
	public E remove(int index) {
		return this.component.remove(index);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public E set(int index, E element) {
		return this.component.set(index, element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.List#subList(int, int)
	 */
	public List<E> subList(int fromIndex, int toIndex) {
		return this.component.subList(fromIndex, toIndex);
	}

}
