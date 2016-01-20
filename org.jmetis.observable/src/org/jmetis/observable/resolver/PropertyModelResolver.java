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
package org.jmetis.observable.resolver;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.el.ELContext;
import javax.el.ELResolver;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.observable.IPropertyModel;
import org.jmetis.observable.object.BeanAdapter;

/**
 * {@code PropertyModelResolver} defines property resolution behavior on objects
 * using the JavaBeans component architecture.
 * 
 * @author aerlach
 */
public class PropertyModelResolver extends ELResolver {

	/**
	 * Constructs a new {@code PropertyModelResolver} instance.
	 */
	public PropertyModelResolver() {
		super();
	}

	protected void assertIsValidContext(ELContext context) {
		Assertions.mustNotBeNull("context", context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext,
	 * java.lang.Object)
	 */
	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		this.assertIsValidContext(context);
		Class<?> commonPropertyType = null;
		if (base instanceof IPropertyModel) {
			commonPropertyType = Object.class;
			context.setPropertyResolved(true);
		}
		return commonPropertyType;
	}

	protected IPropertyModel getPropertyModel(ELContext context, Object base,
			Object property) {
		IPropertyModel baseModel;
		if (base instanceof IPropertyModel) {
			baseModel = (IPropertyModel) base;
		} else {
			IClassDescription classDescriptor = null;
			baseModel = new BeanAdapter(classDescriptor);
			baseModel.setValue(base);
		}
		return baseModel.getPropertyNamed(property.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getType(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		this.assertIsValidContext(context);
		if (base != null && property != null) {
			IPropertyModel propertyModel = this.getPropertyModel(context, base,
					property);
			context.setPropertyResolved(true);
			return propertyModel.getValueType();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext,
	 * java.lang.Object)
	 */
	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {
		if (base != null) {
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(base.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo
						.getPropertyDescriptors();
				List<FeatureDescriptor> featureDescriptors = new ArrayList<FeatureDescriptor>(
						propertyDescriptors.length);
				for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
					propertyDescriptor.setValue("type", propertyDescriptor
							.getPropertyType());
					propertyDescriptor.setValue("resolvableAtDesignTime",
							Boolean.TRUE);
					featureDescriptors.add(propertyDescriptor);
				}
				return featureDescriptors.iterator();
			} catch (Exception ex) {
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getValue(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		this.assertIsValidContext(context);
		if (base != null && property != null) {
			IPropertyModel propertyModel = this.getPropertyModel(context, base,
					property);
			context.setPropertyResolved(true);
			return propertyModel;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		this.assertIsValidContext(context);
		if (base != null && property != null) {
			IPropertyModel propertyModel = this.getPropertyModel(context, base,
					property);
			context.setPropertyResolved(true);
			return propertyModel.isReadOnly();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#setValue(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setValue(ELContext context, Object base, Object property,
			Object value) {
		this.assertIsValidContext(context);
		if (base != null && property != null) {
			IPropertyModel propertyModel = this.getPropertyModel(context, base,
					property);
			context.setPropertyResolved(true);
			propertyModel.setValue(value);
		}
	}

}
