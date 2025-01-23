package com.retailer.rewards.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String m) {
        super(m);
    }
}

