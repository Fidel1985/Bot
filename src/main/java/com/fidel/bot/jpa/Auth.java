package com.fidel.bot.jpa;

public class Auth {

    private String key;
    private String signature;
    private String nonce;

    public Auth(String key, String signature, String nonce) {
        this.key = key;
        this.signature = signature;
        this.nonce = nonce;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("key", key)
                .add("signature", signature)
                .add("nonce", nonce)
                .toString();
    }
}
