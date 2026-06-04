package com.sesac.aibackend0604.error;

public class NotFoundException extends RuntimeException {


    public NotFoundException(String message) {
        super (message);
    }

    public static NotFoundException of(String resource, Object id) {
        return new NotFoundException(resource + " not found: " + id);
    }
}
