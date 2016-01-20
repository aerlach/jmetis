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
package org.jmetis.messaging.core;

/**
 * {@code MessageException}
 * 
 * @author aerlach
 */
public class MessageException extends RuntimeException {

	private static final long serialVersionUID = 4084721680958356153L;

	/**
	 * Constructs a new {@code MessageException} instance.
	 */
	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public static void propertyNameNullOrEmpty() {
		// TODO
	}

	public static void readPropertyFailed(String name, Exception exception) {
		// TODO
	}

	public static void cannotReadProperty(String name) {
		// TODO
	}

	public static void propertyNotFound(String name) {
		// TODO
	}

	public static void writePropertyFailed(String name, Exception exception) {
		// TODO
	}

	public static void cannotWriteProperty(String name) {
		// TODO
	}

	public static void channelNotAvailable(String topic) {
		// TODO
	}

}