package ge.takoba18ibare18.messengerapplication.models

import android.widget.ImageView
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
    val id : String? = null,
    val nickname: String? = null,
    val password: String? = null,
    val profession: String? = null,
    val profileImageURI: String? = null
) : Serializable