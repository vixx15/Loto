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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
        TicketPaymentInfoView(viewModel = viewModel)
        Divider (modifier = Modifier
            .width(500.dp)
            .height(2.dp))
        InputMoneyAmountView(viewModel = viewModel)
    }
}

@Composable
fun TicketPaymentInfoView(viewModel: OffersViewModel) {


    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Uplata", fontWeight = FontWeight.Bold,)
            Text(text = viewModel.moneyInput.value)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Kvota", fontWeight = FontWeight.Bold)
            Text(text = (viewModel.selectedOfferDetailed.value.oddValues[viewModel.clickedNumbers.size - 1].value).toString())
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Isplata", fontWeight = FontWeight.Bold)
            Text(text = viewModel.getMaksimalanDobitak())
        }

    }


}

@Composable
fun InputMoneyAmountView(viewModel: OffersViewModel) {
    val text = remember { viewModel.moneyInput }

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text.value,
            onValueChange = { newText -> text.value = newText },
            label = {
                Text(
                    text = "Novac za uplatu"
                )
            },
            placeholder = { Text(text = "0.00") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.width(150.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4CAF50),
                contentColor = Color.White
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Uplati",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(text = "Dobitak: " + viewModel.getMaksimalanDobitak(), color = Color.White)
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