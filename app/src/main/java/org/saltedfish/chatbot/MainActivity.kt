package org.saltedfish.chatbot

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.saltedfish.chatbot.chat.ChatPage
import org.saltedfish.chatbot.chat.PhotoPage
import org.saltedfish.chatbot.ui.theme.ChatBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                JNIBridge.stop()
                this.startActivity(intent)
            }

        }
        //enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { Home(navController) }
                    composable(
                        "chat/{id}?type={type}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType },
                            navArgument("type") { type = NavType.IntType;defaultValue = 0 })
                    ) {
                        ChatPage(navController, it.arguments?.getInt("type") ?: 0)
                    }
                    composable("photo") {
                        PhotoPage(navController)
                    }
                    composable("vqa") {
                        VQA(navController)
                    }
                    // A surface container using the 'background' color from the theme
                }
            }
        }
    }
}

//@Composable
//fun HistoryItem(icon: Int, text: String, modifier: Modifier = Modifier) {
//    Card(
//        modifier = modifier
//            .padding(top = 5.dp)
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(15),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
//            Image(
//                painter = painterResource(id = icon),
//                contentDescription = "Icon Description",
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//                    .size(26.dp),
//                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
//
//            )
//            Spacer(modifier = Modifier.width(6.dp))
//            Text(
//                text = text,
//                style = MaterialTheme.typography.titleMedium,
//                color = Color.Black,
//                fontSize = 20.sp,
//                overflow = TextOverflow.Ellipsis,
//                softWrap = false,
//                modifier = Modifier.weight(1f)
//            )
//            Image(
//                painter = painterResource(id = R.drawable.more),
//                contentDescription = "Icon Description",
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//                    .size(32.dp),
//                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
//
//            )
//        }
//    }
//}