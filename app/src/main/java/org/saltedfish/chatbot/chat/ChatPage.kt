package org.saltedfish.chatbot.chat

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.origeek.imageViewer.previewer.ImagePreviewer
import com.origeek.imageViewer.previewer.rememberPreviewerState
import kotlinx.coroutines.launch
import org.saltedfish.chatbot.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatPage(navController: NavController, chatType: Int = 0, vm: ChatViewModel = viewModel()) {
    LaunchedEffect(key1 = chatType) {
        vm.setModelType(chatType)
    }
    val previewUri by vm.previewUri.observeAsState()
    val messages by vm.messageList.observeAsState(mutableListOf())
    val context = LocalContext.current
    val isBusy by vm.isBusy.observeAsState(false)
    val isLoading by vm.isLoading.observeAsState(true)
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val photoList by vm.photoList.observeAsState(listOf())
    val imageViewerState = rememberPreviewerState(pageCount = { photoList.size })
    val modelType by vm.modelType.observeAsState(0)
    val isExternalStorageManager = remember {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            true
        }
    }
    if (!isExternalStorageManager) {
        vm._isExternalStorageManager.value = false
        // request permission with ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION

    } else {
        vm._isExternalStorageManager.value = true
        LaunchedEffect(key1 = true) {
            vm.initStatus(context, chatType)
            vm._scrollstate = scrollState
        }
    }
    LaunchedEffect(key1 = isBusy) {
        scrollState.animateScrollTo(scrollState.maxValue)

    }
    Scaffold(modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (imageViewerState.visible) null else CenterAlignedTopAppBar(title = {
                Text(
                    text = "Chat", fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                }
            })
        },
        bottomBar = {
            if (imageViewerState.visible) null else BottomAppBar {
                ChatInput(!isBusy && !isLoading, withImage = modelType == 1, onImageSelected = {
                    vm.setPreviewUri(it)
                }
                ) {
                    //TODO
                    //Get timestamp
                    vm.sendMessage(context, it)

                }
            }
        }) {

        Column(
            modifier = Modifier.fillMaxHeight()

        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .consumeWindowInsets(it)
                    .systemBarsPadding()
                    .verticalScroll(scrollState)
            ) {
//                ChatBubble(message = Message("Hello", true, 0))
                if (!isLoading) ChatBubble(
                    message = Message(
                        if (modelType == 0) "Hi! I am a AI ChatBot. How can I assist you today?" else "Hi! I am a AI ChatBot. Feel Free to Talk to me or even send me some pictures!",
                        false,
                        0
                    )
                )
                messages.forEach {
                    ChatBubble(message = it, vm, scope, imageViewerState)
                }
//                Spacer(
//                    modifier = Modifier
//                        .weight(1f)
//
//                )
            }


        }
        if (previewUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .consumeWindowInsets(it)
                    .systemBarsPadding()
                    .background(Color.Transparent)
            ) {
                PreviewBubble(previewUri!!)

            }
        }
//            if (isLoading) Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(it)
//                    .consumeWindowInsets(it)
//                    .systemBarsPadding()
//                    .background(Color.Transparent)
//            ) {
//               Column (
//                   Modifier.align(Alignment.Center),
//                   horizontalAlignment = Alignment.CenterHorizontally,
//                   verticalArrangement = Arrangement.Center
//                   ){
//                   Wave(
//                       size = 100.dp,
//                       color = MaterialTheme.colorScheme.onPrimaryContainer,
//                   )
//                   Spacer(modifier = Modifier.height(80.dp))
//                   Text(text = "Loading Model...",color = MaterialTheme.colorScheme.onPrimaryContainer, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold
//                   )
//
//               }
//
//        }

        ImagePreviewer(state = imageViewerState, imageLoader = { pageIndex ->
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = photoList[pageIndex].uri)
                    .size(coil.size.Size.ORIGINAL).build()
            )
        }, detectGesture = {
            onTap = {
                scope.launch {
                    imageViewerState.close()
                }
            }
        })
    }
}