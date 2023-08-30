package com.example.loto

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.loto.api.NetworkClient
import com.example.loto.dto.CalculationResult
import com.example.loto.dto.MyNumber
import com.example.loto.dto.MySystemTicketSelector
import com.example.loto.dto.responseOffers.AllOffers
import com.example.loto.dto.responseOffers.LottoOffer
import com.example.loto.dto.responseOffers.LottoOfferDetailed
import com.example.loto.dto.responseOffers.OddValues
import com.example.loto.dto.responseOffers.Offer
import com.example.loto.expandedList.Base
import com.example.loto.expandedList.Child
import com.example.loto.expandedList.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.format
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

    private var _ticketTypeSelected = mutableStateOf(0)
    val ticketTypeSelected: MutableState<Int> = _ticketTypeSelected

    private val _selectedSystems = mutableStateListOf<MySystemTicketSelector>()
    val selectedSystems: SnapshotStateList<MySystemTicketSelector> = _selectedSystems

    private var _numberOfCheckedSystems = mutableStateOf(0)
    var numberOfCheckedSystems: State<Int> = _numberOfCheckedSystems

    private var _selectedSystemsNumbers = mutableStateListOf<Int>()
    val selectedSystemsNumbers: SnapshotStateList<Int> = _selectedSystemsNumbers

    private val _remainingTime = mutableStateOf(0L)
    var remainingTime: State<Long> = _remainingTime

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
        val differentColors = when {


            number <= 10 -> Color(0xFFB39DDB) // Lavender
            number <= 20 -> Color(0xFF90CAF9)// Light Blue
            number <= 30 -> Color(0xFF81C784) // Light Green
            number <= 40 -> Color(0xFFFFCC80) // Peach
            number <= 50 -> Color(0xFFFFAB91)// Light Salmon
            number <= 60 -> Color(0xFFFFD54F)// Pale Yellow
            number <= 70 -> Color(0xFFCE93D8)// Pale Violet
            number <= 80 -> Color(0xFF80CBC4)// Turquoise
            number <= 90 -> Color(0xFFEF9A9A)// Pale Pink
            else -> Color(0xFFA5D6A7)

        }
        return differentColors
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

    fun updateRemainingTime() {
        val now = Calendar.getInstance().timeInMillis
        val draftTime = selectedLottoOffer.time ?: 0
        val allowedTillDraft = selectedLottoOffer.allowBetTimeBefore?.times(1000) ?: 0
        if (draftTime == 0L || allowedTillDraft == 0)
            return

        _remainingTime.value = draftTime - now - allowedTillDraft
    }

    fun handleExpiredOffer() {

        var nextDraft = getNextDraft(selectedLottoOffer)

        if (nextDraft != null)
            selectedLottoOffer = nextDraft
        else {
            selectedLottoOffer = LottoOffer()
        }

    }

    fun getMaksimalanDobitak(): String {

        var uneto = moneyInput.value.toFloatOrNull()
        if (uneto == null)
            uneto = 0.0f

        var result = 0.0
        if (selectedOfferDetailed.value.oddValues[clickedNumbers.size - 1].value != null)
            result = uneto * selectedOfferDetailed.value.oddValues[clickedNumbers.size - 1].value!!
        if (result > 10000000)
            result = 10000000.0



        return String.format("%.02f", (result))
    }

    fun prepareSystemTicketOptions() {
        selectedSystems.clear()
        for (number in 1..clickedNumbers.size) {
            selectedSystems.add(
                MySystemTicketSelector(
                    number.toString() + "/" + clickedNumbers.size,
                    false
                )
            )
        }
    }

    fun calcCombinationNumber(k: Int, n: Int): Int {
        var factorial: Long = 1
        val biggerFactor = if (n - k > k) n - k else k
        val smallerFactor = n - biggerFactor
        for (i in n downTo biggerFactor + 1) {
            factorial *= i.toLong()
        }
        for (i in smallerFactor downTo 2) {
            factorial /= i.toLong()
        }
        return factorial.toInt()
    }

    fun getNumberOfCombinations(numberOfBalls: Int, ballsToDraw: Long, subsystem: Int): Int {
        var n1 = numberOfBalls
        try {
            n1 = Math.min(numberOfBalls, ballsToDraw.toInt())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            //CrashlyticsWrapper.logException(e)
        }
        return calcCombinationNumber(subsystem, n1)
    }

    public fun getMaxPotentialPayment(
        ticketGame: LottoOfferDetailed,
        numberOfFixes: Int,
        numbers: List<Int?>,
        subsystems: List<Int>,
        payinNeto: Double, result: CalculationResult?
    ): Double {
        val comboPayin: Double = payinNeto /
                calculateNumberOfCombinations(numbers, subsystems, numberOfFixes)
        val oddValues: List<OddValues> = ticketGame.oddValues
        var total = 0.0
        for (subsystem in subsystems) {
            val winningCombinations: Int = getNumberOfCombinations(
                numbers.size - numberOfFixes,
                ticketGame.ballsToDraw?.toLong() ?: 0,
                subsystem
            )
            val oddValue: Double =
                findLotoOddValueForBalls((subsystem + numberOfFixes).toLong(), oddValues)
            total += oddValue * comboPayin * winningCombinations
        }
        if (result != null) {
            result.win = total
        }

        return total //bruto
    }

    public fun getFormatedMaxPotentialPayement(): String {

        var result = getMaxPotentialPayment(
            selectedOfferDetailed.value,
            0,
            convertListMyNumbersToListInt(),
            selectedSystemsNumbers,
            moneyInput.value.toDoubleOrNull() ?: 0.0,
            null
        )

        if (result > 10000000)
            result = 10000000.0
        var df = DecimalFormat("#.##")
        var string = df.format(result).toString()
        return string
    }

    fun findLotoOddValueForBalls(winBallsCount: Long, oddValues: List<OddValues?>): Double {
        for (ov in oddValues) {
            if (ov?.ballNumber?.toLong() === winBallsCount) if (ov != null) {
                return ov.value ?: 0.0
            }
        }
        return 0.0
    }

    fun convertListMyNumbersToListInt(): ArrayList<Int> {
        var brojevi = ArrayList<Int>()
        for (number in clickedNumbers) {
            brojevi.add(number.number)
        }
        return brojevi
    }


    fun calculateNumberOfCombinations(
        numbers: List<Int?>,
        subsystems: List<Int?>,
        inFix: Int
    ): Int {
        if (numbers.isEmpty()) {
            return 0
        }
        var total = 0
        val size = numbers.size - inFix
        for (subsystem in subsystems) {
            total += calcCombinationNumber(subsystem!!, size)
        }
        return total
    }

    fun getNumberOfCheckedBoxes() {
        var n = 0
        for (i in selectedSystems) {
            if (i.checked)
                n++

        }
        _numberOfCheckedSystems.value = n
        numberOfCheckedSystems = _numberOfCheckedSystems
    }

    fun getNextDraft(selectedLottoOffer: LottoOffer): LottoOffer? {
        var found = false
        for (item in content) {
            if (found && item is Child)
                return item.lottoOffer
            if (item.type == 1 && (item as Child).lottoOffer == selectedLottoOffer) {
                found = true
            }
        }
        return null
    }

    fun formatTime(time: Long): String {
        if (Date(time).day != Date().day) {
            return SimpleDateFormat("d.M.").format(Date(time)) + "\n" + SimpleDateFormat("HH:mm").format(
                Date(time)
            )
        }
        return SimpleDateFormat("HH:mm").format(Date(time))
    }


}


