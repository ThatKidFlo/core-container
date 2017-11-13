package com.upt.mse.barbuceanu.container.exception;

/**
 * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
 * @since 13/11/2017
 */
public class UnsatisfiedDependencyException extends RuntimeException {
    public UnsatisfiedDependencyException(String s) {
        super(s);
    }
}
