package com.application.kiit_tnp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPManager {

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    public  Response send_postRequest(String url, String auth) throws IOException {

        RequestBody reqbody = RequestBody.create(null, new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .method("POST",reqbody)
                .header("Authorization", "Basic "+auth)
                .header("geo_loc_sec", "undefined")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public Response send_getRequest(String url, String[] data) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("rollno",data[0])
                .header("sessionid1",data[1])
                .header("sessionid2",data[2])
                .header("sessionid3",data[3])
                .header("sessionid4",data[4])
                .header("notice_id", data[5])
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public Response getNoticeDown(String url, String[] data) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("rollno",data[0])
                .header("sessionid1",data[1])
                .header("sessionid2",data[2])
                .header("sessionid3",data[3])
                .header("sessionid4",data[4])
                .header("notice_id", data[5])
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

}
