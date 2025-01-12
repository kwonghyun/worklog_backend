package com.example.worklog.dto;

public record SimpleMessageRes(String message) {

    public static SimpleMessageRes from(SuccessMessage successMessage) {
        return new SimpleMessageRes(successMessage.getMessage());
    }

    public static SimpleMessageRes from(ErrorMessage errorMessage) {
        return new SimpleMessageRes(errorMessage.getMessage());
    }
}
