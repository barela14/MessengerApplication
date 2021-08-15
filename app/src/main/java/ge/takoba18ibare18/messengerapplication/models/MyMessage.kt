package ge.takoba18ibare18.messengerapplication.models

import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalDateTime
import java.util.*

@IgnoreExtraProperties
data class MyMessage(
    val sender: String? = null,
    val receiver: String? = null,
    val text: String? = null,
    val sendTime: Long? = null
)