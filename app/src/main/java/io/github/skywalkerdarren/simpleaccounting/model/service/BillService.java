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
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillDTO;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeAndStats;
import io.github.skywalkerdarren.simpleaccounting.util.BooleanTypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.util.DataTimeTypeAdapter;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BillService {

    public int uid;

    public static final MediaType JSONTpye
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    Gson JSON = new GsonBuilder().registerTypeAdapter(Boolean.TYPE,new BooleanTypeAdapter())
            .registerTypeAdapter(DateTime.class,new DataTimeTypeAdapter())
            .create();

    private int billsCount;

    @Nullable
    public Bill getBill(@NotNull String baseUrl,@NotNull UUID uuid) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/bill?uuid="+uuid+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        Bill bill = JSON.fromJson(body, Bill.class);
        return bill;
    }

    public void delBill(String baseUrl,@NotNull UUID uuid) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid",uuid.toString()).build();
        Request request = new Request.Builder().url(baseUrl + "/delBill"+"?uid="+uid).post(requestBody).build();
        Response response = client.newCall(request).execute();
    }

    @NotNull
    public List<Bill> getsBillsByDate(String baseUrl,@NotNull DateTime start, @Nullable DateTime end) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/bills?start="+start+"&end="+end+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        Log.d("BillService", "getsBillsByDate: "+body);
        List<JsonObject> objects = JSON.fromJson(body, new TypeToken<List<JsonObject>>(){}.getType());
        List<Bill> reuslt = new ArrayList<>();
        for(JsonObject object : objects){
            reuslt.add(JSON.fromJson(object,Bill.class));
        }
        return reuslt;
    }

    public void addBill(@NotNull String baseUrl, @NotNull Bill bill) throws IOException {
        BillDTO billDTO = new BillDTO(bill);
        Log.d("BillService", "addBill: "+billDTO);
        String json = JSON.toJson(billDTO);
        RequestBody requestBody = RequestBody.create(json,JSONTpye);
        Request request = new Request.Builder().url(baseUrl + "/addBill"+"?uid="+uid).post(requestBody).build();
        Response response = client.newCall(request).execute();
    }

    public void updateBill(@NotNull String baseUrl, @NotNull Bill bill) throws IOException {
        BillDTO billDTO = new BillDTO(bill);
        RequestBody requestBody = RequestBody.create(JSON.toJson(billDTO),JSONTpye);
        Request request = new Request.Builder().url(baseUrl + "/updateBill").post(requestBody).build();
        Response response = client.newCall(request).execute();
    }


    public int getbillsCount(String baseUrl) throws IOException {
        if(billsCount != 0){
            return billsCount;
        }
        Request request = new Request.Builder().url(baseUrl + "/billsCount?"+"uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return Integer.valueOf(body);
    }

    public int getBillsCount(@NotNull String baseUrl, @NotNull DateTime start, @Nullable DateTime end) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/billsCountByData?start="+start+"&end="+end+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return Integer.valueOf(body);
    }

    @NotNull
    public List<BillInfo> getBillByAccount(@NotNull String baseUrl, @NotNull UUID accountId) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/billsByAccount?accountId="+accountId+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        List<JsonObject> objects = JSON.fromJson(body, new TypeToken<List<JsonObject>>(){}.getType());
        List<BillInfo> reuslt = new ArrayList<>();
        for(JsonObject object : objects){
            reuslt.add(JSON.fromJson(object,BillInfo.class));
        }
        return reuslt;
    }

    @NotNull
    public List<BillInfo> getTypeBills(@NotNull String baseUrl, @NotNull TypeAndStats typeAndStats, @NotNull DateTime start, @NotNull DateTime end) throws IOException {
        String typeId = typeAndStats.getTypeStats().getTypeId().toString();
        Request request = new Request.Builder().url(baseUrl + "/billsByType?typeId="+typeId+"&start="+start+"&end="+end+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        List<JsonObject> objects = JSON.fromJson(body, new TypeToken<List<JsonObject>>(){}.getType());
        List<BillInfo> reuslt = new ArrayList<>();
        for(JsonObject object : objects){
            BillInfo e = JSON.fromJson(object, BillInfo.class);
            e.setBillTypeRes(typeAndStats.getType().getAssetName());
            e.setExpense(typeAndStats.getType().isExpense());
            reuslt.add(e);
        }
        return reuslt;
    }

//    private String replaceJson(String body){
//        // body如果有大括号
//        body.replaceAll()
//        if(body.matches("\\[[\\s\\S]*\\]")){
//
//        }
//    }
}
