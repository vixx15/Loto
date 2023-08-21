package com.example.loto.dto.responseOffers

import com.google.gson.annotations.SerializedName


data class AllOffers (

  @SerializedName("priorityLottoOffer" ) var priorityLottoOffer : ArrayList<Offer> = arrayListOf(),
  @SerializedName("offerNextNHours"    ) var offerNextNHours    : ArrayList<Offer>    = arrayListOf(),
  @SerializedName("completeOffer"      ) var completeOffer      : ArrayList<Offer>      = arrayListOf()

)