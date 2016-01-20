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
package org.jmetis.kernel.comparator;

/**
 * {@code IPhoneticCodeGenerator} attempts to incorporate the rules of the
 * spoken language so that similar input words produce the same code. Since most
 * people misspell words based on similar sounds, this idea usually works well
 * for search applications. One of the oldest and simplest phonetic encoding
 * algorithms is the Russell Soundex algorithm.
 * 
 * @author era
 */
public interface IPhoneticCodeGenerator {

	String toEncodedValue(String value);

}
