package com.example.cryptofuturestrader;

import java.util.HashMap;

public class GetParams {

    GetParams(){
    }

    public HashMap grabURL() throws Exception {
        HashMap<String,String> parameters = new HashMap<String,String>();
        return parameters;
    }

    public HashMap grabParams(String symbol,long startDate,long endDate) throws Exception {
        HashMap<String,String> parameters1 = new HashMap<String,String>();
        parameters1.put("symbol",symbol);
        parameters1.put("startTime", String.valueOf(startDate));
        parameters1.put("endTime", String.valueOf(endDate));
        return parameters1;
    }






}




