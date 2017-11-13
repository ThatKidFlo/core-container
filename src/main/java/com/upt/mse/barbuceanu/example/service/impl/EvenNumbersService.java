package com.upt.mse.barbuceanu.example.service.impl;

import com.upt.mse.barbuceanu.container.annotations.Component;
import com.upt.mse.barbuceanu.example.service.api.ExampleService;

import java.util.Arrays;
import java.util.List;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 13/11/2017
 */
@Component
public class EvenNumbersService implements ExampleService {
    @Override
    public List<Integer> getSomeNumbers() {
        return Arrays.asList(0, 2, 4, 6, 8, 10);
    }
}
