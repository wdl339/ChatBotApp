package org.saltedfish.chatbot

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.saltedfish.chatbot.R
import org.saltedfish.chatbot.ui.theme.Purple80

@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        contentWindowInsets = WindowInsets(16, 30, 16, 0),
        topBar = {
            Greeting("Android")
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Rounded.Star,"Star Us!") },
                text = { Text(text = "Star Us!") },
                onClick = {
                    // visit Github!
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/UbiquitousLearning/mllm")
                    context.startActivity(intent)
                },
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)

        ) {
            MainEntryCards(navController = navController)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 64.dp, start = 20.dp)) {
        Text(
            text = "Let's Chat",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 30.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        // Subtitle
        Text(
            text = "See what I can do for you",
            style = MaterialTheme.typography.titleLarge,
            lineHeight = 24.sp

        )
    }

}

@Composable
fun MainEntryCards(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    Column(
        Modifier
            .padding(8.dp)
            .padding(top = 20.dp)
    ) {

        Row {
            EntryCard(icon = R.drawable.text,
                backgroundColor = Color(0xEDADE6AA),
                title = "Text Reader",
                subtitle = "",
                onClick = { navController.navigate("chat/1?type=0") })
            Spacer(Modifier.width(8.dp))
            EntryCard(icon = R.drawable.image,
                backgroundColor = Purple80,
                title = "Image Reader",
                subtitle = "",
                onClick = { navController.navigate("chat/1?type=1") }

            )

        }
        Spacer(Modifier.height(8.dp))
        Row {
            EntryCard(icon = R.drawable.camera,
                // Pick up a pink
                backgroundColor = Color(0xEDF8BBD0),
                title = "Take A Photo",
                subtitle = "",
                onClick = {
                    navController.navigate("photo")
                })
            Spacer(Modifier.width(8.dp))
            EntryCard(icon = R.drawable.more_text,
                // pick a blue style
                backgroundColor = Color(0xEDB3E5FC),
                title = "Read My Screen",
                subtitle = "",
                onClick = {
                    navController.navigate("vqa")
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.EntryCard(
    icon: Int,
    backgroundColor: Color,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .weight(0.5f)
            .aspectRatio(0.8f),
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
    ) {
        Column(Modifier.padding(16.dp)) {
            RoundIcon(id = icon, backgoundColor = Color.White)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "Icon Description",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(36.dp),
                )
            }
        }
    }
}

@Composable
fun RoundIcon(id: Int, backgoundColor: Color) {
    Box(
        modifier = Modifier
            .padding(end = 16.dp)
            .size(48.dp)
            .clip(CircleShape)
            .background(backgoundColor)
    ) {
        Image(
            painter = painterResource(id),
            contentDescription = "Icon Description",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}