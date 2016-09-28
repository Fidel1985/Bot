package com.fidel.bot.enumeration;

public enum Status {
    ACTIVE("a"), DONE("d"), CANCELED("c"), CANCEL_DONE("cd");
    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
