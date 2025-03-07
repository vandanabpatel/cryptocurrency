package com.example.cryptocurrency.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CryptoCurrencyModel(
    val id: Int,
    var name: String,
    var symbol: String,
    var logo: String,
    var quote: QuoteModel,
    var qty: Double,
    var assetValue: Double,
    var amountChange: Double,
) : Serializable

data class QuoteModel(
    @field:SerializedName("USD") val usd: UsdModel,
) : Serializable

data class UsdModel(
    var price: Double,
    @field:SerializedName("percent_change_24h") var percentChange24h: Double,
) : Serializable