package com.example.newsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.LazyPagingItems
import androidx.paging.LoadState
import coil.compose.AsyncImage
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.domain.model.Article
import kotlinx.coroutines.launch

/**
 * A composable function representing the home screen of the news application.
 * It displays a list of news articles with search functionality and bookmark options.
 *
 * @param ViewModel The ViewModel that provides the articles and bookmark functionality
 * @param onOpenArticle Callback function to open an article in a new screen
 * @param onOpenBookmarks Callback function to navigate to the bookmarks screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    onOpenArticle: (String) -> Unit,
    onOpenBookmarks: () -> Unit
) {
    val articles = viewModel.articlesFlow.collectAsLazyPagingItems()
    val bookmarkedUrls by viewModel.bookmarkedUrls.collectAsState()
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "News",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    )
                },
                actions = {
                    IconButton(onClick = onOpenBookmarks) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Bookmarks")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    viewModel.setQuery(it.text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Search articles…") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
            )

            when {
                articles.loadState.refresh is LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                articles.loadState.refresh is LoadState.Error -> {
                    val err = (articles.loadState.refresh as LoadState.Error).error
                    ErrorRetry(err.localizedMessage ?: "Unknown", onRetry = { articles.retry() })
                }

                else -> {
                    ArticleList(
                        items = articles,
                        bookmarkedUrls = bookmarkedUrls,
                        onItemClick = { article -> onOpenArticle(article.url) },
                        onToggleBookmark = { article ->
                            scope.launch { viewModel.toggleBookmark(article) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorRetry(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun ArticleList(
    items: LazyPagingItems<Article>,
    bookmarkedUrls: Set<String>,
    onItemClick: (Article) -> Unit,
    onToggleBookmark: (Article) -> Unit,
) {
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {

        items(
            count = items.itemCount,
            key = { index -> items[index]?.url ?: index }
        ) { index ->

            val article = items[index] ?: return@items

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onItemClick(article) },
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(modifier = Modifier.padding(8.dp)) {

                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 8.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            (article.title ?: "").toString(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            (article.source ?: "").toString(),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            (article.description ?: "").toString(),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    val isBookmarked = bookmarkedUrls.contains(article.url)

                    IconButton(onClick = {
                        scope.launch { onToggleBookmark(article) }
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Bookmarked" else "Bookmark"
                        )
                    }
                }
            }
        }

        item {
            when (items.loadState.append) {
                is LoadState.Loading -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    val e = (items.loadState.append as LoadState.Error).error
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Error: ${e.localizedMessage}")
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { items.retry() }) { Text("Retry") }
                    }
                }

                else -> {}
            }
        }
    }
}