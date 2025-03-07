package com.example.cryptocurrency.viewmodel

import com.example.cryptocurrency.api.ApiInterface

class MainRepository(private val retrofitService: ApiInterface) {

    suspend fun fetchCryptocurrency() = retrofitService.fetchCryptocurrency()

    suspend fun fetchCurrency(params: Map<String, String>) = retrofitService.fetchCurrency(params)
}