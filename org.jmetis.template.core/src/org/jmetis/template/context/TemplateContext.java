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

import java.util.HashMap;
import java.util.Map;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

import org.jmetis.kernel.assertion.Assertions;
import org.jmetis.kernel.context.IEvaluationContext;
import org.jmetis.template.handler.ITemplateHandler;
import org.jmetis.template.library.ITemplateHandlerLibrary;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code TemplateContext}
 * 
 * @author aerlach
 */
public class TemplateContext extends ELContext implements ITemplateContext {

	private IEvaluationContext parentContext;

	private Object ownerToken;

	private Map<String, Object> attributes;

	private ExpressionFactory expressionFactory;

	private ELResolver elResolver;

	private FunctionMapper functionMapper;

	private VariableMapper variableMapper;

	/**
	 * Constructs a new {@code TemplateContext} instance.
	 * 
	 * @param parentContext
	 */
	public TemplateContext(IEvaluationContext parentContext) {
		super();
		this.parentContext = Assertions.mustNotBeNull("parentContext",
				parentContext);
		this.ownerToken = new Object();
		this.expressionFactory = this.createExpressionFactory();
		this.elResolver = this.createELResolver();
		this.functionMapper = this.createFunctionMapper(null);
		this.variableMapper = this.createVariableMapper(this
				.createVariableMapper(null));
	}

	/**
	 * Constructs a new {@code TemplateContext} instance.
	 */
	protected TemplateContext(TemplateContext parentContext, Object ownerToken) {
		super();
		this.parentContext = Assertions.mustNotBeNull("parentContext",
				parentContext);
		this.ownerToken = Assertions.mustNotBeNull("ownerToken", ownerToken);
		this.expressionFactory = parentContext.getExpressionFactory();
		this.elResolver = parentContext.getELResolver();
		this.functionMapper = this.createFunctionMapper(parentContext
				.getFunctionMapper());
		this.variableMapper = this.createVariableMapper(parentContext
				.getVariableMapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateContext#getELContext()
	 */
	public ELContext getELContext() {
		return this;
	}

	protected void addELResolversTo(CompositeELResolver compositeELResolver) {

	}

	protected ELResolver createELResolver() {
		CompositeELResolver elResolver = new CompositeELResolver();
		this.addELResolversTo(elResolver);
		return elResolver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELContext#getELResolver()
	 */
	@Override
	public ELResolver getELResolver() {
		return this.elResolver;
	}

	protected FunctionMapper createFunctionMapper(
			FunctionMapper parentFunctionMapper) {
		return new FunctionMapperDecorator(parentFunctionMapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELContext#getFunctionMapper()
	 */
	@Override
	public FunctionMapper getFunctionMapper() {
		return this.functionMapper;
	}

	protected VariableMapper createVariableMapper(
			VariableMapper parentVariableMapper) {
		return new VariableMapperAdapter(this, parentVariableMapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELContext#getVariableMapper()
	 */
	@Override
	public VariableMapper getVariableMapper() {
		return this.variableMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.context.IEvaluationContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		Object value = this.attributes.get(name);
		if (value != null) {
			return value;
		}
		if (this.parentContext != null) {
			return this.parentContext.getAttribute(name);
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.context.IEvaluationContext#getAttribute(java.lang.String,
	 * java.lang.Class)
	 */
	public <T> T getAttribute(String name, Class<T> expectedType) {
		return expectedType.cast(this.getAttribute(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.context.IEvaluationContext#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Object>();
		}
		this.attributes.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.IServiceRegistry#acquireService(java.lang.Class,
	 * java.lang.String)
	 */
	public <T> T acquireService(Class<T> serviceType, String searchFilter) {
		return this.parentContext.acquireService(serviceType, searchFilter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.service.core.IServiceRegistry#acquireService(java.lang.Class)
	 */
	public <T> T acquireService(Class<T> serviceType) {
		return this.parentContext.acquireService(serviceType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.service.core.IServiceRegistry#releaseService(java.lang.Object)
	 */
	public void releaseService(Object service) {
		this.parentContext.releaseService(service);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateContext#enterChildContext(java.lang
	 * .Object)
	 */
	public ITemplateContext enterChildContext(Object ownerToken) {
		return new TemplateContext(this, ownerToken);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateContext#exitChildContext(java.lang
	 * .Object)
	 */
	public ITemplateContext exitChildContext(Object ownerToken) {
		if (this.ownerToken != ownerToken) {
			// TODO provide error message
			throw new IllegalStateException();
		}
		return (ITemplateContext) this.parentContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateContext#getLocalizedString(java.lang
	 * .String)
	 */
	public String getLocalizedString(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	protected ITemplateHandlerLibrary getTemplateHandlerLibrary() {
		return this.getAttribute(ITemplateHandlerLibrary.class.getName(),
				ITemplateHandlerLibrary.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.context.ITemplateContext#getTemplateHandler(org.jmetis
	 * .template.core.ITemplateNode)
	 */
	public ITemplateHandler getTemplateHandler(ITemplateNode templateNode) {
		return this.getTemplateHandlerLibrary().createTemplateHandler(
				templateNode.getNamespace(), templateNode.getLocalName());
	}

	protected ExpressionFactory createExpressionFactory() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jmetis.template.context.ITemplateContext#getExpressionFactory()
	 */
	public ExpressionFactory getExpressionFactory() {
		return this.expressionFactory;
	}

}
