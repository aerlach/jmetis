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
package org.jmetis.template.compiler;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code CachingTemplateCompiler} is a {@link ITemplateCompiler} that supports
 * caching.
 * 
 * @author aerlach
 */
public class CachingTemplateCompiler extends TemplateCompilerDecorator {

	private Map<String, CachedTemplate> templateCache;

	public CachingTemplateCompiler(ITemplateCompiler templateCompiler) {
		super(templateCompiler);
		this.templateCache = this.createTemplateCache();
	}

	protected Map<String, CachedTemplate> createTemplateCache() {
		return new ConcurrentHashMap<String, CachedTemplate>();
	}

	protected CachedTemplate createCompiledTemplate(String templateName,
			ITemplateContext templateContext) {
		return new CachedTemplate(this.templateCompiler, templateName,
				templateContext);
	}

	protected File resolveTemplateFile(String templateName) {
		return new File(templateName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.template.compiler.TemplateCompilerDecorator#compileTemplateIn
	 * (java.lang.String, org.jmetis.template.context.ITemplateContext)
	 */
	@Override
	public ITemplateNode compileTemplateIn(String templateName,
			ITemplateContext templateContext) {
		CachedTemplate cachedTemplate = this.templateCache.get(templateName);
		if (cachedTemplate == null) {
			cachedTemplate = this.createCompiledTemplate(templateName,
					templateContext);
			this.templateCache.put(templateName, cachedTemplate);
		} else {
			File templateFile = this.resolveTemplateFile(templateName);
			if (cachedTemplate.getCreationTime() < templateFile.lastModified()) {
				cachedTemplate = this.createCompiledTemplate(templateName,
						templateContext);
				this.templateCache.put(templateName, cachedTemplate);
			}
		}
		return cachedTemplate;
	}

}
