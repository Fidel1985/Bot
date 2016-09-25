package com.fidel.bot.controller;

import com.fidel.bot.controller.jpa.Auth;
import com.fidel.bot.controller.jpa.Balance;
import com.fidel.bot.controller.jpa.Quote;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Calendar;

@Component
public class CheckConnection {
    private static final Logger LOG = LoggerFactory.getLogger(CheckConnection.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Check for available updates from main application server by organizationId
     */
    @Scheduled(initialDelayString = "${configuration.updateCheck.delay:10}000", fixedDelayString = "${configuration.updateCheck.schedule:10}000")
    public void checkAvailableUpdates() {

        try{

            //Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
            //LOG.info(quote.toString());

            String key = "mfqF7q08JVJyJwXGuOPwxPUok";
            String secret = "37WHDBXDwEAqM5bnZVvTaZZiU";
            String userID = "up102588748";
            String nonce = String.valueOf(System.currentTimeMillis());

            String message = nonce + userID + key;

            try {
                LOG.info("start to build Auth object");

                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
                sha256_HMAC.init(secret_key);

                String signature = Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes())).toUpperCase();
                LOG.info("signature = " + signature);

                Auth auth = new Auth(key, signature, nonce);
                LOG.info(auth.toString());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                String requestJson = String.format("{\"key\":\"%s\",\"signature\":\"%s\",\"nonce\":\"%s\"}", key, signature, nonce);
                HttpEntity<String> entity = new HttpEntity<>(requestJson,headers);
                //restTemplate.put(uRL, entity);
                LOG.info("requestJson = " + requestJson);

                //String response = restTemplate.postForObject("https://cex.io/api/balance/", entity, String.class);
                //LOG.info(response);

            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                LOG.error("NoSuchAlgorithmException" + e);
            }


        } catch (RestClientException e) {
            LOG.error("Error obtaining info from server: {} \n {}", e.getMessage(), e);
        }



    }

    private String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
