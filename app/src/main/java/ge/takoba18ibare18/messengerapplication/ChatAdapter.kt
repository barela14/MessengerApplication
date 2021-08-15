package ge.takoba18ibare18.messengerapplication

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.takoba18ibare18.messengerapplication.models.MyMessage


class ChatAdapter(var messages: ArrayList<MyMessage>, var sharedPreferences: SharedPreferences) :
    RecyclerView.Adapter<MessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
//        return if (viewType == SENT) {
//            SentViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.sent_message_cell, parent, false)
//            )
//        } else {
//            ReceivedViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.received_message_cell, parent, false)
//            )
//        }

        val view: View = if (viewType == SENT) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sent_message_cell, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.received_message_cell, parent, false)
        }

        return MessagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun getItemViewType(position: Int): Int {
        val sender = messages[position].sender

        return if (sender == sharedPreferences.getString("nickname", "")) {
            SENT
        } else {
            RECEIVED
        }
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = messages[position].text
        val date = messages[position].sendTime

        val messageTextView: TextView = holder.itemView.findViewById(R.id.text)
        val dateTextView: TextView = holder.itemView.findViewById(R.id.time)


        messageTextView.text = message
        dateTextView.text = date.toString()
    }


    companion object {
        private const val SENT = 0
        private const val RECEIVED = 1
    }

}


class MessagesViewHolder(view: View) : RecyclerView.ViewHolder(view)