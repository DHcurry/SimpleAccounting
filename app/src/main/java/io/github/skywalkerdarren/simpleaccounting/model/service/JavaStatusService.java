package io.github.skywalkerdarren.simpleaccounting.model.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Stats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;
import io.github.skywalkerdarren.simpleaccounting.util.BooleanTypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.util.DataTimeTypeAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JavaStatusService {

    public int uid;
    OkHttpClient client = new OkHttpClient();

    Gson JSON = new GsonBuilder().registerTypeAdapter(Boolean.TYPE,new BooleanTypeAdapter())
            .registerTypeAdapter(DateTime.class,new DataTimeTypeAdapter())
            .create();

    public List<Stats> getBillsStats(String baseUrl, DateTime start, DateTime end) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/billstatus?start="+start+"&end="+end+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        Log.d("JavaStatusService", "uid = "+uid);
//        Log.d("JavaStatusService", "getBillsStats: body="+body);
//        List<BillStats> billStats = JSON.fromJson(body,new TypeToken<List<BillStats>>(){}.getType());
        List<JsonObject> objects = JSON.fromJson(body, new TypeToken<List<JsonObject>>(){}.getType());
        List<Stats> reuslt = new ArrayList<>();
        for(JsonObject object : objects){
            reuslt.add(JSON.fromJson(object,Stats.class));
        }
        return reuslt;
//        List<BillStats> billStats =  Arrays.asList(JSON.fromJson(body,BillStats.class));
    }

    @NotNull
    public List<Stats> getAccountStats(String baseUrl,@NotNull UUID accountId, @NotNull DateTime start, @NotNull DateTime end) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/accountStatus?start="+start+"&end="+end+"&accountId="+accountId+"&uid="+uid).build();
        Log.d("JavaStatusService", "uid = "+uid);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        List<Stats> stats = JSON.fromJson(body,new TypeToken<List<Stats>>(){}.getType());
        return stats;
    }

    @Nullable
    public List<TypeStats> getTypesStats(@NotNull String baseUrl, @NotNull DateTime start, @NotNull DateTime end, boolean expense) throws IOException {
        int intExpense = expense?1:0;
        Request request = new Request.Builder().url(baseUrl + "/typesStatus?start="+start+"&end="+end+"&expense="+intExpense+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        List<TypeStats> stats = JSON.fromJson(body,new TypeToken<List<TypeStats>>(){}.getType());
        return stats;
    }
}
