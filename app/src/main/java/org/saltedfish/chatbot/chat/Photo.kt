package org.saltedfish.chatbot.chat

import android.net.Uri
import coil.request.ImageRequest

data class Photo(
    var id :Int=0,
    val uri: Uri,
    val request: ImageRequest?
)