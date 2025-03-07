package com.jop.ngaji.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jop.ngaji.presentation.detailSurah.view.DetailSurahScreen
import com.jop.ngaji.presentation.detailSurah.viewModel.DetailSurahViewModel
import com.jop.ngaji.presentation.home.view.HomeScreen
import com.jop.ngaji.presentation.surah.view.SurahScreen
import com.jop.ngaji.presentation.surah.viewModel.SurahViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoute(
    modifier: Modifier,
    navController: NavHostController,
    showBottomBar: (Boolean) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.HOME
    ){
        composable(route = Route.HOME){
            showBottomBar(true)
            HomeScreen(navHostController = navController)
        }

        composable(route = Route.SURAH){
            val viewModel: SurahViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            showBottomBar(true)
            SurahScreen(navHostController = navController, state)
        }

        composable(
            route = Route.SURAH.plus("?number={surahNumber}"),
            arguments = listOf(navArgument("surahNumber"){
                type = NavType.IntType
            })
        ){ navBackStackEntry ->
            val surahNumber = navBackStackEntry.arguments?.getInt("surahNumber")
            val viewModel: DetailSurahViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            showBottomBar(false)
            DetailSurahScreen(navHostController = navController, surahNumber ?: 0, state, viewModel::onEvent)
        }
    }
}