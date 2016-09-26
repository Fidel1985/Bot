package com.fidel.bot.jpa;

import com.google.common.base.MoreObjects;

public class Balance {
    private Currency usd;
    private Currency btc;
    private Currency eth;
    private long timestamp;
    private String username;

    public Balance() {
        usd = new Currency();
        btc = new Currency();
        eth = new Currency();
    }

    public Currency getUsd() {
        return usd;
    }

    public void setUsd(Currency usd) {
        this.usd = usd;
    }

    public Currency getBtc() {
        return btc;
    }

    public void setBtc(Currency btc) {
        this.btc = btc;
    }

    public Currency getEth() {
        return eth;
    }

    public void setEth(Currency eth) {
        this.eth = eth;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public java.lang.String getUsername() {
        return username;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("usd", usd)
                .add("btc", btc)
                .add("eth", eth)
                .add("timestamp", timestamp)
                .add("username", username)
                .toString();
    }
}
