package com.fidel.bot;

import com.fidel.bot.jpa.Balance;
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
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CheckConnection {
    private static final Logger LOG = LoggerFactory.getLogger(CheckConnection.class);

    @Value("${credentials.api_key}")
    private String api_key;
    @Value("${credentials.secret}")
    private String secret;
    @Value("${credentials.userID}")
    private String userID;
    private String nonce = String.valueOf(System.currentTimeMillis());



    @Scheduled(initialDelayString = "${configuration.updateCheck.delay:10}000", fixedDelayString = "${configuration.updateCheck.schedule:10}000")
    public void checkAvailableUpdates() {

        //Balance balance = new Balance();
        Object result = balance();

        //System.out.print("balance " + balance().toString());
    }

    public Object balance(){
        return api_call("balance", null, 1);
    }

    private Object api_call(String method, HashMap<String,String> hashMap, Integer authData){ // api call (Middle level)
        return api_call(method, hashMap, authData, null);
    }

    private Object api_call(String method, HashMap<String,String> hashMap, Integer authData, String couple){ // api call (Middle level)
        if(hashMap == null){hashMap = new HashMap<>();}
        String path = "/api/" + method + "/";//generate url
        Object answer;
        if (couple != null) {
            path = path + couple + "/";//
        }
        if (authData == 1) { //add auth-data if needed
            hashMap.put("key", api_key);
            hashMap.put("signature", getSignature());
            hashMap.put("nonce", nonce);
        }
        answer = post("https://cex.io" + path, hashMap); //Post Request

        return answer;
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
            httppost.setHeader("User-Agent", "bot-cex.io-" + userID);

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity responseEntity = response.getEntity();

            String jsonResultString = EntityUtils.toString(responseEntity);

            return JSONValue.parse(jsonResultString);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
        return null;
    }

}
