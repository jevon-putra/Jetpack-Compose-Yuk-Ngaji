package com.jop.ngaji.presentation.detailSurah.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jop.ngaji.R
import com.jop.ngaji.data.model.LastReadSurah
import com.jop.ngaji.data.model.Surah
import com.jop.ngaji.ui.component.CustomToolbar
import com.jop.ngaji.ui.component.DetailAyahBottomSheet
import com.jop.ngaji.util.shimmerBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSurahScreen(
    navHostController: NavHostController,
    surahNumber: Int,
    ayahNumber: Int?,
    state: DetailSurahScreenState,
    onEvent: (DetailSurahScreenEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val rowListState = rememberLazyListState()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { state.allSurah.size })

    LaunchedEffect(state.allSurah.isNotEmpty()){
        if(surahNumber > 1) {
            scope.launch { pagerState.scrollToPage(surahNumber - 1) }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            rowListState.scrollToItem(page)
            onEvent(DetailSurahScreenEvent.GetDetailSurah(page + 1))
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column {
                    CustomToolbar(
                        title = "Surat",
                        canNavigateBack = true,
                        navigateUp = { navHostController.popBackStack() },
                        actions = {
                            IconButton(
                                modifier = Modifier.alpha(if(state.selectedSurah == null) 0f else 1f),
                                onClick = {
                                    if(state.selectedSurah != null){
                                        onEvent(
                                            DetailSurahScreenEvent.SetFavoriteSurah(
                                                surahNumber = state.selectedSurah.id,
                                                isFavorite = !state.selectedSurah.favorite
                                            )
                                        )
                                    }
                                },
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (state.selectedSurah != null && state.selectedSurah.favorite) R.drawable.ic_star_fill
                                        else R.drawable.ic_star_outline
                                    ),
                                    contentDescription = "Favourite",
                                )
                            }
                        }
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
                        state = rowListState,
                        reverseLayout = true,
                        userScrollEnabled = false
                    ) {
                        if(state.isAllSurahLoading){
                            items(count = 10) {
                                Tab(
                                    selected = false,
                                    onClick = { },
                                    text = {
                                        Text(
                                            modifier = Modifier.shimmerBackground(true),
                                            text = "Al-Fatihah"
                                        )
                                    }
                                )
                            }
                        } else {
                            itemsIndexed(
                                items = state.allSurah,
                                key = { _, it -> it.id }
                            ){ index, surah ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    selectedContentColor = MaterialTheme.colorScheme.primary,
                                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                                    onClick = {
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    },
                                    text = {
                                        Text(text = "${surah.id}. ${surah.namaLatin}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
    ){ padding ->
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding())
                .fillMaxSize(),
        ) {
            if(state.isAllSurahLoading){
                LoadingStateDetailSurah(modifier = Modifier.weight(1f))
            } else {
                HorizontalPager(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    key = { index -> state.allSurah[index].id },
                    state = pagerState,
                    reverseLayout = true,
                ) {
                    if(state.isDetailSurahLoading && state.selectedSurah == null){
                        LoadingStateDetailSurah(modifier = Modifier.weight(1f))
                    } else {
                        AyahOfSurah(
                            modifier = Modifier.fillMaxSize(),
                            lastReadAyah = ayahNumber,
                            surahNumber = surahNumber,
                            state = state,
                            event = onEvent,
                        )
                    }
                }
            }

            state.selectedSurah?.let { surah ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    val speakerNameRaw = if(surah.ayat.isNotEmpty()) surah.ayat.first().audio.x04 else "https://equran.nos.wjv-1.neo.id/audio-partial/Ibrahim-Al-Dossari/001007.mp3"
                    val speakerName = speakerNameRaw.replace("-", " ").split("/")

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shimmerBackground(state.isDetailSurahLoading)
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Murottal ${surah.namaLatin}  ${speakerName[4]}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                            )
                        )

                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    if(state.isAudioPlaying){
                                        onEvent(DetailSurahScreenEvent.OnPauseAudio)
                                    } else {
                                        onEvent(DetailSurahScreenEvent.OnPlayAudio)
                                    }
                                },
                            painter = painterResource(if(state.isAudioPlaying) R.drawable.ic_pause else R.drawable.ic_play_fill),
                            contentDescription = "Play Murottal",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahOfSurah(
    modifier: Modifier,
    surahNumber: Int,
    lastReadAyah: Int?,
    state: DetailSurahScreenState,
    event: (DetailSurahScreenEvent) -> Unit
){
    val scope = rememberCoroutineScope()
    val columnListState = rememberLazyListState()
    val stateBottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(state.audioIndex) {
        if(state.isAudioPlaying && state.audioIndex > 0){
            scope.launch {
                columnListState.animateScrollToItem(state.audioIndex + 1)
            }
        }
    }

    LaunchedEffect(state.selectedSurah!!.id) {
        if (lastReadAyah != null && surahNumber == state.selectedSurah.id) {
            scope.launch {
                columnListState.animateScrollToItem(lastReadAyah - 1)
            }
        }
    }

    if(showBottomSheet.value){
        DetailAyahBottomSheet(
            state = stateBottomSheet,
            showBottomSheet = showBottomSheet,
            selectedAyah = state.selectedAyah!!,
            event = event
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        SurahInfo(
            name = state.selectedSurah.namaLatin,
            meaning = state.selectedSurah.arti,
            location = state.selectedSurah.tempatTurun,
            totalAyah = state.selectedSurah.jumlahAyat,
            isLoading = state.isDetailSurahLoading
        )

        LazyColumn(
            state = columnListState,
            modifier = Modifier.weight(1f),
        ) {
            if(state.selectedSurah.id > 1){
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shimmerBackground(state.isDetailSurahLoading)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(vertical = 12.dp),
                        text = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            itemsIndexed(
                items = state.selectedSurah.ayat,
                key = { _, it -> it.nomorAyat }
            ){ index, it ->
                ItemOfAyah(
                    ayah = it,
                    lastReadAyah = state.lastReadSurah.ayahNumber == it.nomorAyat && state.lastReadSurah.surahNumber == state.selectedSurah.id,
                    showSoundIcon = index == state.audioIndex && state.isAudioPlaying,
                    color = if(index % 2 == 1) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surfaceContainerLowest,
                    isLoading = state.isDetailSurahLoading
                ){
                    showBottomSheet.value = true
                    event(
                        DetailSurahScreenEvent.ShowDetailAyahBottomSheet(
                            LastReadSurah(
                                surahNumber = state.selectedSurah.id,
                                surahNameLatin = state.selectedSurah.namaLatin,
                                surahName = state.selectedSurah.nama,
                                ayahNumber = it.nomorAyat
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ItemOfAyah(
    ayah: Surah.Ayah,
    lastReadAyah: Boolean,
    color: Color,
    isLoading: Boolean,
    showSoundIcon: Boolean,
    action: () -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .clickable { action() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(
            modifier = Modifier.width(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.size(48.dp).shimmerBackground(isLoading),
            ){
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = if(lastReadAyah) R.drawable.ic_number_fill else R.drawable.ic_number_outline),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
                    contentDescription = "Number",
                )

                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    text = ayah.nomorAyat.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color =  if(lastReadAyah) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primaryContainer
                    ),
                )
            }

            AnimatedVisibility(showSoundIcon) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.ic_sound),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
                    contentDescription = "Number",
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().shimmerBackground(isLoading),
                text = ayah.teksArab,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            )
            Text(
                modifier = Modifier.fillMaxWidth().shimmerBackground(isLoading),
                text = ayah.teksLatin,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
            Text(
                modifier = Modifier.fillMaxWidth().shimmerBackground(isLoading),
                text = ayah.teksIndonesia,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Composable
fun SurahInfo(name: String, meaning: String, location: String, totalAyah: Int, isLoading: Boolean){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .shimmerBackground(isLoading)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "$name ($meaning)",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        Text(
            text = "$location $totalAyah Ayat",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
}

@Composable
fun LoadingStateDetailSurah(modifier: Modifier){
    LazyColumn(
        modifier = modifier
    ) {
        item {
            SurahInfo(
                name = "Al-Fatihah",
                meaning = "Pembukaan",
                location = "Mekkah",
                totalAyah = 7,
                isLoading = true
            )
        }

        items(10){
            ItemOfAyah(
                ayah = Surah.Ayah(
                    nomorAyat = 1,
                    teksArab = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
                    teksLatin = "Bismillaahir Rahmaanir Raheem",
                    teksIndonesia = "Allah"
                ),
                lastReadAyah = false,
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                isLoading = true,
                showSoundIcon = false
            )
        }
    }
}