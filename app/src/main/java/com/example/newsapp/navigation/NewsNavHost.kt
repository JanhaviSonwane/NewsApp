package com.example.newsapp.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.newsapp.ui.screens.ArticleDetailScreen
import com.example.newsapp.ui.screens.BookmarksScreen
import com.example.newsapp.ui.screens.HomeScreen
import com.example.newsapp.viewmodel.NewsViewModel

object Destinations {
    const val HOME = "home"
    const val DETAIL = "detail"
    const val BOOKMARKS = "bookmarks"
}

/**
 * Composable function that sets up the navigation graph for the news application.
 * It uses Jetpack Navigation Compose to handle navigation between different screens.
 */
@Composable
fun NewsNavHost() {
    // Creates a NavController to manage navigation between screens
    val navController = rememberNavController()
    // Defines the navigation graph with different routes and their corresponding composable screens
    NavHost(navController = navController, startDestination = Destinations.HOME) {
        // Defines the HOME route which displays the HomeScreen
        composable(Destinations.HOME) {
            // Gets the NewsViewModel using Hilt's hiltViewModel() function
            val vm: NewsViewModel = hiltViewModel()
            // Renders the HomeScreen with necessary callbacks
            HomeScreen(
                viewModel = vm,
                onOpenArticle = { url -> navController.navigate("${Destinations.DETAIL}?url=${Uri.encode(url)}") },
                onOpenBookmarks = { navController.navigate(Destinations.BOOKMARKS) }
            )
        }
        // Defines the DETAIL route which displays the article details
        // It expects a URL parameter to fetch and display the specific article
        composable(
            route = "${Destinations.DETAIL}?url={url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "newsapp://article?url={url}" })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            ArticleDetailScreen(articleUrl = url, onBack = { navController.popBackStack() })
        }
        // Defines the BOOKMARKS route which displays the bookmarked articles
        composable(Destinations.BOOKMARKS) {
            val vm: NewsViewModel = hiltViewModel()
            BookmarksScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onOpenArticle = { url ->
                    navController.navigate("${Destinations.DETAIL}?url=${Uri.encode(url)}")
                }
            )
        }
    }
}