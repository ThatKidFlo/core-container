package com.upt.mse.barbuceanu;

import com.upt.mse.barbuceanu.container.api.ApplicationContainer;
import com.upt.mse.barbuceanu.container.impl.DefaultApplicationContainer;
import com.upt.mse.barbuceanu.example.service.impl.DefaultEvenNumberService;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 12/11/2017
 */
public class ContainerTests {

    public static void main(String[] args) {

        final ApplicationContainer applicationContainer = DefaultApplicationContainer
                .scanPackage("com.upt.mse.barbuceanu.example");

        System.out.println(applicationContainer);

        applicationContainer.getBean(DefaultEvenNumberService.class).getSomeEvenNumbers().forEach(System.out::println);
    }
}
