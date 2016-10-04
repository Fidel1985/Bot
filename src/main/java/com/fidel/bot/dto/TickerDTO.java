package com.fidel.bot.dto;

import java.math.BigDecimal;

import com.fidel.bot.enumeration.Pair;
import com.google.common.base.MoreObjects;

public class TickerDTO {
    private Pair pair;
    private String volume;
    private String volume30d;
    private double high;
    private BigDecimal last;
    private double low;
    private double ask;
    private double bid;
    private long timestamp;

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolume30d() {
        return volume30d;
    }

    public void setVolume30d(String volume30d) {
        this.volume30d = volume30d;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pair", pair)
                .add("last", last)
                .add("high", high)
                .add("low", low)
                .add("ask", ask)
                .add("bid", bid)
                .toString();
    }
}
