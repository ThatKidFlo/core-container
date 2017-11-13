package com.upt.mse.barbuceanu.example.repository.api;

import java.util.stream.Stream;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 13/11/2017
 */
public interface NumbersRepository {
    Stream<Integer> findSomeNumbers();
}
