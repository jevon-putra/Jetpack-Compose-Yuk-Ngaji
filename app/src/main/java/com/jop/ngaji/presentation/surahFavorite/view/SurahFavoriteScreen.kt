package com.jop.ngaji.presentation.surahFavorite.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jop.ngaji.data.model.Surah
import com.jop.ngaji.presentation.surah.view.ItemSurah
import com.jop.ngaji.ui.component.CustomToolbar
import com.jop.ngaji.ui.route.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahFavoriteScreen(navHostController: NavHostController, state: State<List<Surah>>) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                CustomToolbar(
                    title = "Surat Favorit",
                    canNavigateBack = true,
                    navigateUp = { navHostController.navigateUp() }
                )
            }
        }
    ){ padding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize(),
        ) {
            items(
                items = state.value,
                key = { surah -> surah.id }
            ) { surah ->
                ItemSurah(
                    surahNumber = surah.id.toString(),
                    surahName = surah.namaLatin,
                    surahNameArabic = surah.nama,
                    surahInfo = surah.arti,
                    totalAyah = surah.jumlahAyat,
                    onAction = {
                        navHostController.navigate(Route.QURAN.plus("?number=${surah.id}"))
                    }
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                )
            }
        }
    }
}