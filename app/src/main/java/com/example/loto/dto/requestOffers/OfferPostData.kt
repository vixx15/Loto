package com.example.loto.dto.requestOffers

import com.google.gson.annotations.SerializedName

data class OfferPostData (

    @SerializedName("countryId"     ) var countryId     : Int?                     = null,
    @SerializedName("cutoffHours"   ) var cutoffHours   : Int?                     = null,
    @SerializedName("languageId"    ) var languageId    : Int?                     = null,
    @SerializedName("priorityGames" ) var priorityGames : ArrayList<PriorityGames> = arrayListOf()
)