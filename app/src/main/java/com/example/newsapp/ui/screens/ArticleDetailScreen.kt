package com.example.newsapp.ui.screens

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.viewmodel.NewsViewModel

/**
 * ArticleDetailScreen is a composable function that displays a detailed article view.
 * It includes a top app bar with back, share, and open in browser actions,
 * and a WebView to display the article content.
 *
 * @param articleUrl The URL of the article to be displayed
 * @param onBack Callback function to handle back navigation
 * @param viewModel NewsViewModel instance (optional, kept if needed)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    articleUrl: String,
    onBack: () -> Unit,
    viewModel: NewsViewModel = hiltViewModel()   // optional, KEEP if needed
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Article", style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
                ) },

                // BACK BUTTON
                // Creates a navigation icon (back button) in the top app bar
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                // RIGHT SIDE ACTIONS
                // Defines action buttons on the right side of the top app bar
                actions = {

                    // SHARE ACTION
                    // Creates a share button that shares the article URL
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, articleUrl)
                        }
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Share Article")
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }

                    // OPEN IN BROWSER ACTION
                    // Creates a button to open the article in a web browser
                    IconButton(onClick = {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                        context.startActivity(browserIntent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "Open in Browser"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        // COLUMN CONTAINER
        // Creates a column that fills the available space with padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // WEBVIEW INSIDE COMPOSE
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        loadUrl(articleUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}