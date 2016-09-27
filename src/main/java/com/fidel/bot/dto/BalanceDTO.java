package com.fidel.bot.dto;

import com.google.common.base.MoreObjects;

public class BalanceDTO {
    private CurrencyDTO usd;
    private CurrencyDTO btc;
    private CurrencyDTO eth;
    private long timestamp;
    private String username;

    public BalanceDTO() {
        usd = new CurrencyDTO();
        btc = new CurrencyDTO();
        eth = new CurrencyDTO();
    }

    public CurrencyDTO getUsd() {
        return usd;
    }

    public void setUsd(CurrencyDTO usd) {
        this.usd = usd;
    }

    public CurrencyDTO getBtc() {
        return btc;
    }

    public void setBtc(CurrencyDTO btc) {
        this.btc = btc;
    }

    public CurrencyDTO getEth() {
        return eth;
    }

    public void setEth(CurrencyDTO eth) {
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
