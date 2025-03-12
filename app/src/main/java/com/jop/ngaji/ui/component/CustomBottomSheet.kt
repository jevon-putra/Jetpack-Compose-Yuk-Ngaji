package com.jop.ngaji.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jop.ngaji.R
import com.jop.ngaji.data.model.LastReadSurah
import com.jop.ngaji.data.model.Prayer
import com.jop.ngaji.presentation.detailSurah.view.DetailSurahScreenEvent
import com.jop.ngaji.presentation.prayer.view.PrayerScreenEvent

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailAyahBottomSheet(
    state: SheetState,
    showBottomSheet: MutableState<Boolean>,
    selectedAyah: LastReadSurah,
    event: (DetailSurahScreenEvent) -> Unit
){
    ModalBottomSheet(
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        onDismissRequest = {
            event(DetailSurahScreenEvent.ShowDetailAyahBottomSheet(null))
            showBottomSheet.value = false
        }
    ) {
        Column{
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                text = "QS ${selectedAyah.surahNameLatin} : Ayat ${selectedAyah.surahNumber}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            HorizontalDivider()

            ItemActionDetailAyah(
                icon = R.drawable.ic_play_outline,
                text = "Murrotal"
            ) {
                showBottomSheet.value = false
                event(DetailSurahScreenEvent.OnPlayAudioStartFromAyah(selectedAyah.ayahNumber - 1))
            }
            HorizontalDivider()

            ItemActionDetailAyah(
                icon = R.drawable.ic_clip,
                text = "Tandai Terakhir dibaca"
            ) {
                showBottomSheet.value = false
                event(DetailSurahScreenEvent.SetLastReadSurah(selectedAyah))
            }
        }
    }
}

@Composable
fun ItemActionDetailAyah(icon: Int, text: String, action: () -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth().clickable { action() }.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(icon),
            tint = MaterialTheme.colorScheme.primaryContainer,
            contentDescription = ""
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailPrayerBottomSheet(
    state: SheetState,
    showBottomSheet: MutableState<Boolean>,
    prayer: Prayer,
    event: (PrayerScreenEvent) -> Unit
){
    ModalBottomSheet(
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        onDismissRequest = {
            showBottomSheet.value = false
            event(PrayerScreenEvent.ShowDetailPrayer(null))
        }
    ) {
        Column{
            Text(
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                text = prayer.judul,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            HorizontalDivider()

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = prayer.arab,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = prayer.latin,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = prayer.terjemah,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }
    }
}