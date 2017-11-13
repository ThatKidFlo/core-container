package com.upt.mse.barbuceanu.example.repository.impl;

import com.upt.mse.barbuceanu.container.annotations.Component;
import com.upt.mse.barbuceanu.example.repository.api.NumbersRepository;

import java.util.stream.Stream;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 13/11/2017
 */
@Component
public class InMemoryNumbersRepository implements NumbersRepository {

    @Override
    public Stream<Integer> findSomeNumbers() {
        return Stream.of(1, 2, 3, 4, 5, 6);
    }
}
