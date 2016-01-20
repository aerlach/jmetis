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
package org.jmetis.template;

import org.jmetis.template.compiler.SAXTemplateCompiler;
import org.jmetis.template.compiler.StAXTemplateCompiler;
import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.context.TemplateContext;
import org.jmetis.template.library.ITemplateHandlerLibrary;
import org.jmetis.template.library.TemplateHandlerLibrary;
import org.jmetis.template.model.ITemplateNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@code TemplateCompilerTest}
 * 
 * @author aerlach
 */
public class TemplateCompilerTest {

	/**
	 * Constructs a new {@code TemplateCompilerTest} instance.
	 */
	public TemplateCompilerTest() {
		super();
	}

	@Test
	public void staxIsBetterThanSAX() throws Exception {
		ITemplateContext templateContext = new TemplateContext(null);
		templateContext.setAttribute(ITemplateHandlerLibrary.class.getName(),
				new TemplateHandlerLibrary());
		String templateName = "file:///home/era/mic-rcp/at.mic.all.rcp/src/at/mic/all/rcp/configuration/codes/GeneralCodeGroupEditor.xpd";
		StAXTemplateCompiler stAXTemplateCompiler = new StAXTemplateCompiler();
		ITemplateNode staxResult = null;
		long stAXStarttime = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			staxResult = stAXTemplateCompiler.compileTemplateIn(templateName,
					templateContext);
		}

		long stAXEndtime = System.currentTimeMillis();

		SAXTemplateCompiler saxTemplateCompiler = new SAXTemplateCompiler();
		ITemplateNode saxResult = null;
		long saxStarttime = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			saxResult = saxTemplateCompiler.compileTemplateIn(templateName,
					templateContext);
		}

		long saxEndtime = System.currentTimeMillis();
		System.out.println("StAX Time = " + (stAXEndtime - stAXStarttime));
		System.out.println("SAX Time = " + (saxEndtime - saxStarttime));
		staxResult.performIn(templateContext);
		Assert.assertTrue(stAXEndtime - stAXStarttime < saxEndtime
				- saxStarttime);
		Assert.assertEquals(String.valueOf(staxResult), String
				.valueOf(saxResult));
	}

}
