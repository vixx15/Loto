package com.example.loto

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.loto.api.NetworkClient
import com.example.loto.dto.MyNumber
import com.example.loto.dto.responseOffers.AllOffers
import com.example.loto.dto.responseOffers.LottoOffer
import com.example.loto.dto.responseOffers.Offer
import com.example.loto.expandedList.Base
import com.example.loto.expandedList.Child
import com.example.loto.expandedList.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class OffersViewModel : ViewModel() {

    private var allOffers: AllOffers? = null

    private var _tabSelected = mutableStateOf(0)
    val tabSelected: MutableState<Int> = _tabSelected

    private val _content = mutableStateListOf<Base>()
    val content: SnapshotStateList<Base> = _content

    private val myScope = CoroutineScope(Dispatchers.IO)

    var selectedLottoOffer: LottoOffer = LottoOffer(name = "Ponudaaa")

    var numbersList = ArrayList<MyNumber>()

    init {
        getOfferData()
        content.clear()
    }

    fun getListOfNumbers(numbers: Int) {
        numbersList.clear()
        for (i in 1..numbers) {
            numbersList.add(MyNumber(i,false))
        }
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
                    var threeKids = ArrayList<Child>()
                    var header = Header(offer, children, threeKids)

                    for (lottoOffer in offer.lottoOffer) {
                        children.add(Child(lottoOffer, 0, header))
                    }
                    header.children = children
                    content.add(header)
                    //content.add(Header(offer, children, threeKids))

                }
            }
        }
    }

    fun updateContentBasedOnTab() {
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
                var threeKids = ArrayList<Child>()
                var header = Header(offer, children, threeKids)

                for (lottoOffer in offer.lottoOffer) {
                    children.add(Child(lottoOffer, 0, header))
                }
                header.children = children
                content.add(header)
                //content.add(Header(offer, children, threeKids))
            }
        }
    }
    /*fun getTimeLeft(lottoOffer: LottoOffer): String {
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
    }*/

    fun formatRemainingTime(remainingTime: Long): String {

        val HH = TimeUnit.MILLISECONDS.toHours(remainingTime)
        val mm = TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60
        val ss = TimeUnit.MILLISECONDS.toSeconds(remainingTime) % 60


        var formattedString = ""
        formattedString = if (HH < 1)
            String.format("%02d:%02d", mm, ss)
        else
            String.format("%02d:%02d:%02d", HH, mm, ss)
        return formattedString

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

    @Composable
    fun getRemainingTimeMillis(lottoOffer: LottoOffer): State<Long> {
        val remainingTime = remember { mutableStateOf(0L) }

        LaunchedEffect(true) {
            val timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    val now = Calendar.getInstance().timeInMillis
                    val draftTime = lottoOffer.time ?: 0
                    val allowedTillDraft = lottoOffer.allowBetTimeBefore?.times(1000) ?: 0
                    if (draftTime == 0L || allowedTillDraft == 0)
                        return

                    val result = draftTime - now - allowedTillDraft


                    if (result <= 0L) {
                        cancel()
                        remainingTime.value = 0
                    } else remainingTime.value = result

                }

            }
            timer.scheduleAtFixedRate(timerTask, 0, 1000)

        }
        return remainingTime
    }
}


