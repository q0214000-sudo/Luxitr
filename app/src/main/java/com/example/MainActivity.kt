package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CollectionScreen
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LuxitrWebScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.LuxitrTheme
import com.example.ui.theme.ObsidianBlack
import com.example.ui.viewmodel.LuxitrViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LuxitrTheme {
                val viewModel: LuxitrViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

                // Intercept back button when not on Home screen
                BackHandler(enabled = currentScreen !is ScreenDestination.Home && currentScreen !is ScreenDestination.Splash) {
                    viewModel.navigateBack()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ObsidianBlack
                ) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "screen_transition"
                    ) { target ->
                        when (target) {
                            is ScreenDestination.Splash -> SplashScreen()
                            is ScreenDestination.Home -> HomeScreen(viewModel = viewModel)
                            is ScreenDestination.Chat -> ChatScreen(viewModel = viewModel)
                            is ScreenDestination.Collection -> CollectionScreen(viewModel = viewModel)
                            is ScreenDestination.Detail -> DetailScreen(
                                perfumeId = target.perfumeId,
                                viewModel = viewModel
                            )
                            is ScreenDestination.Quiz -> QuizScreen(viewModel = viewModel)
                            is ScreenDestination.InAppWeb -> LuxitrWebScreen(
                                url = target.url,
                                perfumeName = target.perfumeName,
                                viewModel = viewModel
                            )
                            is ScreenDestination.Favorites -> FavoritesScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
