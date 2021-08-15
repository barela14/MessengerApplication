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
        val profession = user.profession
        val url = user.profileImageURI

        holder.nickname.text = nickname
        holder.profession.text = profession
        if (url == "" || url == null) {
            Glide.with(holder.itemView)
                .load(R.drawable.avatar_image_placeholder)
                .circleCrop().into(holder.profileImage)
        } else {
            Glide.with(holder.itemView)
                .load(Uri.parse(url))
                .circleCrop().into(holder.profileImage)
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java).apply {
                this.putExtra("friend", user)
            }

            holder.itemView.context.startActivity(intent)
        }
    }
}

class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nickname: TextView = itemView.findViewById(R.id.nickname)
    val profession: TextView = itemView.findViewById(R.id.profession)
    val profileImage: ImageView = itemView.findViewById(R.id.image)
}