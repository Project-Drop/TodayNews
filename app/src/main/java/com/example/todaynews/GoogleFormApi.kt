package com.example.todaynews

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleFormApi {
    @FormUrlEncoded
    @POST("/forms/d/e/1FAIpQLSeyVOoRi-hnsZPGT4yX0ODUUet8kw_F0cBd5jHNB1bIzAAfuQ/formResponse")
    fun sendFormData(
        @Field("entry.1618555544") name: String,
        @Field("entry.1007337472") location: String,
        @Field("entry.401445507") experience: String,
        @Field("entry.1687422396") improvements: String,
    ): Call<Void>
}