package com.upt.mse.barbuceanu.container.impl;

import com.upt.mse.barbuceanu.container.ContainerUtils;
import com.upt.mse.barbuceanu.container.api.ApplicationContainer;
import com.upt.mse.barbuceanu.container.exception.UnsatisfiedDependencyException;
import io.vavr.control.Try;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 12/11/2017
 */
public class DefaultApplicationContainer implements ApplicationContainer {

    private final Map<Class<?>, Object> container;

    private DefaultApplicationContainer() {
        container = new HashMap<>();
    }

    private DefaultApplicationContainer(Map<Class<?>, Object> container) {
        this.container = container;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        return (T) container
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getName().equals(name))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new UnsatisfiedDependencyException("Unable to find bean by class name: " + name));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<? extends T> clazz) {
        return Optional
                .ofNullable((T) container.get(clazz))
                .orElseThrow(() -> new UnsatisfiedDependencyException("Unable to find bean by class: " + clazz.getName()));
    }

    @Override
    public Map<Class<?>, Object> getBeans() {
        return Collections
                .unmodifiableMap(container);
    }

    @Override
    public <T> ApplicationContainer addBeanDefinition(Class<T> clazz) {
        System.out.println("A new bean has been added to the application container: " + clazz);
        final Object beanInstance = ContainerUtils
                .getConstructorsSortedByParamCount(clazz)
                .map(constructor -> {
                    System.out.println("Candidate constructor found: " + constructor);
                    return makeInstance(constructor);
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElseThrow(() -> new UnsatisfiedDependencyException("Failed to instantiate a bean of type: " + clazz.getName()));

        System.out.println("Successfully created bean " + beanInstance + " of type " + clazz.getName());

        //TODO:: add this bean instance for all the linear supertypes that it fulfills.
        container.put(clazz, beanInstance);

        return this;
    }

    @Override
    public String toString() {
        return "ApplicationContainer(" + container.toString() + ")";
    }

    private <T> Optional<T> makeInstance(Constructor<T> constructor) {
        return Try.ofSupplier(() -> {
            try {
                return constructor.newInstance(getAllOrFail(constructor.getParameterTypes()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("Unable to instantiate using constructor: " + constructor);
                throw new UnsatisfiedDependencyException("ignored");
            }
        }).toJavaOptional();
    }

    private Object[] getAllOrFail(Class<?>[] parameterTypes) {
        return Stream
                .of(parameterTypes)
                .map((paramType) -> {
                    final Object parameter = container.get(paramType);
                    if (Objects.isNull(parameter)) {
                        throw new UnsatisfiedDependencyException("Unable to find parameter of type " + paramType.getName());
                    }
                    return parameter;
                }).toArray();
    }

    public static ApplicationContainer scanPackage(String basePackageToScan) {
        final ApplicationContainer applicationContainer = new DefaultApplicationContainer();
        return ContainerUtils
                .scanPackageForComponents(basePackageToScan)
                .stream()
                .reduce(applicationContainer,
                        (ApplicationContainer::addBeanDefinition),
                        (firstContainer, secondContainer) -> {
                            final Map<Class<?>, Object> combinedBeans = new HashMap<>();
                            combinedBeans.putAll(firstContainer.getBeans());
                            combinedBeans.putAll(secondContainer.getBeans());
                            return new DefaultApplicationContainer(combinedBeans);
                        });
    }
}
