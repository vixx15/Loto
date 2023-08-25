package com.example.loto.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loto.OffersViewModel
import com.example.loto.dto.MyNumber


@Composable
fun TicketScreen(viewModel: OffersViewModel, navController: NavHostController) {
    Column {


        viewModel.selectedOfferDetailed.value.name?.let { TicketTitleView(gameName = it) }
        TimeAndKoloView(selectedOffer = viewModel.selectedLottoOffer)
        SelectedNumbersView(viewModel = viewModel)
        InputMoneyAmountView(viewModel = viewModel)
    }
}


@Composable
fun InputMoneyAmountView(viewModel: OffersViewModel) {
    var text = remember { viewModel.moneyInput }

    Row {
        OutlinedTextField(
            value = text.value,
            onValueChange = { newText -> text.value = newText },
            label = {
                Text(
                    text = "Novac za uplatu"
                )
            },
            placeholder = { Text(text = "0.00") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Button(onClick = { /*TODO*/ }) {
            Column {
                Text(text = "Uplati")
              //  Text(text = "Maksimalan dobitak: " + viewModel.getMaksimalanDobitak())
            }
        }
    }

}

@Composable
fun SelectedNumbersView(viewModel: OffersViewModel) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.height(140.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        itemsIndexed(viewModel.clickedNumbers) { index, item ->
            BallView(item = item, viewModel = viewModel)
        }
    }
}

@Composable
fun BallView(item: MyNumber, viewModel: OffersViewModel) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(Color.Transparent)
            .padding(4.dp)
            .border(
                2.dp,
                viewModel.getColor(item.number)
                    ?: MaterialTheme.colorScheme.onPrimaryContainer,
                CircleShape
            )
            .aspectRatio(1f),
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

@Composable
fun TicketTitleView(gameName: String) {

    Row(
        // horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFC694D3)
            )
            .height(70.dp)
            .padding(10.dp)
    ) {

        Text(
            text = "Moj broj - ",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp),
            color = Color.White,

            )
        Text(
            text = gameName,
            modifier = Modifier.padding(4.dp),
            color = Color.White
        )


    }
}