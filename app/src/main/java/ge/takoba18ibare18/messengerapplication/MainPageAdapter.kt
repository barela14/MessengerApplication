package ge.takoba18ibare18.messengerapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.takoba18ibare18.messengerapplication.models.Chat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainPageAdapter(private var chats: ArrayList<Chat>) :
    RecyclerView.Adapter<MainPageHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPageHolder {
        val cell = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_page_messege_cell, parent, false)
        return MainPageHolder(cell)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: MainPageHolder, position: Int) {
        val chat = chats[position]
        val friendNickname = chat.friend?.nickname
        val lastMessage = chat.lastMessage
        val date = Date(lastMessage?.sendTime!!)
        val url = chat.friend?.profileImageURI
        val user = chat.friend

        holder.name.text = friendNickname
        holder.text.text = lastMessage.text
        holder.time.text = date.toHourAndMinuteFormat()

        if (url == "" || url == null) {
            Glide.with(holder.itemView)
                .load(R.drawable.avatar_image_placeholder)
                .circleCrop().into(holder.image)
        } else {
            Glide.with(holder.itemView)
                .load(Uri.parse(url))
                .circleCrop().into(holder.image)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java).apply {
                this.putExtra("friend", user)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    private fun Date.toHourAndMinuteFormat(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(this)
    }
}

class MainPageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: ImageView = itemView.findViewById(R.id.profile_image)
    var name: TextView = itemView.findViewById(R.id.name)
    var text: TextView = itemView.findViewById(R.id.text)
    var time: TextView = itemView.findViewById(R.id.time)
}