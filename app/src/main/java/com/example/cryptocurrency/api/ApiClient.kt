package com.example.cryptocurrency.api

import android.app.Activity
import com.example.cryptocurrency.utilities.Utility
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    const val BASE_URL = "https://pro-api.coinmarketcap.com/"

    fun apiInterface(context: Activity): ApiInterface {
        val httpClient = getClientBuilder(context)
        return getClient(httpClient, BASE_URL).create(
            ApiInterface::class.java
        )
    }

    private fun getClientBuilder(context: Activity): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(1, TimeUnit.MINUTES)
        httpClient.readTimeout(3, TimeUnit.MINUTES)

        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)

        httpClient.cache(myCache)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()

            request.addHeader(
                "Accept",
                "application/json"
            )

            // added api key for fetch cryptocurrency
            request.addHeader(
                "X-CMC_PRO_API_KEY",
                "32f35d5e-2101-441a-9914-02df3c297888"
            )


            if (Utility.isNetworkAvailable(context)) {
                /**
                 * If there is Internet, get the cache that was stored 10 seconds ago.
                 * If the cache is older than 10 seconds, then discard it,
                 * and indicate an error in fetching the response.
                 * The 'max-age' attribute is responsible for this behavior.
                 */
                request.addHeader("Cache-Control", "public, max-age=" + 10)
            } else {
                /**
                 * If there is no Internet, get the cache that was stored 1 day ago.
                 * If the cache is older than 1 day, then discard it,
                 * and indicate an error in fetching the response.
                 * The 'max-stale' attribute is responsible for this behavior.
                 * The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                 */
                request.addHeader(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24
                )
            }

            chain.proceed(request.build())
        }

        // for api request/response logging
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(interceptor)

        return httpClient
    }

    private fun getClient(httpClient: OkHttpClient.Builder, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(httpClient.build())
            .build()
    }
}
