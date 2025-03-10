package com.jop.ngaji.presentation.home.view

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.jop.ngaji.R
import com.jop.ngaji.ui.component.PermissionDialog
import com.jop.ngaji.ui.theme.YukNgajiTheme
import com.jop.ngaji.ui.theme.surfaceContainerLowestLight

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
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        CardLastReadSurah(
            surahNameArabic = state.lastReadSurah.surahName,
            surahNumber = state.lastReadSurah.ayahNumber.toString()
        ){
            navHostController.navigate("surah?number=${state.lastReadSurah.surahNumber}&ayah=${state.lastReadSurah.ayahNumber}")
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
            modifier = Modifier.fillMaxSize().padding(16.dp),
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