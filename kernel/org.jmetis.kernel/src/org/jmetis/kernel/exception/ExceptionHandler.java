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
package org.jmetis.kernel.exception;

import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedActionException;

/**
 * {@code ExceptionHandler}
 * 
 * @author aerlach
 */
public class ExceptionHandler implements IExceptionHandler {

	/**
	 * Constructs a new {@code ExceptionHandler} instance.
	 */
	public ExceptionHandler() {
		super();
	}

	/**
	 * Returns {@code true} if the given {@code exception} is either a
	 * {@link RuntimeException}, or an {@link Error}; otherwise {@code false}.
	 * 
	 * @param exception
	 *            the {@link Throwable} to test; must not be {@code null}
	 * @return {@code true} if the given {@code exception} is either a
	 *         {@link RuntimeException}, or an {@link Error}; otherwise {@code
	 *         false}
	 */
	protected boolean isUncheckedException(Throwable exception) {
		return exception instanceof RuntimeException
				|| exception instanceof Error;
	}

	/**
	 * Returns {@code true} if the given {@code exception} is neither a
	 * {@link RuntimeException}, nor an {@link Error}; otherwise {@code false}.
	 * 
	 * @param exception
	 *            the {@link Throwable} to test; must not be {@code null}
	 * @return {@code true} if the given {@code exception} is neither a
	 *         {@link RuntimeException}, nor an {@link Error}; otherwise {@code
	 *         false}
	 */
	protected boolean isCheckedException(Throwable exception) {
		return this.isUncheckedException(exception);
	}

	protected boolean isWrappedException(Throwable exception) {
		return exception instanceof InvocationTargetException
				|| exception instanceof PrivilegedActionException;
	}

	protected Throwable realCauseOf(Throwable exception) {
		Throwable realCause = exception.getCause();
		if (realCause != null) {
			return realCause;
		}
		return exception;
	}

	protected Throwable compatibleCause(Throwable exception,
			Class<? extends Throwable>... exceptionTypes) {
		if (this.isWrappedException(exception)) {
			exception = this.realCauseOf(exception);
		}
		if (this.isUncheckedException(exception)) {
			return exception;
		}
		for (Class<?> exceptionType : exceptionTypes) {
			if (exceptionType.isInstance(exception)) {
				return exception;
			}
		}
		return new RuntimeException(exception.getMessage(), exception);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.exception.IExceptionHandler#handleException(java.lang
	 * .Throwable)
	 */
	public void handleException(Throwable exception,
			Class<? extends Throwable>... exceptionTypes) {
		throw new RuntimeException(this.compatibleCause(exception,
				exceptionTypes));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jmetis.kernel.exception.IExceptionHandler#handleException(java.lang
	 * .Throwable, java.lang.Class)
	 */
	public <T> T handleException(Throwable exception, Class<T> resultType,
			Class<? extends Throwable>... exceptionTypes) {
		this.handleException(exception, exceptionTypes);
		return null;
	}

}
