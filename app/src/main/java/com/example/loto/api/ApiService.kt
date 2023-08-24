package com.example.loto.api

import com.example.loto.dto.requestOffers.OfferPostData
import com.example.loto.dto.responseOffers.AllOffers
import com.example.loto.dto.responseOffers.LottoOfferDetailed
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-type:application/json;charset=UTF-8")
    @POST("/MozzartWS/external.json/lotto-offer-complete")
    fun postPonuda(@Body offerData: OfferPostData?): Call<AllOffers>

    @GET("/MozzartWS/external.json/lotto-event")
    fun getDetailedLottoOffer(
        @Query("languageId") languageId: String,
        @Query("gameId") gameId: String,
        @Query("eventId") eventId: String
    ): Call<LottoOfferDetailed>
}