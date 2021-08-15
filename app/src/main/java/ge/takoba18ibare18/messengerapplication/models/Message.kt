package ge.takoba18ibare18.messengerapplication.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Message(
    val sender: User? = null,
    val receiver: User? = null,
    val text: String? = null,
    val sendTime: Date? = null
)