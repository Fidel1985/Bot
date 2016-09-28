package com.fidel.bot.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestController {
    private static final Logger LOG = LoggerFactory.getLogger(RequestController.class);

    @Value("${credentials.api_key}")
    private String api_key;
    @Value("${credentials.secret}")
    private String secret;
    @Value("${credentials.userID}")
    private String userID;
    private String nonce;

    private Object api_call(String method, HashMap<String,String> hashMap, Integer authData, String pair) throws  EmptyResponseException { // api call (Middle level)
        if(hashMap == null) {
            hashMap = new HashMap<>();
        }
        String path = "/api/" + method + "/";//generate url
        if (pair != null) {
            path = path + pair + "/";
        }
        if (authData == 1) { //add auth-data if needed
            generateNonce();
            hashMap.put("key", api_key);
            hashMap.put("signature", getSignature());
            hashMap.put("nonce", nonce);
        }

        return post("https://cex.io" + path, hashMap); //Post Request
    }

    private void generateNonce(){
        nonce = String.valueOf(System.currentTimeMillis());
    }

    private String getSignature() {
        String message = nonce + userID + api_key;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes())).toUpperCase();

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOG.error("NoSuchAlgorithmException" + e);
            return null;
        }
    }

    private Object post(String strUrl, HashMap<String,String> hashMap) throws EmptyResponseException {//Post Request (Low Level API call)
        HttpPost httppost = new HttpPost(strUrl);
        try {
            HttpClient httpclient = HttpClientBuilder.create().build();
            List<NameValuePair> nameValuePairs = hashMap.keySet().stream().
                    map(x->new BasicNameValuePair(x, hashMap.get(x))).collect(Collectors.toList());
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity responseEntity = response.getEntity();
            String jsonResultString = EntityUtils.toString(responseEntity);
            return JSONValue.parse(jsonResultString);

        } catch (IOException e) {
            LOG.error("Fail to execute POST method {}", e.getMessage());
        } finally {
            httppost.releaseConnection();
        }
        throw new EmptyResponseException("Fail to obtain response from server");
    }

    public Object ticker(String couple) throws EmptyResponseException, InvalidSymbolsPairException {
        return api_call("ticker", null, 0, couple);
    }

    public Object balance() throws EmptyResponseException {
        return api_call("balance", null, 1, null);
    }

    public Object open_orders(String couple) throws EmptyResponseException, InvalidSymbolsPairException {
        return api_call("open_orders", null, 1, couple);
    }

    /*public Object archived_orders(String couple) throws EmptyResponseException, InvalidSymbolsPairException {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("limit", String.valueOf(order_id));
        return api_call("archived_orders", hashMap, 1, couple);
    }*/

    public Object get_order(long Id) throws EmptyResponseException {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", String.valueOf(Id));
        return api_call("get_order", hashMap, 1, null);
    }

    public Object cancel_order(long Id) throws EmptyResponseException {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", String.valueOf(Id));
        return api_call("cancel_order", hashMap, 1, null);
    }

    public Object place_order(String pairType, double amount, double price, String pair) throws EmptyResponseException {
        if(!pairType.equals("buy") && !pairType.equals("sell")) {
            return null; // invalid param exception
        }
        if(amount < 0 || price < 0) {
            amount = 0;
            price = 0;
        }
        if(pair == null) {
            return null;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type", pairType);
        hashMap.put("amount", String.valueOf(amount));
        hashMap.put("price", String.valueOf(price));
        return api_call("place_order", hashMap, 1, pair);
    }

}
