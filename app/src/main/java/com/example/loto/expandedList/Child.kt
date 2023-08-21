package com.example.loto.expandedList

import com.example.loto.dto.responseOffers.LottoOffer

class Child:Base(1) {




    var lottoOffer:LottoOffer
        get(){
            return LottoOffer()
        }
        set(lottoOffer)
        {
            this.lottoOffer=lottoOffer
        }
}