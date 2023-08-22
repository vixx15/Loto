package com.example.loto.expandedList

import com.example.loto.dto.responseOffers.Offer

class Header(val offer: Offer, val children:ArrayList<Child>,var expanded: Boolean =false) : Base(0) {




}

