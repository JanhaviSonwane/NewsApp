package com.example.newsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(viewModel: NewsViewModel,    onBack: () -> Unit,
                    onOpenArticle: (String) -> Unit) {
    val bookmarks by viewModel.getBookmarks().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    "Bookmarks",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
    }) { padding ->
        if (bookmarks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No bookmarks")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),   // ⭐ FIX
                contentPadding = PaddingValues(8.dp)
            ) {  items(bookmarks.size) { idx ->
                    val b = bookmarks[idx]
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onOpenArticle(b.url) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {

                            AsyncImage(
                                model = b.imageUrl,
                                contentDescription = b.title,
                                modifier = Modifier.size(100.dp)
                            )

                            Spacer(Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(b.title, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text((b.source ?: "").toString(), style = MaterialTheme.typography.labelMedium)
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    b.description ?: "",
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // ⭐ FIXED Delete icon – always visible
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                IconButton(onClick = {
                                    scope.launch { viewModel.toggleBookmark(b) }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                }
                            }
                        }                    }
                }
            }
        }
    }
}