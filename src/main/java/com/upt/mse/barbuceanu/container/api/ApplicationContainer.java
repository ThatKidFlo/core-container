package com.upt.mse.barbuceanu.container.api;

import java.util.Map;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 12/11/2017
 */
public interface ApplicationContainer {

    <T> T getBean(String name);

    <T> T getBean(Class<? extends T> clazz);

    Map<Class<?>, Object> getBeans();

    <T> ApplicationContainer addBeanDefinition(Class<T> clazz);
}
