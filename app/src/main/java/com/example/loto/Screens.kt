package com.example.loto

sealed class Screens(val route:String){
    object MainScreen:Screens("main_screen")
    object OfferDetailScreen:Screens("offer_detail_screen")
}