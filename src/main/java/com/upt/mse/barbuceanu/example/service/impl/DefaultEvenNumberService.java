package com.upt.mse.barbuceanu.example.service.impl;

import com.upt.mse.barbuceanu.container.annotations.Component;
import com.upt.mse.barbuceanu.example.repository.impl.InMemoryNumbersRepository;
import com.upt.mse.barbuceanu.example.service.api.EvenNumbersService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 13/11/2017
 */
@Component
public class DefaultEvenNumberService implements EvenNumbersService {

    private final InMemoryNumbersRepository numbersRepository;

    //TODO:: add linear supertypes in container to be able to depend on abstractions
    public DefaultEvenNumberService(InMemoryNumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
    }

    @Override
    public List<Integer> getSomeEvenNumbers() {
        return numbersRepository
                .findSomeNumbers()
                .map(x -> x * 2)
                .collect(Collectors.toList());
    }
}
