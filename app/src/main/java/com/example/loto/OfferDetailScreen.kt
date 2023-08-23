package com.example.loto

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loto.dto.MyNumber

@Composable
fun OfferDetailScreen(viewModel: OffersViewModel) {

    var length = viewModel.selectedLottoOffer.name?.length
    var gameNumbers = 0
    if (length != null) {
        gameNumbers =
            viewModel.selectedLottoOffer.name?.substring(length - 3, length - 1)?.toInt() ?: 0
    }

    viewModel.getListOfNumbers(gameNumbers)

    LazyVerticalGrid(columns = GridCells.Fixed(10)) {
        itemsIndexed(viewModel.numbersList) { index, item ->
            StyledGridItem(item = item, onClickNumber = {
                item.clicked = true
            })
        }

    }


}

@Composable
fun StyledGridItem(item: MyNumber, onClickNumber: () -> Unit) {
    Box(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp) // Add padding
            .fillMaxSize().clickable(onClick = onClickNumber),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item.number.toString(),

            fontSize = 14.sp, // Increase font size
            fontWeight = FontWeight.Bold, // Make the text bold
            textAlign = TextAlign.Center
        )
    }
}