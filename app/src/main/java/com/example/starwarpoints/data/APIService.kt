package com.example.starwarpoints.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://api.npoint.io/"

interface APIService {

    @GET("ca180e840b481675d500")
    suspend fun getPlayers(): Response<Players?>

    @GET("bc3f07c7442e85446788")
    suspend fun getMatchList(): Response<MatchList?>



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