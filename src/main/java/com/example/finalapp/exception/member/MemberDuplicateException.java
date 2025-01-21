package com.example.finalapp.exception.member;


public class MemberDuplicateException extends RuntimeException {
    public MemberDuplicateException(String message) {
        super(message);
    }
}
