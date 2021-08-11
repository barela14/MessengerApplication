package ge.takoba18ibare18.messengerapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class ChatAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == SENT) {
            SentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sent_message_cell, parent, false))
        }
        else {
            ReceivedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.received_message_cell, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun getItemViewType(position: Int): Int {
        return if(position % 2 == 0) {
            SENT
        } else {
            RECEIVED
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    inner class SentViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }

    inner class ReceivedViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }

    companion object{
        private const val SENT = 0
        private const val RECEIVED = 1;
    }

}