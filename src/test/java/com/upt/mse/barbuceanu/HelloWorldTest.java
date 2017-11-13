package com.upt.mse.barbuceanu;

import org.junit.Test;

import java.util.stream.Stream;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 12/11/2017
 */
public class HelloWorldTest {

    @Test
    public void simpleEqualityTestShouldPass() {
        assert(Stream.of(1, 2, 3).map(x -> x + 1).filter(x -> x == 4).count() == 1);
    }
}
