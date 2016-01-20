/*
 * Copyright (c) 2005  Tall Software Ltd
 *
 * DISCLAIMER OF WARRANTY. Software is provided "AS IS," without a
 * warranty of any kind. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, CONDITIONS OR OTHER TERMS, INCLUDING ANY IMPLIED TERM
 * OF SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
package com.tallsoft.microspring;

/**
 * <p>
 * Copy of Spring Bean Factory interface
 * </p>
 * 
 * @author J.Gibbons
 */
public interface BeanFactory {
    /**
     * Does this bean factory contain a bean definition with the given name?
     * 
     * @param name
     * @return
     */
    boolean containsBean(String name);

    /**
     * Return the aliases for the given bean name, if defined.
     * 
     * @param name
     * @return
     */
    String[] getAliases(String name) throws NoSuchBeanDefinitionException;

    /**
     * Return an instance, which may be shared or independent, of the given bean
     * name.
     * 
     * @param name
     * @return
     */
    Object getBean(String name) throws BeansException;

    /**
     * Return an instance (possibly shared or independent) of the given bean
     * name.
     * 
     * @param name
     * @param requiredType
     * @return
     */
    Object getBean(String name, Class requiredType) throws BeansException;

    /**
     * Determine the type of the bean with the given name.
     * 
     * @param name
     * @return
     */
    Class getType(String name) throws NoSuchBeanDefinitionException;

    /**
     * Is this bean a singleton?
     * 
     * @param name
     * @return
     */
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

}
