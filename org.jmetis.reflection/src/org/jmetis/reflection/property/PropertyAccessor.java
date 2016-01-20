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
package org.jmetis.reflection.property;

import java.lang.reflect.Member;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.metadata.IPropertyAccessor;
import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code PropertyAccessor}
 * 
 * @author aerlach
 */
public abstract class PropertyAccessor implements IPropertyAccessor {

	private IPropertyDescription propertyDescriptor;

	/**
	 * Constructs a new {@code PropertyAccessor} instance.
	 */
	protected PropertyAccessor(IPropertyDescription propertyDescriptor) {
		super();
		this.propertyDescriptor = this.mustNotBeNull("propertyDescriptor",
				propertyDescriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Assertions#mustNotBeNull(String, Object)
	 */
	protected <T> T mustNotBeNull(String name, T value) {
		return Assertions.mustNotBeNull(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.metadata.IPropertyAccessor#getPropertyDescriptor()
	 */
	public IPropertyDescription propertyDescriptor() {
		return propertyDescriptor;
	}

	protected void accessPropertyFailedBecauseOf(Member member, Object object,
			Throwable exception) {
		throw new RuntimeException("Failed to access <"
				+ String.valueOf(object) + "> using <" + String.valueOf(member)
				+ ">", exception);
	}

	protected void modifyPropertyFailedBecauseOf(Member member, Object object,
			Object value, Throwable exception) {
		throw new RuntimeException("Failed to modify <" + String.valueOf(value)
				+ "> using <" + String.valueOf(member) + ">", exception);
	}

}
