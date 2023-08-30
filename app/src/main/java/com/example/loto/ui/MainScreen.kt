package com.example.loto.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AppBarDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loto.OffersViewModel
import com.example.loto.R
import com.example.loto.Screens
import com.example.loto.expandedList.Child
import com.example.loto.expandedList.Header
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: OffersViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navHostController: NavHostController
) {
    val items = remember {
        viewModel.content
    }
    Calendar.getInstance().timeInMillis
    Scaffold(topBar = { TopBar() }, bottomBar = { BottomBar(viewModel) }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            itemsIndexed(items) { index, item ->

                if (item.type == 0) {
                    HeaderView(
                        countryOffer = (item as Header).offer,
                        onClickOffer = {
                            item.expanded = !item.expanded
                            if (item.expanded) {
                                item.threeByThree.clear()
                                for (i in 0..2) {
                                    if (i <= item.children.size - 1) {
                                        viewModel.content.add(index + 1 + i, item.children[i])
                                        item.threeByThree.add(item.children[i])
                                    }
                                }

                            }
                            if (!item.expanded && (items[index + 1] is Child)) {

                                viewModel.content.removeRange(
                                    index + 1,
                                    index + item.threeByThree.size + 1
                                )

                            }
                        })
                    Divider(color = Color.Black)
                }


                if (item.type == 1) {
                    if (items[index - 1].type == 0) {
                        ChildViewLabels()
                    }
                    ChildView(item = item, viewModel = viewModel, onChildClick = {
                        viewModel.selectedLottoOffer.value = (item as Child).lottoOffer
                        item.lottoOffer.gameId?.let {
                            item.lottoOffer.eventId?.let { it1 ->
                                viewModel.getDetailedOffer(
                                    it,
                                    it1
                                )
                            }
                        }
                        viewModel.clickedNumbers.clear()
                        navHostController.navigate(Screens.OfferDetailScreen.route)

                    })

                    if (index == items.size - 1 || items[index + 1].type == 0) {
                        ThreeDotsView(item = item, index = index, viewModel = viewModel)
                    }
                }


            }


        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val secondaryColor = MaterialTheme.colorScheme.secondary
    TopAppBar(
        title = { Text(text = "Moj broj", color = secondaryColor) },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.logo_image),
                contentDescription = "Logo top bar",
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
            )
        }

    )

}

@Composable
fun BottomBar(viewModel: OffersViewModel) {
    var selected by viewModel.tabSelected

    LaunchedEffect(selected) {
        viewModel.updateContentBasedOnTab()
    }

    BottomAppBar(
        Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { selected = 0 }
                    .background(if (selected == 0) Color(0xFFC694D3) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Brzze igre",
                    fontWeight = if (selected == 0) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected == 0) Color.White else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { selected = 1 }
                    .background(if (selected == 1) Color(0xFFC694D3) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sledeca 24 sata",
                    fontWeight = if (selected == 1) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected == 1) Color.White else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { selected = 2 }
                    .background(if (selected == 2) Color(0xFFC694D3) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Svi dani",
                    fontWeight = if (selected == 2) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected == 2) Color.White else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp) // Adjust padding as needed
                )
            }
        }
    }

}
