package org.jmetis.kernel.factory;

/**
 * {@code IObjectInitializer} is used to provide customized initialization for
 * more complex components.
 * 
 * @author era
 */
public interface IObjectInitializer<T> {

	void initialise(T instance);

}
