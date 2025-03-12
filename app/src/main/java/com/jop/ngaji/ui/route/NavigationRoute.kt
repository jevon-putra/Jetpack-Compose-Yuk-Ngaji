package com.jop.ngaji.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jop.ngaji.presentation.detailSurah.view.DetailSurahScreen
import com.jop.ngaji.presentation.detailSurah.viewModel.DetailSurahViewModel
import com.jop.ngaji.presentation.home.view.HomeScreen
import com.jop.ngaji.presentation.home.viewModel.HomeViewModel
import com.jop.ngaji.presentation.prayer.view.PrayerScreen
import com.jop.ngaji.presentation.prayer.viewModel.PrayerViewModel
import com.jop.ngaji.presentation.surah.view.SurahScreen
import com.jop.ngaji.presentation.surah.viewModel.SurahViewModel
import com.jop.ngaji.presentation.surahFavorite.view.SurahFavoriteScreen
import com.jop.ngaji.presentation.surahFavorite.viewModel.SurahFavoriteViewModel
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
            val viewModel: HomeViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            showBottomBar(true)
            HomeScreen(navHostController = navController, state = state, onEvent = viewModel::onEvent)
        }

        composable(route = Route.QURAN){
            val viewModel: SurahViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            showBottomBar(true)
            SurahScreen(navHostController = navController, state = state)
        }

        composable(route = Route.QURAN_FAVORITE){
            val viewModel: SurahFavoriteViewModel = koinViewModel()
            val state = viewModel.getAllSurahFavorite().observeAsState(initial = emptyList())
            showBottomBar(false)
            SurahFavoriteScreen(navHostController = navController, state = state)
        }

        composable(route = Route.PRAYER){
            val viewModel: PrayerViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            showBottomBar(false)
            PrayerScreen(navHostController = navController, state = state, onEvent = viewModel::onEvent)
        }

        composable(
            route = Route.QURAN.plus("?number={surahNumber}&ayah={ayah}"),
            arguments = listOf(
                navArgument("surahNumber"){ type = NavType.IntType },
                navArgument("ayah"){
                    nullable = true
                    defaultValue = null
                    type = NavType.StringType
                }
            )
        ){ navBackStackEntry ->
            val surahNumber = navBackStackEntry.arguments?.getInt("surahNumber")
            val ayah = navBackStackEntry.arguments?.getString("ayah")?.toInt()
            val viewModel: DetailSurahViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            showBottomBar(false)
            DetailSurahScreen(
                navHostController = navController,
                surahNumber = surahNumber ?: 0,
                ayahNumber = ayah,
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }
}