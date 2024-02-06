package com.example.cryptofuturestrader;

import java.util.HashMap;
import java.util.Map;

public class Request {

    String baseUrl = "https://fapi.binance.com";
    Signature sign = new Signature();
    private String getTimeStamp() {
        long timestamp = System.currentTimeMillis();
        return "timestamp=" + String.valueOf(timestamp);
    }

    //concatenate query parameters
    private String joinQueryParameters(HashMap<String,String> parameters) {
        String urlPath = "";
        boolean isFirst = true;

        for (Map.Entry mapElement : parameters.entrySet()) {
            if (isFirst) {
                isFirst = false;
                urlPath += (String)mapElement.getKey() + "=" + (String)mapElement.getValue();
            } else {
                urlPath += "&" + (String)mapElement.getKey() + "=" + (String)mapElement.getValue();
            }
        }
        return urlPath;
    }

    public String sendSignedRequest(HashMap<String,String> parameters,String grkey) throws Exception {
            String queryPath = "";
            String signature = "";
        if (parameters!= null && !parameters.isEmpty()) {
                queryPath += joinQueryParameters(parameters) + "&" + getTimeStamp();
            } else {
                queryPath += getTimeStamp();
            }
        try {
            System.out.println(queryPath);
            System.out.println(grkey);
            signature = sign.getSignature(queryPath, grkey);
            }
        catch (Exception e) {
                System.out.println("Please Ensure Your Secret Key Is Set Up Correctly! " + e);
//                System.exit(0);
            }
            queryPath += "&signature=" + signature;

        return queryPath.toString();
        }
}

