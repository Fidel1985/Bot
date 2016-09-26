package com.fidel.bot.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
public class AccountManager {
    private static final Logger LOG = LoggerFactory.getLogger(AccountManager.class);

    @Value("${credentials.api_key}")
    private String api_key;
    @Value("${credentials.secret}")
    private String secret;
    @Value("${credentials.userID}")
    private String userID;
    private String nonce;

    private Object api_call(String method, HashMap<String,String> hashMap, Integer authData){ // api call (Middle level)
        return api_call(method, hashMap, authData, null);
    }

    private Object api_call(String method, HashMap<String,String> hashMap, Integer authData, String couple){ // api call (Middle level)
        if(hashMap == null) {
            hashMap = new HashMap<>();
        }
        String path = "/api/" + method + "/";//generate url
        if (couple != null) {
            path = path + couple + "/";
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
            LOG.info("generating signature");
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes())).toUpperCase();

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOG.error("NoSuchAlgorithmException" + e);
            return null;
        }
    }

    private Object post(String strUrl, HashMap<String,String> hashMap){//Post Request (Low Level API call)
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
        return null;
    }

    public Object balance(){
        return api_call("balance", null, 1);
    }

    public Object current_orders(){
        return current_orders(null);
    }

    public Object current_orders(String couple){
        if(couple == null) couple = "GHS/BTC";
        return api_call("open_orders", null, 1, couple);
    }

    public Object cancel_order(Integer order_id){
        HashMap<String, String> hmap = new HashMap<String,String>();
        hmap.put("id", order_id.toString());
        return api_call("cancel_order", hmap, 1);
    }

    public Object place_order(String ptype, Double amount,Double price) {
        return place_order(ptype,amount,price);
    }

    public Object place_order(String ptype, Double amount, Double price, String couple) {
        if(ptype==null) ptype="buy";
        if(amount==null || amount < 0) amount = new Double(1);
        if(price == null || price < 0) price = new Double(1);
        if(couple == null) couple = "GHS/BTC";

        HashMap<String, String> hmap = new HashMap<>();
        hmap.put("type", ptype);
        hmap.put("amount", amount.toString());
        hmap.put("price", price.toString());
        return api_call("place_order", hmap, 1, couple);
    }

}
