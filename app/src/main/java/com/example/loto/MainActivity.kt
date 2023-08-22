package com.example.loto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loto.expandedList.Child
import com.example.loto.expandedList.Header
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


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
    val items = remember {
        viewModel.content
    }
    Calendar.getInstance().timeInMillis
    Scaffold(topBar = { topBar() }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            itemsIndexed(items) { index, item ->

                if (item.type == 0) {
                    HeaderView(
                        countryOffer = (item as Header).offer,
                        onClickItem = {
                            item.expanded = !item.expanded
                            if (item.expanded) {
                                for (child in item.children) {
                                    viewModel.content.add(index + 1, child)
                                }

                            }
                            if (!item.expanded && (items[index + 1] is Child)) {

                                viewModel.content.removeRange(
                                    index + 1,
                                    index + item.children.size + 1
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
                    ChildView(item = item, viewModel = viewModel)
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