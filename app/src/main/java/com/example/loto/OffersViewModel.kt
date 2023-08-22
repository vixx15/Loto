package com.example.loto

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.loto.api.NetworkClient
import com.example.loto.dto.responseOffers.AllOffers
import com.example.loto.dto.responseOffers.LottoOffer
import com.example.loto.dto.responseOffers.Offer
import com.example.loto.expandedList.Base
import com.example.loto.expandedList.Child
import com.example.loto.expandedList.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.Calendar
import java.util.concurrent.TimeUnit

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
                    var children = ArrayList<Child>()


                    for (lottoOffer in offer.lottoOffer) {
                        children.add(Child(lottoOffer))
                    }
                    content.add(Header(offer, children))
                }
            }
        }
    }

    fun getTimeLeft(lottoOffer: LottoOffer): String {
        val now = Calendar.getInstance().timeInMillis
        val draftTime = lottoOffer.time ?: 0
        val allowedTillDraft = lottoOffer.allowBetTimeBefore?.times(1000) ?: 0

        if (draftTime == 0L || allowedTillDraft == 0)
            return "Not found"

        val result = draftTime - now - allowedTillDraft

        val mm = TimeUnit.MILLISECONDS.toMinutes(result)
        val ss = TimeUnit.MILLISECONDS.toSeconds(result) % 60

        val formatedString = String.format("%02d:%02d", mm, ss)
        return formatedString
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


