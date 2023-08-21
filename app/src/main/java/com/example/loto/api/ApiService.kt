package com.example.loto.api

import com.example.loto.dto.requestOffers.OfferPostData
import com.example.loto.dto.responseOffers.AllOffers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-type:application/json;charset=UTF-8")
    @POST("/MozzartWS/external.json/lotto-offer-complete")
    fun postPonuda(@Body offerData: OfferPostData?): Call<AllOffers>
}