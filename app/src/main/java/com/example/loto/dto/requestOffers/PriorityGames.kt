package com.example.loto.dto.requestOffers

import com.google.gson.annotations.SerializedName

data class PriorityGames (

    @SerializedName("gameId"         ) var gameId         : Int? = null,
    @SerializedName("numberOfRounds" ) var numberOfRounds : Int? = null,
    @SerializedName("priority"       ) var priority       : Int? = null

)