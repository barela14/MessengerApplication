package ge.takoba18ibare18.messengerapplication.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Chat(
    val friend: String? = null,
    val lastMessage: Message? = null
)