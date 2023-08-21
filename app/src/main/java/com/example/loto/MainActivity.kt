package com.example.loto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.darkColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loto.expandedList.Base
import com.example.loto.expandedList.Header
import kotlinx.coroutines.flow.collect


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: OffersViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val content by viewModel.content.collectAsState()

    Scaffold (topBar = {topBar()}){
        padding->
        LazyColumn(Modifier.padding(padding)){
            itemsIndexed(content as List<Base>){ index, item->

                if(item.type == 0)
                    ExpandableContainerView(countryOffer = (item as Header).offer, onClickItem = {}, expanded = false )
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Greeting("Android")
    }
}