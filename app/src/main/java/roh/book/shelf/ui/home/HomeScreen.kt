package roh.book.shelf.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import roh.book.shelf.R
import roh.book.shelf.data.local.entities.BookEntity
import roh.book.shelf.ui.theme.TextColor

private const val TAG = "HomeScreen"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onLogOutClick: () -> Unit
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val books by homeViewModel.books.collectAsState(initial = emptyList())
    val years = books.map { it.publishedChapterDate }.distinct().sortedDescending()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Book Shelf",
                        fontWeight = FontWeight.Bold,
                        color = TextColor,
                    )
                },
                actions = {
                    IconButton(onClick = onLogOutClick) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            YearsChips(years)
            BookListScreen(
                viewModel = homeViewModel,
                books = books
            )
        }
    }
}

@Composable
fun BookListScreen(viewModel: HomeViewModel, books: List<BookEntity>) {
    Log.d(TAG, "BookListScreen: ==> ${books.size} : $books")
    if (books.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(books) { book ->
                BookItem(book, viewModel)
            }
        }
    }
}

@Composable
fun BookItem(book: BookEntity, viewModel: HomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = book.image,
            contentDescription = book.title,
            modifier = Modifier
                .weight(0.2f)
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(0.5f)
        ) {
            Text(book.title, style = MaterialTheme.typography.titleMedium)
            Text("Score: ${book.score}", style = MaterialTheme.typography.bodySmall)
            Text(
                "Published: ${book.publishedChapterDate}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopEnd,
        ) {
            IconButton(
                onClick = { viewModel.updateBookmark(book) }
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = if (book.isBookmarked)
                        painterResource(R.drawable.ic_heart_solid) else
                        painterResource(R.drawable.ic_heart_outline),
                    contentDescription = if (book.isBookmarked) "Remove bookmark" else "Add bookmark",
                    tint = Color.Unspecified
                )
            }
        }
    }

}

@Composable
fun YearsChips(years: List<Int>) {
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 18.dp)
    ) {
        items(years) { year ->
            Box(
                modifier = Modifier
                    .background(
                        color = if (selectedYear == year) Color(0xFF388E3C) else Color(0xFF81C784),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { selectedYear = year }
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = year.toString(),
                    color = Color.White
                )
            }
        }
    }
}
