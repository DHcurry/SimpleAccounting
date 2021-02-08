package io.github.skywalkerdarren.simpleaccounting.model.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.io.IOException;

import io.github.skywalkerdarren.simpleaccounting.util.BooleanTypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.util.DataTimeTypeAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginService {
    public static final MediaType JSONTpye
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    Gson JSON = new GsonBuilder().registerTypeAdapter(Boolean.TYPE,new BooleanTypeAdapter())
            .registerTypeAdapter(DateTime.class,new DataTimeTypeAdapter())
            .create();

    public int login(@NotNull String baseUrl, @NotNull String username, @NotNull String password) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/login?username="+username+"&password="+password).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return Integer.valueOf(body);
    }
}
