package ge.takoba18ibare18.messengerapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.takoba18ibare18.messengerapplication.models.User


class FriendsAdapter(var usersList: ArrayList<User>) :
    RecyclerView.Adapter<FriendsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val cell = LayoutInflater.from(parent.context)
            .inflate(R.layout.friends_cell, parent, false)
        return FriendsViewHolder(cell)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val user = usersList[position]
        val nickname = user.nickname
        val profession = user.password
        val url = user.profileImageURI

        holder.nickname.text = nickname
        holder.profession.text = profession
        Glide.with(holder.itemView).load(BitmapDrawable(stringToBitMap(url))).circleCrop()
            .into(holder.profileImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java).apply {
                this.putExtra("userId", user.id)
            }

            holder.itemView.context.startActivity(intent)
        }
    }

    private fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(
                encodeByte, 0,
                encodeByte.size
            )
        } catch (e: Exception) {
            e.message
            null
        }
    }
}

class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nickname: TextView = itemView.findViewById(R.id.nickname)
    val profession: TextView = itemView.findViewById(R.id.profession)
    val profileImage: ImageView = itemView.findViewById(R.id.image)
}