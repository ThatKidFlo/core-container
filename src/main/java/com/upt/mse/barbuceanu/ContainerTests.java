package com.upt.mse.barbuceanu;

import com.upt.mse.barbuceanu.container.annotations.Component;
import com.upt.mse.barbuceanu.container.api.ApplicationContainer;
import com.upt.mse.barbuceanu.container.exception.UnsatisfiedDependencyException;
import com.upt.mse.barbuceanu.container.impl.DefaultApplicationContainer;
import io.vavr.Tuple2;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 12/11/2017
 */
public class ContainerTests {

    public static void main(String[] args) {
        //TODO:: lift this into the creation of the Application container, taking the starting package as a param.
        Reflections refl = new Reflections("com.upt.mse.barbuceanu", new SubTypesScanner(false));
//        refl.getAllTypes().forEach(System.out::println);

        final List<Class<?>> classList = refl
                .getAllTypes()
                .stream()
                // get Class<T> instance
                .map(ContainerTests::classFor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                // get managed beans only
                .filter(aClass -> Stream.of(aClass.getAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(Component.class)))
                // get instantiable types
                .filter(aClass -> aClass.getConstructors().length != 0)
                .collect(Collectors.toList());

        final ApplicationContainer applicationContainer = new DefaultApplicationContainer();

        classList.forEach(applicationContainer::addBeanDefinition);

        System.out.println(classList);

        // Get all constructors, sorted by instantiation difficulty, aggregated by class
        final Map<Class<?>, List<Constructor<?>>> constructors = classList
                .stream()
                .map(clazz -> new Tuple2<Class<?>, List<Constructor<?>>>(clazz, getSortedConstructors(clazz)))
                .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
//                .collect(Collectors.toMap(tuple2 -> tuple2::_1, Tuple2::_2);

        System.out.println(constructors);

    }

    private static Optional<Class<?>> classFor(String name) {
        try {
            return Optional.ofNullable(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    private static <T> List<Constructor<?>> getSortedConstructors(Class<T> clazz) {
        return Arrays
                .stream((Constructor<T>[]) clazz.getConstructors())
                .sorted(Comparator.comparingInt(Constructor::getParameterCount))
                .collect(Collectors.toList());
    }

    private static <T> T tryInstantiate(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsatisfiedDependencyException("Failed to instantiate a bean of type: " + clazz.getName());
        }
    }
}
