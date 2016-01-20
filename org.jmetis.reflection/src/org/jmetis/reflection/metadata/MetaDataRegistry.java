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
package org.jmetis.reflection.metadata;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IMetaDataIntrospector;
import org.jmetis.kernel.metadata.IMetaDataRegistry;

/**
 * {@code MetaDataRegistry}
 * 
 * @author aerlach
 */
public class MetaDataRegistry implements IMetaDataRegistry {

	private IMetaDataIntrospector metaDataIntrospector;

	private Map<Class<?>, IClassDescription> classDescriptors;

	/**
	 * Constructs a new {@code MetaDataRegistry} instance.
	 * 
	 */
	public MetaDataRegistry() {
		super();
		classDescriptors = Collections
				.synchronizedMap(new WeakHashMap<Class<?>, IClassDescription>());
	}

	protected IMetaDataIntrospector createMetaDataIntrospector() {
		return new MetaDataIntrospector(this);
	}

	protected IMetaDataIntrospector metaDataIntrospector() {
		if (metaDataIntrospector == null) {
			metaDataIntrospector = createMetaDataIntrospector();
		}
		return metaDataIntrospector;
	}

	protected IClassDescription createClassDescriptor(Class<?> typeToDescribe) {
		return new ClassDescriptor(metaDataIntrospector(), typeToDescribe);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.reflection.IMetaDataRegistry#classDescriptorOf(java.lang.Class
	 * )
	 */
	public IClassDescription classDescriptorOf(Class<?> typeToDescribe) {
		IClassDescription classDescriptor = classDescriptors.get(typeToDescribe);
		if (classDescriptor == null) {
			classDescriptor = createClassDescriptor(typeToDescribe);
			classDescriptors.put(typeToDescribe, classDescriptor);
		}
		return classDescriptor;
	}

}
