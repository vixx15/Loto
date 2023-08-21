package com.example.loto.api

import com.example.loto.dto.requestOffers.OfferPostData
import com.example.loto.dto.responseOffers.AllOffers
import com.example.loto.dto.requestOffers.PriorityGames
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {

    private var my_api: ApiService


    init {
        val retrofit = Retrofit.Builder().baseUrl("https://api.mozzartbet.com/#!/external")
            .addConverterFactory(GsonConverterFactory.create()).build()

        my_api = retrofit.create(ApiService::class.java)
    }


    suspend fun getData(): Call<AllOffers> {

        val request = OfferPostData(
            countryId = 1,
            cutoffHours = 24,
            languageId = 1,
            priorityGames = arrayListOf(
                PriorityGames(gameId = 26, numberOfRounds = 20, priority = 1),
                PriorityGames(gameId = 58, numberOfRounds = 20, priority = 2),
                // Add more PriorityGames objects as needed
            )
        )
        return my_api.postPonuda(request)
    }

}