package com.example.loto.dto.responseOffers

import com.google.gson.annotations.SerializedName

data class OddValues (

    @SerializedName("ballNumber" ) var ballNumber : Int?    = null,
    @SerializedName("value"      ) var value      : Double? = null

)