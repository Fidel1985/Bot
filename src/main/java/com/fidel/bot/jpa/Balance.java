package com.fidel.bot.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
    private BTC btc;

    public Balance() {
    }

    public Balance(BTC btc) {
        this.btc = btc;
    }

    public BTC getBtc() {
        return btc;
    }

    public void setBtc(BTC btc) {
        this.btc = btc;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("btc", btc)
                .toString();
    }
}
