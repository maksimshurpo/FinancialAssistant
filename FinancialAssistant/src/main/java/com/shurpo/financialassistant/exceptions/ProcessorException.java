package com.shurpo.financialassistant.exceptions;

public class ProcessorException extends Exception{

    private String message;

    public ProcessorException() {
    }

    public ProcessorException(String detailMessage) {
        super(detailMessage);
        message = detailMessage;
    }

    @Override
    public String toString() {
        return message;
    }
}
