package io.github.skywalkerdarren.simpleaccounting.model.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;
import io.github.skywalkerdarren.simpleaccounting.util.BooleanTypeAdapter;
import io.github.skywalkerdarren.simpleaccounting.util.DataTimeTypeAdapter;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JavaAccountService {
    public int uid;

    public static final MediaType JSONTpye
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    Gson JSON = new GsonBuilder().registerTypeAdapter(Boolean.TYPE,new BooleanTypeAdapter())
            .registerTypeAdapter(DateTime.class,new DataTimeTypeAdapter())
            .create();

    public List<Account> getAccounts(String baseUrl) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/accounts"+"?uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        List<Account> accounts = JSON.fromJson(body, new TypeToken<List<Account>>(){}.getType());
        Log.d("JavaAccountService", "getAccounts: " + accounts);
        return accounts;
    }

    @Nullable
    public Account getAccount(@NotNull String baseUrl, @NotNull UUID uuid) throws IOException {
        Request request = new Request.Builder().url(baseUrl + "/account?uuid="+uuid+"&uid="+uid).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        Account account = JSON.fromJson(body, Account.class);
        return account;
    }

    public void updateAccountId(String baseUrl,@NotNull UUID uuid, int i) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid",uuid.toString())
                .add("id",String.valueOf(i))
                .add("uid",String.valueOf(uid))
                .build();
        Request request = new Request.Builder().url(baseUrl + "/changeId").post(requestBody).build();
        Response response = client.newCall(request).execute();
    }

    public String updateAccountBalance(String baseUrl, @NotNull UUID uuid, @Nullable BigDecimal balance) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid",uuid.toString())
                .add("balance",balance.toEngineeringString())
                .add("uid",String.valueOf(uid))
                .build();
        Request request = new Request.Builder().url(baseUrl + "/changeAccountBalance").post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String addAccount(@NotNull String baseUrl, @NotNull String name, @NotNull String hint, @NotNull String balance) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("name",name)
                .add("hint",hint)
                .add("balance",balance)
                .add("uid",String.valueOf(uid))
                .build();
        Request request = new Request.Builder().url(baseUrl+"/addAccount").post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @NotNull
    public String delAccount(@NotNull String baseUrl, @NotNull UUID uuid) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid",uuid.toString())
                .add("uid",String.valueOf(uid))
                .build();
        Request request = new Request.Builder().url(baseUrl+"/delAccount").post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
