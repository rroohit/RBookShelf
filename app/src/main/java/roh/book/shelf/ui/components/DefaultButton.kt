package roh.book.shelf.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import roh.book.shelf.ui.theme.PrimaryColor
import roh.book.shelf.ui.theme.Purple80

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    title: String = "Please add text",
    enabled: Boolean = true,
    onButtonClick: () -> Unit
) {
    Button(
        onClick = onButtonClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 70.dp, end = 70.dp)
            .clip(RoundedCornerShape(8.dp)), // Rounded corners
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryColor
        ),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}