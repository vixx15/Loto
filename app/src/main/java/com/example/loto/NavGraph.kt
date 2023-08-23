package com.example.loto

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavGraph(navController: NavHostController, viewModel: OffersViewModel= androidx.lifecycle.viewmodel.compose.viewModel()){

    NavHost(navController = navController, startDestination = Screens.MainScreen.route){
        composable(route = Screens.MainScreen.route){
            MainScreen(
                viewModel=viewModel,
                navHostController =navController
            )
        }

        composable(
            route=Screens.OfferDetailScreen.route
        ){
          OfferDetailScreen(viewModel = viewModel)
        }

    }
}


