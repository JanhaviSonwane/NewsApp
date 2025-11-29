package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity serves as the entry point of the News App.
 * It is annotated with AndroidEntryPoint to enable dependency injection using Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     * Sets up the Jetpack Compose UI for the application.
     *
     * @param savedInstanceState Contains the data most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Applies the NewsAppTheme to the entire application
            NewsAppTheme {
                // Provides a surface container using the 'background' color from the theme
                Surface {
                    // Sets up the navigation graph for the app
                    com.example.newsapp.navigation.NewsNavHost()
                }
            }
        }
    }
}