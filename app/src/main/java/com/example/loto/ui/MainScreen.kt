package com.example.loto.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    Scaffold(topBar = { topBar() }, bottomBar = { bottomBar(viewModel) }) { padding ->
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
                                        viewModel.content.add(index + 1, item.children.get(i))
                                        item.threeByThree.add(item.children.get(i))
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
                        /*Row(
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
                        }*/
                        ChildViewLabels()
                    }
                    ChildView(item = item, viewModel = viewModel, onChildClick = {
                        viewModel.selectedLottoOffer = (item as Child).lottoOffer
                        (item as Child).lottoOffer.gameId?.let {
                            (item as Child).lottoOffer.eventId?.let { it1 ->
                                viewModel.getDetailedOffer(
                                    it,
                                    it1
                                )
                            }
                        }

                        navHostController.navigate(Screens.OfferDetailScreen.route)

                    })
                    /*Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = (item as Child).lottoOffer.time?.let {
                                SimpleDateFormat("HH:mm").format(Date(it))
                            } ?: "N/A", Modifier.padding(10.dp))
                        // Text(text = SimpleDateFormat("mm:ss").format((item as Child).lottoOffer.time?.let { Date(it) }))
                        Text(
                            text = viewModel.getTimeLeft((item as Child).lottoOffer),


                            Modifier.padding(10.dp)
                        )
                    }*/

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
fun topBar() {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
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
fun bottomBar(viewModel: OffersViewModel) {
    var selected by viewModel.tabSelected

    LaunchedEffect(selected) {
        viewModel.updateContentBasedOnTab()
    }

    BottomAppBar(
        Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentPadding = AppBarDefaults.ContentPadding,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Brzze igre", Modifier.clickable { selected = 0 })
            Text(text = "Sledeca 24 sata", Modifier.clickable { selected = 1 })
            Text(text = "Svi dani", Modifier.clickable { selected = 2 })
        }
    }

}
