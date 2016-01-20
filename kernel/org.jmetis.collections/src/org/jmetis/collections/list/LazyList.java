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
package org.jmetis.collections.list;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jmetis.collections.decorator.ListDecorator;
import org.jmetis.kernel.assertion.Assertions;

/**
 * 
 * @author era
 */
public class LazyList<P extends IProvisioner<E>, E> extends ListDecorator<E> {

	protected Set<E> addedElements;

	protected Set<Object> removedElements;

	protected final P provisioner;

	/**
	 * @param component
	 */
	public LazyList(List<E> component, P provisioner) {
		super(component);
		this.provisioner = Assertions.mustNotBeNull("provisioner", //$NON-NLS-1$
				provisioner);
	}

	protected void elementAdded(E element) {
		if (this.removedElements != Collections.EMPTY_SET
				&& this.removedElements.remove(element)) {
			if (this.removedElements.size() == 0) {
				this.removedElements = Collections.emptySet();
			}
		} else {
			if (this.addedElements == Collections.EMPTY_SET) {
				this.addedElements = new HashSet<E>();
			}
			this.addedElements.add(element);
		}
	}

	protected void elementRemoved(Object element) {
		if (this.addedElements != Collections.EMPTY_SET
				&& this.addedElements.remove(element)) {
			if (this.addedElements.size() == 0) {
				this.addedElements = Collections.emptySet();
			}
		} else {
			if (this.removedElements == Collections.EMPTY_SET) {
				this.removedElements = new HashSet<Object>();
			}
			this.removedElements.add(element);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.CollectionDecorator#add(java.lang.Object
	 *      )
	 */
	@Override
	public boolean add(E element) {
		if (super.add(element)) {
			this.elementAdded(element);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.CollectionDecorator#addAll(java.util
	 *      .Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean modified = false;
		for (E element : collection) {
			modified |= this.add(element);
		}
		return modified;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.ListDecorator#add(int,
	 *      java.lang.Object)
	 */
	@Override
	public void add(int index, E element) {
		super.add(index, element);
		this.elementAdded(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.ListDecorator#addAll(int,
	 *      java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		boolean modified = false;
		for (E element : collection) {
			this.add(index++, element);
			modified = true;
		}
		return modified;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.CollectionDecorator#remove(java.lang
	 *      .Object)
	 */
	@Override
	public boolean remove(Object element) {
		if (super.remove(element)) {
			this.elementRemoved(element);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.CollectionDecorator#removeAll(java.
	 *      util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean modified = false;
		for (Object element : collection) {
			modified |= this.remove(element);
		}
		return modified;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.ListDecorator#remove(int)
	 */
	@Override
	public E remove(int index) {
		E element = super.remove(index);
		this.elementRemoved(element);
		return element;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.CollectionDecorator#retainAll(java.
	 *      util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> collection) {
		boolean modified = false;
		for (Iterator<E> it = this.component.iterator(); it.hasNext();) {
			if (!collection.contains(it.next())) {
				it.remove();
				modified = true;
			}
		}
		return modified;
	}

	/**
	 * Fill the indexes in-between the {@code fromIndex} and the {@code toIndex}
	 * with a place-holder.
	 * 
	 * @param fromIndex
	 *            low end-point (inclusive) of the range.
	 * @param toIndex
	 *            high end-point (exclusive) of the range.
	 */
	protected void fillGap(int fromIndex, int toIndex) {
		for (int i = fromIndex; i < toIndex; i++) {
			this.component.add(null);
		}
	}

	/**
	 * Decorate the get method to perform the lazy behavior.
	 * <p>
	 * If the requested index is greater than the current size, the list will
	 * grow to the new size and a new object will be returned from the factory.
	 * Indexes in-between the old size and the requested size are left with a
	 * place-holder that is replaced with a factory object when requested.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.ListDecorator#get(int)
	 */
	@Override
	public E get(int index) {
		E element;
		int size = this.component.size();
		if (index < size) {
			element = super.get(index);
		} else {
			this.fillGap(size, index);
			element = null;
		}
		if (element == null) {
			element = this.provisioner.evaluate(index
					- this.addedElements.size() + this.removedElements.size());
			this.component.add(element);
		}
		return element;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jmetis.collections.decorator.CollectionDecorator#size()
	 */
	@Override
	public int size() {
		return this.provisioner.size() + this.addedElements.size()
				- this.removedElements.size();
	}

}
