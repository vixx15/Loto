package com.example.loto.dto.responseOffers

import com.google.gson.annotations.SerializedName


data class LottoOffer (

  @SerializedName("eventId"            ) var eventId            : Int?    = null,
  @SerializedName("gameId"             ) var gameId             : Int?    = null,
  @SerializedName("name"               ) var name               : String? = null,
  @SerializedName("time"               ) var time               : Long?    = null,
  @SerializedName("allowBetTimeBefore" ) var allowBetTimeBefore : Int?    = null

)