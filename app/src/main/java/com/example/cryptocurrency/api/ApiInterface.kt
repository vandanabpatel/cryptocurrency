package com.example.cryptocurrency.api

import com.example.cryptocurrency.data.CryptoCurrencyListModel
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("v1/cryptocurrency/map?start=1&limit=20&sort=cmc_rank")
    suspend fun fetchCryptocurrency(): Response<CryptoCurrencyListModel>

    @GET("v2/cryptocurrency/quotes/latest")
    suspend fun fetchCurrency(@QueryMap params: Map<String, String>): Response<JsonObject>
}