package com.example.cryptocurrency.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptocurrency.data.CryptoCurrencyModel
import com.example.cryptocurrency.data.ErrorModel
import com.example.cryptocurrency.utilities.Utility
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val totalValue = MutableLiveData<Double>()
    val currencyList = MutableLiveData<List<CryptoCurrencyModel>>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    /**
     * Fetch list of crypto currency from CoinMarketCap and pass API_KEY in header
     * refer ApiClient.kt class
     * once we have a response get all IDs of crypto currency and call another API to fetch other details
     */
    fun getCurrencyList() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.fetchCryptocurrency()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val list = response.body()?.data

                    loading.value = false
                    getCurrency(list!!)

                } else {
                    val json = response.errorBody()?.string()
                    val errorResponse = Utility.jsonToPojo(
                        json, ErrorModel::class.java
                    ) as ErrorModel
                    onError("Error : ${errorResponse.status.errorMessage} ")
                }
            }
        }
    }

    /**
     * Fetch crypto currency's details
     */
    private fun getCurrency(list: List<CryptoCurrencyModel>) {
        val ids = ArrayList<Int>()
        for (item in list) {
            ids.add(item.id)
        }

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val params: MutableMap<String, String> = HashMap()
            params["id"] = ids.joinToString(",").trim()

            val response = mainRepository.fetchCurrency(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val json = response.body() as JsonObject
                    val data = json.get("data") as JsonObject

                    var total = 0.0
                    for (item in list) {
                        val details = data.get(item.id.toString()) as JsonObject
                        val detailResponse = Utility.jsonToPojo(
                            details.toString(), CryptoCurrencyModel::class.java
                        ) as CryptoCurrencyModel

                        val randomValue = Random.nextDouble(1.0, 10.0)
                        val assetValue = detailResponse.quote.usd.price * randomValue
                        val percentChange24h = detailResponse.quote.usd.percentChange24h
                        val amountChange = detailResponse.quote.usd.price * percentChange24h / 100

                        item.name = detailResponse.name
                        item.symbol = detailResponse.symbol
                        item.logo =
                            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
                        item.quote = detailResponse.quote
                        item.qty = randomValue
                        item.assetValue = assetValue
                        item.quote.usd.percentChange24h = percentChange24h
                        item.amountChange = amountChange

                        total += assetValue
                    }
                    totalValue.postValue(total)
                    currencyList.postValue(list)
                    loading.value = false
                } else {
                    val json = response.errorBody()?.string()
                    val errorResponse = Utility.jsonToPojo(
                        json, ErrorModel::class.java
                    ) as ErrorModel
                    onError("Error : ${errorResponse.status.errorMessage} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}