package com.example.loto.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.loto.OffersViewModel
import com.example.loto.Screens
import com.example.loto.dto.MyNumber
import com.example.loto.dto.responseOffers.LottoOffer
import java.text.SimpleDateFormat


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun OfferDetailScreen(viewModel: OffersViewModel, navController: NavHostController) {

    val clickedNumbers = remember { viewModel.clickedNumbers }

    var length = viewModel.selectedLottoOffer.name?.length
    var gameNumbers = 0

    if (length != null) {
        gameNumbers =
            viewModel.selectedLottoOffer.name?.substring(length - 3, length - 1)?.toInt() ?: 0
    }

    viewModel.getListOfNumbers(gameNumbers)

    val layoutDirection = LocalLayoutDirection.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val itemSize = remember(screenWidth, layoutDirection) {
        val columns = 10
        val itemWidth = screenWidth / columns
        itemWidth
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = { navController.navigate(Screens.TicketScreen.route) },
            shape = RoundedCornerShape(25.dp),
            backgroundColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.width(150.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Moj Broj", color = Color.Black)
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .background(Color(0xFF872E9E), CircleShape)
                        .border(
                            2.dp,
                            Color(0xFF872E9E),
                            CircleShape
                        )
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = clickedNumbers.size.toString(), color = Color.White)
                }
            }
        }
    }, floatingActionButtonPosition = FabPosition.Center) {
        Column {
            TimeAndKoloView(selectedOffer = viewModel.selectedLottoOffer)
            RandomSelectionView(viewModel = viewModel, onClickButtonRandom =
            { viewModel.getRandomNumbers() }

            )
            LazyVerticalGrid(columns = GridCells.Fixed(10)) {

                itemsIndexed(viewModel.numbersList) { index, item ->
                    StyledGridItem(item = item, viewModel = viewModel, onClickNumber = {
                        item.clicked = !item.clicked
                        if (item.clicked) {
                            if (clickedNumbers.size < 8) clickedNumbers.add(item) else
                                item.clicked = false
                        } else
                            clickedNumbers.remove(item)
                    }, itemSize)
                }

            }
        }
    }


}

@Composable
fun StyledGridItem(
    item: MyNumber,
    viewModel: OffersViewModel,
    onClickNumber: () -> Unit,
    itemSize: Dp
) {

    Box(
        modifier = Modifier
            .size(itemSize)
            .border(0.5.dp, MaterialTheme.colorScheme.onTertiary)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp) // Add padding
            .fillMaxSize()
            .clickable(onClick = onClickNumber)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.clickedNumbers.contains(item)) {
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .background(Color.Transparent)
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

        } else {
            Text(
                text = item.number.toString(),

                fontSize = 14.sp, // Increase font size
                fontWeight = FontWeight.Bold, // Make the text bold
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RandomSelectionView(viewModel: OffersViewModel, onClickButtonRandom: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(10.dp)
            .height(70.dp)
    ) {
        Button(onClick = onClickButtonRandom) {
            Text(text = "Slucajan Odabir")
        }

        DropDownMenuView(viewModel = viewModel)
    }


}

@Composable
fun DropDownMenuView(viewModel: OffersViewModel) {
    val items = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex = remember { viewModel.randomNumbersCounter }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Brojeva:")
            Text(text = items[selectedIndex.value].toString())
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "More"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .height(200.dp)
                .width(45.dp)
        ) {
            items.forEachIndexed { index, num ->
                DropdownMenuItem(
                    text = { Text(text = num.toString()) },
                    onClick = {
                        selectedIndex.value = index
                        expanded = false
                    })
            }
        }
    }
}

@Composable
fun TimeAndKoloView(selectedOffer: LottoOffer) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF872E9E)
            )
            .height(70.dp)
            .padding(10.dp)
    ) {
        Row {
            Text(
                text = "Vreme izvlacenja:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp),
                color = Color.White,

                )
            Text(
                text = SimpleDateFormat("HH:mm").format(selectedOffer.time),
                modifier = Modifier.padding(4.dp),
                color = Color.White
            )
        }
        Divider(
            modifier = Modifier
                .height(30.dp)
                .width(1.dp)
        )
        Row {
            Text(
                text = "Kolo:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp),
                color = Color.White
            )
            Text(
                text = selectedOffer.eventId.toString(),
                modifier = Modifier.padding(4.dp),
                color = Color.White
            )
        }
    }

}

