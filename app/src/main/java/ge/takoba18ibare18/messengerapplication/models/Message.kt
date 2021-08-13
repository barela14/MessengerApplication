package ge.takoba18ibare18.messengerapplication.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Message(
    val sender: String? = null,
    val receiver: String? = null,
    val message: String? = null,
    val sendTime: Date? = null
)