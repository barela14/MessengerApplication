package ge.takoba18ibare18.messengerapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ge.takoba18ibare18.messengerapplication.models.Chat
import ge.takoba18ibare18.messengerapplication.models.MyMessage
import ge.takoba18ibare18.messengerapplication.models.User

class MainPageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var profileButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: MainPageAdapter
    private lateinit var res: ArrayList<Chat>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        initializePrivateVariables()
        setListeners()
        fetchChats()
    }

    private fun getChats(chats: Map<String, Map<String, MyMessage>>) {
        var lastMessage = MyMessage("", "", "", 0)
        var receiverId: String
        var senderId: String

        for ((chatId, chat) in chats) {
            val id1 = chatId.substringBefore(" ")
            val id2 = chatId.substringAfter(" ")

            if (sharedPreferences.getString("id", "") == id1) {
                receiverId = id2
                senderId = id1
            } else {
                receiverId = id1
                senderId = id2
            }

            if (senderId != sharedPreferences.getString("id", "")) {
                continue
            }

            val messages = chat.values.sortedWith(compareBy {
                it.sendTime
            })

            for (message in messages) {
                lastMessage = message
            }

            fillChats(receiverId, lastMessage, chatId)
        }
    }

    private fun fillChats(
        receiverId: String,
        lastMessage: MyMessage,
        chatId: String
    ) {
        val usersReference = database.getReference("Users")
        usersReference.child(receiverId).get().addOnSuccessListener {
            if (it.exists()) {
                val user = it.getValue<User>()
                res.add(Chat(chatId, user, lastMessage))
            }
            res.sortWith(compareBy { chat ->
                chat.lastMessage?.sendTime
            })
            res.reverse()
            recyclerView.adapter = MainPageAdapter(res)
        }
    }

    private fun fetchChats() {
        val chatReference = database.getReference("Chats")
        chatReference.get().addOnSuccessListener {
            if (it.exists()) {
                val map = it.getValue<Map<String, Map<String, MyMessage>>>()
                getChats(map!!)
            }
        }
    }

    private fun setListeners() {
        profileButton.setOnClickListener {
            val newIntent = Intent(this, ProfileActivity::class.java)
            startActivity(newIntent)
        }

        fab.setOnClickListener {
            val newIntent = Intent(this, FriendsActivity::class.java)
            startActivity(newIntent)
        }
    }

    private fun initializePrivateVariables() {
        database = Firebase.database
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = MainPageAdapter(arrayListOf())
        recyclerView.adapter = adapter
        profileButton = findViewById(R.id.profileButton)
        fab = findViewById(R.id.fab_button)
        searchEditText = findViewById(R.id.editText)
        res = arrayListOf()
    }
}