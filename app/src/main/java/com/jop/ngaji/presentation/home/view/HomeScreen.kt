package com.jop.ngaji.presentation.home.view

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.jop.ngaji.R
import com.jop.ngaji.data.model.pray.DetailPrayTime
import com.jop.ngaji.data.model.LastSyncLocation
import com.jop.ngaji.ui.component.PermissionDialog
import com.jop.ngaji.ui.route.Route
import com.jop.ngaji.ui.theme.onSecondaryContainerLight
import com.jop.ngaji.ui.theme.secondaryContainerLight
import com.jop.ngaji.ui.theme.surfaceContainerLight
import com.jop.ngaji.ui.theme.surfaceContainerLowestDark
import com.jop.ngaji.ui.theme.surfaceContainerLowestLight
import com.jop.ngaji.ui.theme.surfaceLight
import com.jop.ngaji.ui.theme.tertiaryLight
import com.jop.ngaji.ui.theme.yellow
import com.jop.ngaji.util.differenceResult
import com.jop.ngaji.util.shimmerBackground
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit
) {
    val context = LocalContext.current
    val showAlertPermission = remember { mutableStateOf(false) }
    val locationPermissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.all { it.value }) {
                onEvent(HomeScreenEvent.GetPrayTime(false))
            } else {
                onEvent(HomeScreenEvent.GetPrayTime(true))
                if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(context,"Izinkan aplikasi untuk mengakses kamera", Toast.LENGTH_SHORT).show()
                } else {
                    showAlertPermission.value = true
                }
            }
        }
    )

    LaunchedEffect(Unit){
        permissionLauncher.launch(locationPermissions)
    }

    if(showAlertPermission.value){
        PermissionDialog(
            title = "Peringatan",
            message = "Izinkan aplikasi untuk lokasi terkini anda",
            onDismiss = { showAlertPermission.value = false }
        )
    }

    Column(
        modifier = Modifier.background(surfaceLight).fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(tertiaryLight)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ){
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                painter = painterResource(R.drawable.img_mosque),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(surfaceContainerLowestLight.copy(0.2f))
            )

            Column{
                HomeTopBarContent(state.lastSyncLocation)
                CurrentTimeContent(
                    isLoading = state.isLoading,
                    listPrayTime = state.prayerTimes,
                    event = onEvent
                )
            }
        }

        //BODY CONTENT
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            LazyVerticalGrid  (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(surfaceContainerLight)
                    .padding(16.dp)
                ,
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ){
               item {
                   ItemMenu(title = "Quran", icon = R.drawable.ic_book_outline){
                       navHostController.navigate(Route.QURAN) {
                           popUpTo(navHostController.graph.startDestinationId)
                           launchSingleTop = true
                       }
                   }
               }
                item {
                    ItemMenu(title = "Surat\nFavorit", icon = R.drawable.ic_star_outline){
                        navHostController.navigate(Route.QURAN_FAVORITE)
                    }
                }
                item {
                    ItemMenu(title = "Kumpulan\nDoa", icon = R.drawable.ic_pray_outline){
                        navHostController.navigate(Route.PRAYER)
                    }
                }
            }

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "Yuk Lanjutkan membaca mu",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = surfaceContainerLowestDark
                )
            )

            CardLastReadSurah(
                surahNameArabic = state.lastReadSurah.surahName,
                surahNumber = state.lastReadSurah.ayahNumber.toString()
            ){
                navHostController.navigate("${Route.QURAN}?number=${state.lastReadSurah.surahNumber}&ayah=${state.lastReadSurah.ayahNumber}")
            }
        }
    }
}

@Composable
fun CardLastReadSurah(surahNameArabic: String, surahNumber: String, action: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(164.dp)
        .clip(RoundedCornerShape(16.dp))
    ) {
        val painter = painterResource(R.drawable.bg_last_red)

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Terakhir Dibaca",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = surfaceContainerLowestLight
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = surahNameArabic,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = surfaceContainerLowestLight
                    )
                )

                Text(
                    text = "Ayat $surahNumber",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = surfaceContainerLowestLight
                    )
                )

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clickable { action() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Lanjut Membaca",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = surfaceContainerLowestLight
                        )
                    )

                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "",
                        tint = surfaceContainerLowestLight
                    )
                }
            }

            Image(
                painter = painterResource(R.drawable.img_quran),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun HomeTopBarContent(lastSyncLocation: LastSyncLocation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(surfaceLight.copy(alpha = 0.5f))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.ic_location_outline),
                contentDescription = "",
                tint = surfaceContainerLowestLight
            )

            Text(
                text = "${lastSyncLocation.city}, ${lastSyncLocation.country}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = surfaceContainerLowestLight
                )
            )
        }

        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { },
            painter = painterResource(R.drawable.ic_notification_outline),
            contentDescription = "",
            tint = surfaceContainerLowestLight
        )
    }
}

@Composable
fun CurrentTimeContent(isLoading: Boolean, listPrayTime: List<DetailPrayTime>, event: (HomeScreenEvent) -> Unit){
    val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    var date by remember { mutableStateOf(Date()) }
    var indexCurrentPrayTime by remember { mutableIntStateOf(-1) }
    var prayTimeText by remember { mutableStateOf("") }
    var remainingTimeText by remember { mutableStateOf("") }

    LaunchedEffect(listPrayTime.isNotEmpty()) {
        val listOfPrayTimeFormat = listPrayTime.map {
            val time = it.time.split(":")
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, time[0].toInt())
                set(Calendar.MINUTE, time[1].toInt())
            }
            calendar.time
        }

        val listOfPrayTimeFormatZip = listOfPrayTimeFormat.zipWithNext()
        var indexOfIncomingPrayTime: Int
        var indexOfSelectedPrayTime: Int

        while (listPrayTime.isNotEmpty()){
            date = Date()

            val indexOf = listOfPrayTimeFormatZip.indexOfFirst { it.first <= date && date <= it.second }
            indexOfSelectedPrayTime = if(indexOf == -1) 4 else indexOf
            indexOfIncomingPrayTime = if(indexOf == -1) 0 else indexOf + 1

            val incomingPrayTime = listPrayTime[indexOfIncomingPrayTime]
            val differentTime = listOfPrayTimeFormat[indexOfIncomingPrayTime].time.differenceResult()

            indexCurrentPrayTime = indexOfSelectedPrayTime
            prayTimeText = "Sholat ${incomingPrayTime.prayName} akan segera dimulai"
            remainingTimeText = "${if(differentTime.first > 0) "${differentTime.first} Jam" else "${differentTime.second} Menit"} Lagi"
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dateFormat.format(date),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = surfaceContainerLowestLight
            )
        )

        Text(
            text = timeFormat.format(date),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = surfaceContainerLowestLight
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = prayTimeText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = surfaceContainerLowestLight
                )
            )

            Text(
                text = remainingTimeText,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = yellow
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = surfaceContainerLowestLight
        )

        LazyVerticalGrid  (
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ){
            if(isLoading){
                items(5){
                    ItemPrayTime(
                        isLoading = true,
                        prayTime = DetailPrayTime()
                    )
                }
            } else {
                itemsIndexed(
                   items =  listPrayTime,
                    key = { _, item -> item.prayName }
                ){ index, prayTime ->
                    ItemPrayTime(
                        prayTime = prayTime,
                        isSelected = indexCurrentPrayTime == index
                    )
                }
            }
        }
    }
}

@Composable
fun ItemPrayTime(isLoading: Boolean = false, prayTime: DetailPrayTime, isSelected: Boolean = false){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            modifier = Modifier.shimmerBackground(isLoading),
            text = prayTime.prayName,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if(isSelected) yellow else surfaceContainerLowestLight
            )
        )

        Image(
            modifier = Modifier
                .size(24.dp)
                .shimmerBackground(isLoading),
            painter = painterResource(if(isSelected) prayTime.selectedIcon else prayTime.unselectedIcon),
            colorFilter = ColorFilter.tint(if(isSelected) yellow else surfaceContainerLowestLight),
            contentDescription = ""
        )

        Text(
            modifier = Modifier.shimmerBackground(isLoading),
            text = prayTime.time,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if(isSelected) yellow else surfaceContainerLowestLight
            )
        )
    }
}

@Composable
fun ItemMenu(title: String, icon: Int, action: () -> Unit = {}){
    Column(
        modifier = Modifier.clickable { action() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
               containerColor = secondaryContainerLight
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Icon(
                modifier = Modifier.padding(8.dp).size(24.dp),
                painter = painterResource(icon),
                contentDescription = "",
                tint = onSecondaryContainerLight
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = onSecondaryContainerLight,
                textAlign = TextAlign.Center
            )
        )
    }
}