/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.dependencymanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * Service dependency that can track an OSGi service.
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public class ServiceDependency implements Dependency, ServiceTrackerCustomizer, ServiceComponentDependency {
    private boolean m_isRequired;
    private Service m_service;
    private ServiceTracker m_tracker;
    private BundleContext m_context;
    private boolean m_isAvailable;
    private Class m_trackedServiceName;
    private Object m_nullObject;
    private String m_trackedServiceFilter;
    private ServiceReference m_trackedServiceReference;
    private boolean m_isStarted;
    private Object m_callbackInstance;
    private String m_callbackAdded;
    private String m_callbackChanged;
    private String m_callbackRemoved;
    private boolean m_autoConfig;
    private ServiceReference m_reference;
    private Object m_serviceInstance;
    private final Logger m_logger;
    private String m_autoConfigInstance;
    private Object m_defaultImplementation;
    private Object m_defaultImplementationInstance;
    
    /**
     * Creates a new service dependency.
     * 
     * @param context the bundle context
     * @param logger 
     */
    public ServiceDependency(BundleContext context, Logger logger) {
        m_context = context;
        m_logger = logger;
        m_autoConfig = true;
    }

    public synchronized boolean isRequired() {
        return m_isRequired;
    }

    public synchronized boolean isAvailable() {
        return m_isAvailable;
    }
    
    public synchronized boolean isAutoConfig() {
        return m_autoConfig;
    }

    public synchronized Object getService() {
        Object service = null;
        if (m_isStarted) {
            service = m_tracker.getService();
        }
        if (service == null) {
            service = getDefaultImplementation();
            if (service == null) {
                service = getNullObject();
            }
        }
        return service;
    }

    private Object getNullObject() {
        if (m_nullObject == null) {
            Class trackedServiceName;
            synchronized (this) {
                trackedServiceName = m_trackedServiceName;
            }
            try {
                m_nullObject = Proxy.newProxyInstance(trackedServiceName.getClassLoader(), new Class[] {trackedServiceName}, new DefaultNullObject()); 
            }
            catch (Exception e) {
                m_logger.log(Logger.LOG_ERROR, "Could not create null object for " + trackedServiceName + ".", e);
            }
        }
        return m_nullObject;
    }
    
    private Object getDefaultImplementation() {
        if (m_defaultImplementation != null) {
            if (m_defaultImplementation instanceof Class) {
                try {
                    m_defaultImplementationInstance = ((Class) m_defaultImplementation).newInstance();
                }
                catch (Exception e) {
                    m_logger.log(Logger.LOG_ERROR, "Could not create default implementation instance of class " + m_defaultImplementation + ".", e);
                }
            }
            else {
                m_defaultImplementationInstance = m_defaultImplementation;
            }
        }
        return m_defaultImplementationInstance;
    }
    
    public synchronized Class getInterface() {
        return m_trackedServiceName;
    }

    public void start(Service service) {
        synchronized (this) {
            if (m_isStarted) {
                throw new IllegalStateException("Service dependency was already started." + m_trackedServiceName);
            }
            m_service = service;
            if (m_trackedServiceName != null) {
                if (m_trackedServiceFilter != null) {
                    try {
                        m_tracker = new ServiceTracker(m_context, m_context.createFilter(m_trackedServiceFilter), this);
                    }
                    catch (InvalidSyntaxException e) {
                        throw new IllegalStateException("Invalid filter definition for dependency.");
                    }
                }
                else if (m_trackedServiceReference != null) {
                    m_tracker = new ServiceTracker(m_context, m_trackedServiceReference, this);
                }
                else {
                    m_tracker = new ServiceTracker(m_context, m_trackedServiceName.getName(), this);
                }
            }
            else {
                throw new IllegalStateException("Could not create tracker for dependency, no service name specified.");
            }
            m_isStarted = true;
        }
        m_tracker.open();
    }

    public void stop(Service service) {
        synchronized (this) {
            if (!m_isStarted) {
                throw new IllegalStateException("Service dependency was not started.");
            }
            m_isStarted = false;
        }
        m_tracker.close();
        m_tracker = null;
    }

    public Object addingService(ServiceReference ref) {
        Object service = m_context.getService(ref);
        // we remember these for future reference, needed for required service callbacks
        m_reference = ref;
        m_serviceInstance = service;
        return service;
    }

    public void addedService(ServiceReference ref, Object service) {
        if (makeAvailable()) {
            m_service.dependencyAvailable(this);
        }
        else {
            m_service.dependencyChanged(this);
        }
        // try to invoke callback, if specified, but only for optional dependencies
        // because callbacks for required dependencies are handled differently
        if (!isRequired()) {
            invokeAdded(ref, service);
        }
    }

    public void invokeAdded() {
        invokeAdded(m_reference, m_serviceInstance);
    }
    
    public void invokeAdded(ServiceReference reference, Object serviceInstance) {
        Object callbackInstance = getCallbackInstance();
        if ((callbackInstance != null) && (m_callbackAdded != null)) {
            try {
                invokeCallbackMethod(callbackInstance, m_callbackAdded, reference, serviceInstance);
            }
            catch (NoSuchMethodException e) {
                m_logger.log(Logger.LOG_ERROR, "Could not invoke method " + m_callbackAdded + " on " + callbackInstance + ".", e);
            }
        }
    }

    public void modifiedService(ServiceReference ref, Object service) {
        m_reference = ref;
        m_serviceInstance = service;
        m_service.dependencyChanged(this);
        // only invoke the changed callback if the service itself is "active"
        if (((ServiceImpl) m_service).isRegistered()) {
            invokeChanged(ref, service);
        }
    }

    public void invokeChanged(ServiceReference reference, Object serviceInstance) {
        Object callbackInstance = getCallbackInstance();
        if ((callbackInstance != null) && (m_callbackChanged != null)) {
            try {
                if (m_reference == null) {
                    Thread.dumpStack();
                }
                invokeCallbackMethod(callbackInstance, m_callbackChanged, reference, serviceInstance);
            }
            catch (NoSuchMethodException e) {
                m_logger.log(Logger.LOG_ERROR, "Could not invoke method " + m_callbackChanged + " on " + callbackInstance + ".", e);
            }
        }
    }

    public void removedService(ServiceReference ref, Object service) {
        if (makeUnavailable()) {
            m_service.dependencyUnavailable(this);
        }
        // try to invoke callback, if specified, but only for optional dependencies
        // because callbacks for required dependencies are handled differently
        if (!isRequired()) {
            invokeRemoved(ref, service);
        }
        
        // unget what we got in addingService (see ServiceTracker 701.4.1)
        m_context.ungetService(ref);
    }

    public void invokeRemoved() {
        invokeRemoved(m_reference, m_serviceInstance);
    }
    
    public void invokeRemoved(ServiceReference reference, Object serviceInstance) {
        Object callbackInstance = getCallbackInstance();
        if ((callbackInstance != null) && (m_callbackRemoved != null)) {
            try {
                if (m_reference == null) {
                    Thread.dumpStack();
                }
                invokeCallbackMethod(callbackInstance, m_callbackRemoved, reference, serviceInstance);
            }
            catch (NoSuchMethodException e) {
                m_logger.log(Logger.LOG_ERROR, "Could not invoke method " + m_callbackRemoved + " on " + callbackInstance + ".", e);
            }
        }
    }
    
    private synchronized boolean makeAvailable() {
        if (!m_isAvailable) {
            m_isAvailable = true;
            return true;
        }
        return false;
    }
    
    private synchronized boolean makeUnavailable() {
        if ((m_isAvailable) && (m_tracker.getServiceReference() == null)) {
            m_isAvailable = false;
            return true;
        }
        return false;
    }
    
    private synchronized Object getCallbackInstance() {
        Object callbackInstance = m_callbackInstance;
        if (callbackInstance == null) {
            callbackInstance = m_service.getService();
        }
        return callbackInstance;
    }
    
    private void invokeCallbackMethod(Object instance, String methodName, ServiceReference reference, Object service) throws NoSuchMethodException {
        Class currentClazz = instance.getClass();
        boolean done = false;
        while (!done && currentClazz != null) {
            Class trackedServiceName;
            synchronized (this) {
                trackedServiceName = m_trackedServiceName;
            }
            done = invokeMethod(instance, currentClazz, methodName,
                new Class[][] {{ServiceReference.class, trackedServiceName}, {ServiceReference.class, Object.class}, {ServiceReference.class}, {trackedServiceName}, {Object.class}, {}},
                new Object[][] {{reference, service}, {reference, service}, {reference}, {service}, {service}, {}},
                false);
            if (!done) {
                currentClazz = currentClazz.getSuperclass();
            }
        }
        if (!done && currentClazz == null) {
            throw new NoSuchMethodException(methodName);
        }
    }

    private boolean invokeMethod(Object object, Class clazz, String name, Class[][] signatures, Object[][] parameters, boolean isSuper) {
        Method m = null;
        for (int i = 0; i < signatures.length; i++) {
            Class[] signature = signatures[i];
            try {
                m = clazz.getDeclaredMethod(name, signature);
                if (!(isSuper && Modifier.isPrivate(m.getModifiers()))) {
                    m.setAccessible(true);
                    try {
                        m.invoke(object, parameters[i]);
                    }
                    catch (InvocationTargetException e) {
                        m_logger.log(Logger.LOG_ERROR, "Exception while invoking method " + m + ".", e);
                    }
                    // we did find and invoke the method, so we return true
                    return true;
                }
            }
            catch (NoSuchMethodException e) {
                // ignore this and keep looking
            }
            catch (Exception e) {
                // could not even try to invoke the method
                m_logger.log(Logger.LOG_ERROR, "Exception while trying to invoke method " + m + ".", e);
            }
        }
        return false;
    }
    
    // ----- CREATION

    /**
     * Sets the name of the service that should be tracked. 
     * 
     * @param serviceName the name of the service
     * @return this service dependency
     */
    public synchronized ServiceDependency setService(Class serviceName) {
        ensureNotActive();
        if (serviceName == null) {
            throw new IllegalArgumentException("Service name cannot be null.");
        }
        m_trackedServiceName = serviceName;
        m_trackedServiceReference = null;
        m_trackedServiceFilter = null;
        return this;
    }
    
    /**
     * Sets the name of the service that should be tracked. You can either specify
     * only the name, or the name and a filter. In the latter case, the filter is used
     * to track the service and should only return services of the type that was specified
     * in the name.
     * 
     * @param serviceName the name of the service
     * @param serviceFilter the filter condition
     * @return this service dependency
     */
    public synchronized ServiceDependency setService(Class serviceName, String serviceFilter) {
        ensureNotActive();
        if (serviceName == null) {
            throw new IllegalArgumentException("Service name cannot be null.");
        }
        m_trackedServiceName = serviceName;
        m_trackedServiceFilter = serviceFilter;
        m_trackedServiceReference = null;
        return this;
    }

    /**
     * Sets the name of the service that should be tracked. You can either specify
     * only the name, or the name and a reference. In the latter case, the service reference
     * is used to track the service and should only return services of the type that was 
     * specified in the name.
     * 
     * @param serviceName the name of the service
     * @param serviceReference the service reference to track
     * @return this service dependency
     */
    public synchronized ServiceDependency setService(Class serviceName, ServiceReference serviceReference) {
        ensureNotActive();
        if (serviceName == null) {
            throw new IllegalArgumentException("Service name cannot be null.");
        }
        m_trackedServiceName = serviceName;
        m_trackedServiceReference = serviceReference;
        m_trackedServiceFilter = null;
        return this;
    }
    
    /**
     * Sets the default implementation for this service dependency. You can use this to supply
     * your own implementation that will be used instead of a Null Object when the dependency is
     * not available. This is also convenient if the service dependency is not an interface
     * (which would cause the Null Object creation to fail) but a class.
     * 
     * @param implementation the instance to use or the class to instantiate if you want to lazily
     *     instantiate this implementation
     * @return this service dependency
     */
    public synchronized ServiceDependency setDefaultImplementation(Object implementation) {
        ensureNotActive();
        m_defaultImplementation = implementation;
        return this;
    }

    /**
     * Sets the required flag which determines if this service is required or not.
     * 
     * @param required the required flag
     * @return this service dependency
     */
    public synchronized ServiceDependency setRequired(boolean required) {
        ensureNotActive();
        m_isRequired = required;
        return this;
    }

    /**
     * Sets auto configuration for this service. Auto configuration allows the
     * dependency to fill in any attributes in the service implementation that
     * are of the same type as this dependency. Default is on.
     * 
     * @param autoConfig the value of auto config
     * @return this service dependency
     */
    public synchronized ServiceDependency setAutoConfig(boolean autoConfig) {
        ensureNotActive();
        m_autoConfig = autoConfig;
        return this;
    }
    
    /**
     * Sets auto configuration for this service. Auto configuration allows the
     * dependency to fill in the attribute in the service implementation that
     * has the same type and instance name.
     * 
     * @param instanceName the name of attribute to auto config
     * @return this service dependency
     */
    public synchronized ServiceDependency setAutoConfig(String instanceName) {
        ensureNotActive();
        m_autoConfig = (instanceName != null);
        m_autoConfigInstance = instanceName;
        return this;
    }
    
    /**
     * Sets the callbacks for this service. These callbacks can be used as hooks whenever
     * a dependency is added or removed. They are called on the service implementation.
     * 
     * @param added the method to call when a service was added
     * @param removed the method to call when a service was removed
     * @return this service dependency
     */
    public synchronized ServiceDependency setCallbacks(String added, String removed) {
        return setCallbacks(null, added, null, removed);
    }
    public synchronized ServiceDependency setCallbacks(String added, String changed, String removed) {
        return setCallbacks(null, added, changed, removed);
    }
    public synchronized ServiceDependency setCallbacks(Object instance, String added, String removed) {
        return setCallbacks(instance, added, null, removed);
    }
    
    /**
     * Sets the callbacks for this service. These callbacks can be used as hooks whenever
     * a dependency is added or removed. They are called on the instance you provide.
     * 
     * @param instance the instance to call the callbacks on
     * @param added the method to call when a service was added
     * @param changed the method to call when a service was changed
     * @param removed the method to call when a service was removed
     * @return this service dependency
     */
    public synchronized ServiceDependency setCallbacks(Object instance, String added, String changed, String removed) {
        ensureNotActive();
        m_callbackInstance = instance;
        m_callbackAdded = added;
        m_callbackChanged = changed;
        m_callbackRemoved = removed;
        return this;
    }
    
    
    private void ensureNotActive() {
        if (m_tracker != null) {
            throw new IllegalStateException("Cannot modify state while active.");
        }
    }
    
    public synchronized String toString() {
        return "ServiceDependency[" + m_trackedServiceName + " " + m_trackedServiceFilter + "]";
    }

    public String getAutoConfigName() {
        return m_autoConfigInstance;
    }

    public String getName() {
        return m_trackedServiceName.getName();
    }

    public int getState() {
        return (isAvailable() ? 1 : 0) + (isRequired() ? 2 : 0);
    }

    public String getType() {
        return "service";
    }
}
