package ge.takoba18ibare18.messengerapplication

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.takoba18ibare18.messengerapplication.models.MyMessage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatAdapter(
    private var messages: ArrayList<MyMessage>,
    private var sharedPreferences: SharedPreferences
) :
    RecyclerView.Adapter<MessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {

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
        val message = messages[position]

        val messageText = message.text
        val dateText = message.sendTime

        val date = Date(dateText!!)

        val messageTextView: TextView = holder.itemView.findViewById(R.id.text)
        val dateTextView: TextView = holder.itemView.findViewById(R.id.time)

        messageTextView.text = messageText
        dateTextView.text = date.toHourAndMinuteFormat()
    }

    private fun Date.toHourAndMinuteFormat(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(this)
    }


    companion object {
        private const val SENT = 0
        private const val RECEIVED = 1
    }
}


class MessagesViewHolder(view: View) : RecyclerView.ViewHolder(view)