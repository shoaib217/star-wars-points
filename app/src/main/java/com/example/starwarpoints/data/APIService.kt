package com.example.starwarpoints.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://jsonkeeper.com/b/"

interface APIService {

    @GET("IKQQ")
    suspend fun getPlayers(): Players?

    @GET("JNYL")
    suspend fun getMatchList(): MatchList?



    companion object{
        private var apiService: APIService? = null
        fun getInstance() : APIService{
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(APIService::class.java)
            }
            return apiService!!
        }
    }
}