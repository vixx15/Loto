package com.example.loto.dto.responseOffers

import com.google.gson.annotations.SerializedName


data class LottoOfferDetailed (

    @SerializedName("eventId"            ) var eventId            : Int?                 = null,
    @SerializedName("gameId"             ) var gameId             : Int?                 = null,
    @SerializedName("name"               ) var name               : String?              = null,
    @SerializedName("time"               ) var time               : Long?                 = null,
    @SerializedName("countryId"          ) var countryId          : Int?                 = null,
    @SerializedName("extraRoundCode"     ) var extraRoundCode     : String?              = null,
    @SerializedName("ballsTotalNumber"   ) var ballsTotalNumber   : Int?                 = null,
    @SerializedName("ballsToDraw"        ) var ballsToDraw        : Int?                 = null,
    @SerializedName("ballsToBet"         ) var ballsToBet         : Int?                 = null,
    @SerializedName("ballsToPick"        ) var ballsToPick        : Int?                 = null,
    @SerializedName("allowBetTimeBefore" ) var allowBetTimeBefore : Int?                 = null,
    @SerializedName("oddValues"          ) var oddValues          : ArrayList<OddValues> = arrayListOf(),
    @SerializedName("winerNumbers"       ) var winerNumbers       : ArrayList<String>    = arrayListOf(),
    @SerializedName("priority"           ) var priority           : Int?                 = null

)