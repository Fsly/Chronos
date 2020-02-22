package com.example.chronos.connect;

import android.util.Log;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Connect {
    public int register(String account, String nickname, String password) {

        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("account", account)
                    .add("nickname", nickname)
                    .add("password", password)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://39.97.188.42:8080/HelloWeb_war/reg.do")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String jie=response.body().string();
            Log.d("connect",jie);
            if (jie.equals("1")) return 1;
            else if(jie.equals("2")) return 2;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean login(String account, String password) {

        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("account", account)
                    .add("password", password)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://39.97.188.42:8080/HelloWeb_war/log.do")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String jie=response.body().string();
            Log.d("LoginActivity","登录结果：" + jie);
            if (jie.equals("true")) return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getUserData(String account, String password) {

        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("account", account)
                    .add("password", password)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://39.97.188.42:8080/HelloWeb_war/user_data.do")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String jie=response.body().string();
            return jie;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(false);
    }
}
