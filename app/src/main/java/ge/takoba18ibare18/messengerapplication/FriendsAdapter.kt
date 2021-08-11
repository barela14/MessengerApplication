package ge.takoba18ibare18.messengerapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class FriendsAdapter() :
    RecyclerView.Adapter<FriendsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val cell = LayoutInflater.from(parent.context)
            .inflate(R.layout.friends_cell, parent, false)
        return FriendsViewHolder(cell)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {

    }
}

class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}