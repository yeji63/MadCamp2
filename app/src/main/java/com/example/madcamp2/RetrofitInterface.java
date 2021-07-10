package com.example.madcamp2;

import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @POST("/addgroup")
    Call<GroupItem> executeGroupAdd (@Body HashMap<String, String> map);

    @GET("/getgroup")
    Call<ArrayList<Listgroup>> executeGroupGet();


}