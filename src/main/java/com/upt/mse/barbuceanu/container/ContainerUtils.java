package com.upt.mse.barbuceanu.container;

import com.upt.mse.barbuceanu.container.annotations.Component;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
}
