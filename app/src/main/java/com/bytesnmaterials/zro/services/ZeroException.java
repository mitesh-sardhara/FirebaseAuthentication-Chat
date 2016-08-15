package com.bytesnmaterials.zro.services;

/**
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public class ZeroException extends RuntimeException {
    public ZeroException(String message) {
        super(message);
    }

    public ZeroException(String message, Throwable cause) {
        super(message, cause);
    }
}

