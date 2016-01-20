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
package org.jmetis.conversion;

/**
 * {@code ITypeConverter} is a data type converter that can be registered and
 * used to manage the conversion of objects from one type to another.
 * 
 * @author aerlach
 */
public interface ITypeConverter {

	/**
	 * Convert the specified input {@code value} into an output value of the
	 * specified {@code type}.
	 * 
	 * @param sourceValue
	 *            the input value to be converted
	 * @param targetType
	 *            the type to which the input value should be converted
	 * @throws ConversionException
	 *             if the conversion cannot be performed successfully
	 */
	<T> T convertTo(Object sourceValue, Class<T> targetType);

}
