package com.example.UserService.exception.card;

public class CardNumberAlreadyExistsException extends RuntimeException {
    public CardNumberAlreadyExistsException(String number) {
        super("Card with number already exists: " + number);
    }
}
