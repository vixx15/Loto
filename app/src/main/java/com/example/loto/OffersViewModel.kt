package com.example.loto

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.loto.api.NetworkClient
import com.example.loto.dto.responseOffers.AllOffers
import com.example.loto.dto.responseOffers.Offer
import com.example.loto.expandedList.Base
import com.example.loto.expandedList.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class OffersViewModel : ViewModel() {

    private var allOffers: AllOffers? = null
    private var tabSelected = mutableIntStateOf(0)


    private val _content = mutableStateListOf<Base>()
    val content: SnapshotStateList<Base> = _content

    private val myScope = CoroutineScope(Dispatchers.IO)

    init {
        getOfferData()
        content.clear()
    }

    fun getOfferData() {

        myScope.launch {
            allOffers = fetchOfferDataAsync()
            //iz all offers u content spakovati


            content.clear()
            var selectedOffer: ArrayList<Offer>? = null
            if (tabSelected.value == 0) {
                selectedOffer = allOffers?.priorityLottoOffer
            } else if (tabSelected.value == 1) {
                selectedOffer = allOffers?.offerNextNHours
            } else {
                selectedOffer = allOffers?.completeOffer
            }

            if (selectedOffer != null) {
                for (offer in selectedOffer) {
                    content.add(Header(offer))
                }
            }
        }
    }

    suspend fun fetchOfferDataAsync(): AllOffers? {

        try {
            val response = NetworkClient.getData().execute()

            if (response.isSuccessful) return response.body()
            else
                Log.e("ERR", "Unsuccessfull response")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}


