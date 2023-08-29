package com.example.loto.dto


class CalculationResult {
    var brutoPayin = 0.0 //bruto uplata
    var payinTax = 0.0 //porez na uplati (pare)
    var quota = 0.0 //kvota
    var win = 0.0 //dobitak bez bonusa
    var bonus = 0.0 //bonus - suma bonusa
    var bonusPercent = 0.0 //procenat bonusa koji vraca javascript
    var payoutTax = 0.0 //porez na isplati (pare)
    var payout = 0.0 //isplata koja ide korisniku (neto)
    var combinations = 0 //broj kombinacija
    var rowNumber = 0 //broj kuglica
    var areAnyRowsStarted = false
    var ticketSplitCount = 0
    var ticketSplitAmount = 0.0
    var selectedSystems: List<Int> = ArrayList() //selektovani sistemi
    var fixes = 0
    var freebetType: String? = null
}