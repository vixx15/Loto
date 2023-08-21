package com.example.loto.expandedList

import com.example.loto.dto.responseOffers.Offer

class Header(val offer: Offer) : Base(0) {


    var expanded: Boolean
        get() {
            return expanded
        }
        set(expanded) {
            this.expanded = expanded
        }

}

