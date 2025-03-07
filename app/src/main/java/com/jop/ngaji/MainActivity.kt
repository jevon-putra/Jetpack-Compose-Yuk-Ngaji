package com.jop.ngaji

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jop.ngaji.ui.component.BottomBar
import com.jop.ngaji.ui.route.NavigationRoute
import com.jop.ngaji.ui.theme.YukNgajiTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YukNgajiTheme {
                KoinAndroidContext{
                    val showBottomBar = remember { mutableStateOf(false) }
                    val navController = rememberNavController()

                    Column {
                        NavigationRoute(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            navController = navController,
                            showBottomBar = {
                                showBottomBar.value = it
                            }
                        )

                        AnimatedVisibility(visible = showBottomBar.value) {
                            BottomBar(
                                modifier = Modifier,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}