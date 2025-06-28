package com.example.reservationapp.Server

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIService {
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseBody>
}
