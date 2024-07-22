package org.saltedfish.chatbot.chat

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.origeek.imageViewer.previewer.ImagePreviewerState
import com.origeek.imageViewer.previewer.rememberPreviewerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.saltedfish.chatbot.ChatViewModel

@Composable
fun ColumnScope.ChatBubble(
    message: Message,
    vm: ChatViewModel? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    imageViewerState: ImagePreviewerState = rememberPreviewerState(pageCount = { 1 })
) {
    if (message.text.isNotEmpty()) ChatBubbleBox(isUser = message.isUser) {
        SelectionContainer {
            Text(text = message.text, fontSize = 18.sp)
        }
    }
    if (message.type == MessageType.IMAGE && vm != null) ChatBubbleBox(isUser = message.isUser) {

        (message.content as Uri?)?.let { image ->
            val photo = vm.photoList.value!!.find { it.uri == image }
            val (painter, id) = if (photo == null) {
                val requests = ImageRequest.Builder(LocalContext.current).data(data = image).build()
                val painter_ = rememberAsyncImagePainter(
                    requests
                )
                val photo_ = Photo(uri = image, request = requests)
                val id = vm.addPhoto(photo_)
                painter_ to id
            } else {
                rememberAsyncImagePainter(photo.request) to photo.id
            }
            Image(painter = painter,
                contentDescription = "Image Description",
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            imageViewerState.open(id)
                        }
                    }
                    .size(200.dp)
                    .clip(RoundedCornerShape(20.dp)))
        }
    }
}

@Composable
fun ColumnScope.ChatBubbleBox(isUser: Boolean, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .align(if (isUser) Alignment.End else Alignment.Start)
            .clip(
                RoundedCornerShape(
                    topStart = 48f,
                    topEnd = 48f,
                    bottomStart = if (isUser) 48f else 0f,
                    bottomEnd = if (isUser) 0f else 48f
                )
            )
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
            .widthIn(max = 300.dp)
    ) {
        content()
    }
}