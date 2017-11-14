package com.upt.mse.barbuceanu.container;

import com.upt.mse.barbuceanu.container.annotations.Component;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 13/11/2017
 */
public class ContainerUtils {

    @SuppressWarnings("unchecked")
    public static <T> Stream<Constructor<T>> getConstructorsSortedByParamCount(Class<T> clazz) {
        return Arrays
                .stream((Constructor<T>[]) clazz.getConstructors())
                .sorted(Comparator.comparingInt(Constructor::getParameterCount));
    }

    public static List<Class<?>> scanPackageForComponents(String basePackageToScan) {
        return new Reflections(basePackageToScan, new SubTypesScanner(false))
                .getAllTypes()
                .stream()
                // get Class<T> instance
                .map(ContainerUtils::classFor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                // get managed beans only
                .filter(aClass -> Stream
                        .of(aClass.getAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(Component.class)))
                // get instantiable types
                .filter(aClass -> aClass.getConstructors().length != 0)
                .collect(Collectors.toList());
    }

    public static Optional<Class<?>> classFor(String name) {
        try {
            return Optional.ofNullable(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static Stream<Class<?>> buildClassHierarchy(Class<?> clazz) {
        if (Objects.isNull(clazz) || clazz.equals(Object.class)) {
            return Stream.empty();
        }
        final Stream.Builder<Class<?>> resultStream = Stream.builder();

        resultStream.add(clazz);
        Stream.of(clazz.getInterfaces())
                .forEach(resultStream::add);
        buildClassHierarchy(clazz.getSuperclass())
                .forEach(resultStream::add);

        return resultStream.build();
    }
}
