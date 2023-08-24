package com.example.loto.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loto.OffersViewModel

import com.example.loto.dto.responseOffers.LottoOffer
import com.example.loto.dto.responseOffers.Offer
import com.example.loto.expandedList.Base
import com.example.loto.expandedList.Child

import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun HeaderView(countryOffer: Offer, onClickOffer: () -> Unit) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        Column {
            countryOffer.gameName?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 8.dp, end = 8.dp)
                        .clickable(onClick = onClickOffer),
                    fontSize = 22.sp
                )
            }

        }
    }
}

@Composable
fun ChildView(item: Base, viewModel: OffersViewModel, onChildClick:()->Unit) {

    val remainingTime by viewModel.getRemainingTimeMillis((item as Child).lottoOffer)


    Row(
        modifier = Modifier
            .fillMaxWidth().clickable(onClick = onChildClick),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = (item as Child).lottoOffer.time?.let {
                SimpleDateFormat("HH:mm").format(Date(it))
            } ?: "N/A", Modifier.padding(10.dp))

        Text(
            //text = viewModel.getTimeLeft((item as Child).lottoOffer),
            text = viewModel.formatRemainingTime(remainingTime),
            Modifier.padding(10.dp)
        )
    }
}

@Composable
fun ChildViewLabels() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Vreme Izvlacenja",
            Modifier.padding(10.dp),
            fontSize = 16.sp,

            )
        Text(
            text = "Preostalo za uplatu",
            Modifier.padding(10.dp),
            fontSize = 16.sp,

            )
    }

}

@Composable
fun ThreeDotsView(item: Base, index: Int, viewModel: OffersViewModel) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "...", modifier = Modifier
                .fillMaxHeight()
                .padding(start = 8.dp, end = 8.dp)
                .clickable(onClick = {
                    var father = (item as Child).father

                    for (i in 0..2) {
                        if (father.threeByThree.size >= father.children.size)
                            break

                        var newElement =
                            father.children.get(father.threeByThree.size)

                        viewModel.content.add(
                            index + 1,
                            newElement
                        )
                        father.threeByThree.add(newElement)

                    }
                }),
            fontSize = 30.sp
        )
    }
}

@Composable
fun OfferHeader(offerName: String, onClickItem: () -> Unit) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = onClickItem
            )
            .padding(8.dp)
    ) {
        Text(
            text = "",
            fontSize = 17.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ExpandableView(currentLottoOffers: List<LottoOffer>, isExpanded: Boolean) {
    val expandTranstion = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))
    }

    val colapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }


    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTranstion,
        exit = colapseTransition
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(15.dp)
        ) {
            Row {
                Text(
                    text = "Vreme Izvlacenja", fontSize = 16.sp,
                    color = Color.White,
                )
                Text(
                    text = "Preostalo za uplatu", fontSize = 16.sp,
                    color = Color.White,
                )
            }
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(3) { index ->
                    ListItem(currentLottoOffers.get(index))
                }
            }

        }
    }
}

@Composable
fun ListItem(currentOfferDisplayed: LottoOffer) {
    Row {

        Text(text = SimpleDateFormat("mm:ss").format(currentOfferDisplayed.time?.let { Date(it) }))
        Text(text = currentOfferDisplayed.allowBetTimeBefore.toString())
    }

}