package com.ajisansin.iqraquran.data.source

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://ajisansin.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface QuranApiService {
    @GET("api/quran_data.json")
    suspend fun getSurah(): List<QuranEntity>
}

object QuranApi {
    val retrofitService: QuranApiService by lazy {
        retrofit.create(QuranApiService::class.java)
    }
}