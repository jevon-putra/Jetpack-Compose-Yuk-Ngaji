package com.jop.ngaji.presentation.prayer.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.jop.ngaji.ui.component.DetailPrayerBottomSheet
import com.jop.ngaji.util.shimmerBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(navHostController: NavHostController, state: PrayerScreenState, onEvent: (PrayerScreenEvent) -> Unit) {
    val showBottomSheet = remember { mutableStateOf(false) }
    val stateBottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if(showBottomSheet.value){
        DetailPrayerBottomSheet(
            state = stateBottomSheet,
            showBottomSheet = showBottomSheet,
            prayer = state.selectedPrayer!!,
            event = onEvent
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            CustomToolbar(
                title = "Doa",
                canNavigateBack = true,
                navigateUp = { navHostController.popBackStack() }
            )
        }
    ){ padding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding())
                .fillMaxSize(),
        ) {
            if(state.isLoading){
                items(count = 20) {
                    ItemPrayer(isLoading = true)

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                }
            } else {
                itemsIndexed(
                    items = state.data,
                    key = { _, prayer -> prayer.id }
                ) { index, prayer ->
                    ItemPrayer(
                        number = (index + 1).toString(),
                        prayerName = prayer.judul,
                        onAction = {
                            showBottomSheet.value = true
                            onEvent(PrayerScreenEvent.ShowDetailPrayer(prayer))
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
fun ItemPrayer(
    number: String = "1",
    prayerName: String = "Doa Bangun Tidur",
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
                modifier = Modifier.run { fillMaxWidth().align(Alignment.Center) },
                text = number,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primaryContainer
                ),
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = prayerName,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
    }
}