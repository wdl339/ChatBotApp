package org.saltedfish.chatbot.chat

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.saltedfish.chatbot.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatInput(
    enable: Boolean,
    withImage: Boolean,
    onImageSelected: (Uri?) -> Unit = {},

    onMessageSend: (Message) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var imageUri = remember { mutableStateOf<Uri?>(null) }

//softkeyborad
    val keyboardController = LocalSoftwareKeyboardController.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let {
            imageUri.value = it
            onImageSelected(it)
        }

    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
    ) {
        if (withImage) IconButton(onClick = {
            launcher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }, Modifier.padding(10.dp)) {
            Image(
                painter = painterResource(id = if (imageUri.value != null) R.drawable.add_done else R.drawable.add_other),
                contentDescription = "Add Other Resources.",
                Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        OutlinedTextField(
//            enabled = enable,
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White.copy(0.5f),
                focusedContainerColor = Color.White.copy(0.5f),

                )

        )
        IconButton(onClick = {
            keyboardController?.hide()
            val punctuation = listOf('.', '?', '!', ',', ';', ':', '。', '？', '！', '，', '；', '：')
            if (text.isNotEmpty() && !punctuation.contains(text.last()) && text.last() != '\n') text += "."
            onMessageSend(
                Message(
                    text,
                    true,
                    0,
                    type = if (imageUri.value == null) MessageType.TEXT else MessageType.IMAGE,
                    content = imageUri.value
                )
            );text = "";imageUri.value = null;onImageSelected(null)
        }, enabled = enable) {
            Icon(
                painter = painterResource(id = R.drawable.up),
                contentDescription = "Send",
                Modifier.size(36.dp)
            )
        }

    }

}