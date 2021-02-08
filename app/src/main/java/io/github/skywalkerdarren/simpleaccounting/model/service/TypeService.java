package io.github.skywalkerdarren.simpleaccounting.model.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.BooleanTypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.util.DataTimeTypeAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TypeService {
    public int uid;
    public static final MediaType JSONTpye
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    Gson JSON = new GsonBuilder().registerTypeAdapter(Boolean.TYPE,new BooleanTypeAdapter())
            .registerTypeAdapter(DateTime.class,new DataTimeTypeAdapter())
            .create();

    @Nullable
    public Type getType(String baseUrl ,@NotNull UUID uuid) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/type?uuid="+uuid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        Type type = JSON.fromJson(body, Type.class);
        return type;
    }

    @Nullable
    public TypeStats getTypeStats(@NotNull String baseUrl, @NotNull DateTime start, @NotNull DateTime end, @NotNull UUID typeId) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/typeStatus?typeId="+typeId+"&start="+start+"&end="+end+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        TypeStats typeStats = JSON.fromJson(body,TypeStats.class);
        return typeStats;
    }

    @Nullable
    public TypeStats getTypeAverageStats(@NotNull String baseUrl, @NotNull DateTime start, @NotNull DateTime end, @NotNull UUID typeId) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/typeAverageStatus?typeId="+typeId+"&start="+start+"&end="+end+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        TypeStats typeStats = JSON.fromJson(body,TypeStats.class);
        return typeStats;
    }

    @Nullable
    public List<Type> getTypes(@NotNull String baseUrl, boolean expense) throws IOException {
        int intExpense = expense?1:0;
        Request request = new Request.Builder().url(baseUrl + "/types?expense="+intExpense).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        Log.d("getTypes", "expense="+expense+ " getTypes: "+body);
        List<Type> types = JSON.fromJson(body, new TypeToken<List<Type>>(){}.getType());
        return types;
    }
}
