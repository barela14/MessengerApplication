package ge.takoba18ibare18.messengerapplication

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ge.takoba18ibare18.messengerapplication.models.MyMessage
import ge.takoba18ibare18.messengerapplication.models.User

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var nickname: TextView
    private lateinit var profession: TextView
    private lateinit var profileImage: ImageView
    private lateinit var sendButton: ImageButton
    private lateinit var sendEditText: EditText
    private lateinit var adapter: ChatAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var receiver: User
    private lateinit var sender: User
    private lateinit var messages: ArrayList<MyMessage>
    private lateinit var chatId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initPrivateVariables()
        getUsers()
        setListeners()
    }

    private fun getMessages(map: Map<String, MyMessage>) {
        if (map.count() > 0) {
            for (message in map.values) {
                messages.add(message)
            }
        }
        messages.sortWith(compareBy { it.sendTime })

        recyclerView.adapter = ChatAdapter(messages, sharedPreferences)
    }

    private fun fetchChat() {
        val chatReference = database.getReference("Chats")

        chatId = if (sender.id!! > receiver.id!!) {
            receiver.id + " " + sender.id
        } else {
            sender.id + " " + receiver.id
        }

        val query = chatReference.child(chatId).orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val map = snapshot.getValue<Map<String, MyMessage>>()
                    getMessages(map!!)
                } else {
                    //create New Chat
                    chatReference.child(chatId).push().key
                }
            }
        })
    }

    private fun getUsers() {
        receiver = intent.extras?.getSerializable("friend") as User
        setFriendInfo()
        getSender()
    }

    private fun getSender() {
        val myNickname = sharedPreferences.getString("nickname", "")
        val usersReference = database.getReference("Users")
        val newQuery = usersReference.orderByChild("nickname").equalTo(myNickname)
        newQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val senderMap = snapshot.getValue<Map<String, User>>()
                    val senderIterator = senderMap!!.iterator().next().value
                    sender = senderIterator
                    fetchChat()
                }
            }
        })
    }

    private fun sendNewMessage() {
        if (sendEditText.text.isNotEmpty()) {
            val text = sendEditText.text.toString()
            val time = System.currentTimeMillis()
            val newMessage = MyMessage(sender.nickname, receiver.nickname, text, time)
            val chatReference = database.getReference("Chats")
            messages.add(newMessage)

            chatReference.child(chatId).push().key?.let {
                chatReference.child(chatId).child(it).setValue(newMessage)
            }

            recyclerView.adapter = ChatAdapter(messages, sharedPreferences)
            sendEditText.text.clear()
        }
    }

    private fun setFriendInfo() {
        nickname.text = receiver.nickname
        profession.text = receiver.profession
        if (receiver.profileImageURI == "" || receiver.profileImageURI == null) {
            Glide.with(this@ChatActivity)
                .load(R.drawable.avatar_image_placeholder)
                .circleCrop().into(profileImage)
        } else {
            Glide.with(this@ChatActivity)
                .load(Uri.parse(receiver.profileImageURI))
                .circleCrop().into(profileImage)
        }
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            this.onBackPressed()
        }

        sendButton.setOnClickListener {
            sendNewMessage()
        }
    }

    private fun initPrivateVariables() {
        database = Firebase.database
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ChatAdapter(arrayListOf(), sharedPreferences)
        recyclerView.adapter = adapter
        backButton = findViewById(R.id.backButton)
        nickname = findViewById(R.id.nickname)
        profession = findViewById(R.id.profession)
        profileImage = findViewById(R.id.profile_image)
        sendButton = findViewById(R.id.sendButton)
        sendEditText = findViewById(R.id.editText)
        messages = arrayListOf()
    }
}