package org.saltedfish.chatbot

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.commandiron.compose_loading.Wave
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import org.saltedfish.chatbot.R
import org.saltedfish.chatbot.VQAViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VQA(navController: NavController, viewModel: VQAViewModel = viewModel()) {
    val selectedMessage by viewModel.selectedMessage.observeAsState()
    val answer by viewModel.answerText.observeAsState()
    val messages = viewModel.messages;
    val context = LocalContext.current
    LaunchedEffect(key1 = false) {
        viewModel.initStatus(context)
    }
    Scaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) {

        Box(modifier = Modifier.padding(it)) {
            Image(
                painter = painterResource(id = R.drawable.chat_record_demo),
                contentDescription = "Demo",
                Modifier.fillMaxSize()
            )

            NeverHideBottomSheet{
                BackHandler {
                    if (selectedMessage != null && selectedMessage != -1) {
                        viewModel.setSelectedMessage(-1)
                    } else {
                        navController.popBackStack()
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)) {
                    Row(Modifier.padding(5.dp),verticalAlignment = Alignment.CenterVertically) {
//                        Text(text = "🤖", fontSize = 32.sp, modifier = Modifier.padding(end = 10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.robot),
                            contentDescription = "",
                            Modifier
                                .padding(end = 10.dp)
                                .size(56.dp)
                        )
                        Column {
                            Text(
                                text = "Ask Me Something",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "About the screen",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    if (selectedMessage == -1 || selectedMessage == null) {
                        Text(
                            text = "You may want to ask",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Monospace,
                            fontStyle = FontStyle.Italic
                        )
                        FlowRow {
                            messages.forEachIndexed { index: Int, s: String ->
                                FilterChip(label = { Text(text = s, )
                                },
                                    selected = true,
                                    onClick = { viewModel.setSelectedMessage(index) },

                                    leadingIcon = {
                                        Image(
                                            modifier = Modifier.size(16.dp),
                                            painter = painterResource(id = R.drawable.star_filled),
                                            contentDescription = ""
                                        )
                                    })
                            }

                        }
                    } else {
                        Text(
                            text = messages[selectedMessage!!],
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Monospace,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        if(answer==null) Box(modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(), contentAlignment = Alignment.Center){
                            Wave(
                                size = 50.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )

                        } else{
                            Text(
                                text = answer!!,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                            )
                        }

//                        Text(
//                            text = "Answer",
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 18.sp,
//                            fontFamily = FontFamily.Monospace,
//                            fontStyle = FontStyle.Italic
//                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NeverHideBottomSheet(content: @Composable () -> Unit) {
    BottomSheetDialog(
        onDismissRequest = {

        },
        properties = BottomSheetDialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            enableEdgeToEdge = true,
            behaviorProperties = BottomSheetBehaviorProperties(
                isHideable = false,
                peekHeight = BottomSheetBehaviorProperties.PeekHeight(300),


                )
        )
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Box(
                        Modifier
                            .size(
                                width = 50.dp,
                                height = 4.dp
                            )
                    )
                }
                Text(text = "Drag to top to view more..", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                content()
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun rememberModalBottomSheetState(
//    skipPartiallyExpanded: Boolean = false,
//    confirmValueChange: (SheetValue) -> Boolean = { true },
//) = rememberSaveable(
//    skipPartiallyExpanded, confirmValueChange,
//    saver = SheetState.Saver(
//        skipPartiallyExpanded = skipPartiallyExpanded,
//        confirmValueChange = confirmValueChange
//    )
//) {
//    SheetState(skipPartiallyExpanded, SheetValue.Expanded, confirmValueChange, true)
//}

