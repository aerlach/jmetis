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
package org.jmetis.template.context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.el.FunctionMapper;

import org.jmetis.kernel.assertion.Assertions;

/**
 * {@code FunctionMapperDecorator} wraps another {@link FunctionMapper} with a
 * new context, represented by a {@link Map}. Modifications occur to the
 * {@link Map} instance, but resolve against the wrapped {@link FunctionMapper}
 * if the {@link Map} doesn't contain the {@link Method} requested.
 * 
 * @author aerlach
 */
class FunctionMapperDecorator extends FunctionMapper {

	private FunctionMapper parentFunctionMapper;

	private Map<String, Method> functions;

	/**
	 * Constructs a new {@code FunctionMapperDecorator} instance.
	 * 
	 * @param parentFunctionMapper
	 */
	FunctionMapperDecorator(FunctionMapper parentFunctionMapper) {
		super();
		this.parentFunctionMapper = Assertions.mustNotBeNull(
				"parentFunctionMapper", parentFunctionMapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.FunctionMapper#resolveFunction(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Method resolveFunction(String prefix, String localName) {
		Method function = null;
		if (this.functions != null) {
			function = this.functions.get(prefix + ':' + localName);
		}
		if (function == null) {
			function = this.parentFunctionMapper.resolveFunction(prefix,
					localName);
		}
		return function;
	}

	/**
	 * @param prefix
	 * @param localName
	 * @param method
	 */
	public void setFunction(String prefix, String localName, Method method) {
		if (this.functions == null) {
			this.functions = new HashMap<String, Method>();
		}
		this.functions.put(prefix + ':' + localName, method);
	}
}
