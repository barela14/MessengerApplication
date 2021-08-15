package ge.takoba18ibare18.messengerapplication.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Chat(
    val id: String? = null,
    val friend: User? = null,
    val lastMessage: MyMessage? = null
)