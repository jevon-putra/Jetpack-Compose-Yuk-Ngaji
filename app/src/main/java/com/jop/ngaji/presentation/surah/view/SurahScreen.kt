package com.jop.ngaji.presentation.surah.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jop.ngaji.R
import com.jop.ngaji.ui.component.CustomToolbar
import com.jop.ngaji.ui.route.Route
import com.jop.ngaji.util.shimmerBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahScreen(navHostController: NavHostController, state: SurahScreenState) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            CustomToolbar(
                title = "Quran"
            )
        }
    ){ padding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize(),
        ) {
            if(state.isLoading){
                items(count = 20) {
                    ItemSurah(isLoading = true)

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                }
            } else {
                items(
                    items = state.data,
                    key = { surah -> surah.id }
                ) { surah ->
                    ItemSurah(
                        surahNumber = surah.id.toString(),
                        surahName = surah.namaLatin,
                        surahNameArabic = surah.nama,
                        surahInfo = surah.arti,
                        totalAyah = surah.jumlahAyat,
                        onAction = {
                            navHostController.navigate(Route.SURAH.plus("?number=${surah.id}"))
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
}



@Composable
fun ItemSurah(
    surahNumber: String = "1",
    surahName: String = "Al-Fatihah",
    surahNameArabic: String = "الفاتحة",
    totalAyah: Int = 1,
    surahInfo: String = "Pembuka",
    isLoading: Boolean = false,
    onAction: () -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAction() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shimmerBackground(isLoading),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp),
        ){
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.ic_number_outline),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
                contentDescription = "Number",
            )
            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                text = surahNumber,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primaryContainer
                ),
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = surahName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "$surahInfo | $totalAyah Ayat",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
            )
        }

        Text(
            text = surahNameArabic,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
    }
}