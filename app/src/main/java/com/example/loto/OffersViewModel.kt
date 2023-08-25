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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.loto.api.NetworkClient
import com.example.loto.dto.MyNumber
import com.example.loto.dto.responseOffers.AllOffers
import com.example.loto.dto.responseOffers.LottoOffer
import com.example.loto.dto.responseOffers.LottoOfferDetailed
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
    private var _selectedDetailedOffer = mutableStateOf(LottoOfferDetailed())
    var selectedOfferDetailed: MutableState<LottoOfferDetailed> = _selectedDetailedOffer

    var numbersList = ArrayList<MyNumber>()

    private val _clickedNumbers = mutableStateListOf<MyNumber>()
    val clickedNumbers: SnapshotStateList<MyNumber> = _clickedNumbers

    private var _randomNumbersCounter = mutableStateOf(1)
    var randomNumbersCounter: MutableState<Int> = _randomNumbersCounter

    private var _moneyInput = mutableStateOf(String())
    var moneyInput: MutableState<String> = _moneyInput

    init {
        getOfferData()
        content.clear()
    }

    fun getListOfNumbers(numbers: Int) {
        numbersList.clear()
        for (i in 1..numbers) {
            numbersList.add(MyNumber(i, false))
        }
    }

    fun getRandomNumbers() {
        clickedNumbers.clear()
        while (clickedNumbers.size <= randomNumbersCounter.value) {
            val num = numbersList.random()
            if (!clickedNumbers.contains(num)) {
                num.clicked = true
                clickedNumbers.add(num)
            }
        }
    }

    fun getDetailedOffer(gameId: Int, eventId: Int) {
        myScope.launch {
            val response = fetchLottoOfferDetails(gameId, eventId)
            if (response != null)
                selectedOfferDetailed.value =
                    response

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

    suspend fun fetchLottoOfferDetails(gameId: Int, eventId: Int): LottoOfferDetailed? {
        try {
            val response = NetworkClient.getDetailedLottoOffer(1, gameId, eventId).execute()
            if (response.isSuccessful) return response.body() else
                Log.e("ErRRRRR", "Unsuccessful response")

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
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

    fun getColor(number: Int): Color? {
        val differentColors = mapOf(
            0 to Color(0xFFB39DDB), // Lavender
            1 to Color(0xFF90CAF9), // Light Blue
            2 to Color(0xFF81C784), // Light Green
            3 to Color(0xFFFFCC80), // Peach
            4 to Color(0xFFFFAB91), // Light Salmon
            5 to Color(0xFFFFD54F), // Pale Yellow
            6 to Color(0xFFCE93D8), // Pale Violet
            7 to Color(0xFF80CBC4), // Turquoise
            8 to Color(0xFFEF9A9A), // Pale Pink
            9 to Color(0xFFA5D6A7)

        )
        return differentColors[number / 10]
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

    fun getMaksimalanDobitak(): String {

        var uneto = moneyInput.value.toFloatOrNull()
        if (uneto == null)
            uneto = 0.0f
        return (uneto * selectedOfferDetailed.value.oddValues[clickedNumbers.size - 1].value!!).toString()
    }
}


