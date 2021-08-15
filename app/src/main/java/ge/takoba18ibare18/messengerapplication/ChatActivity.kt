package ge.takoba18ibare18.messengerapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
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
    private lateinit var userId: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var receiver: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        initPrivateVariables()
        setListeners()
        getUsers(userId)
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)
        }
        sendButton.setOnClickListener {
            //
        }
    }

    private fun getChat(sender: User, receiver: User) {
        val chatReference = database.getReference("Chats")

        val id = if (sender.id!! > receiver.id!!) {
            receiver.id + " " + sender.id
        } else {
            sender.id + " " + receiver.id
        }

        val query = chatReference.orderByKey().equalTo(id)


        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var k = 2
                    val map = snapshot.getValue<Map<String, Map<String, MyMessage>>>()

                    createChat(map!!)
                } else {
                    var k = 2
                }
            }

            private fun createChat(map: Map<String, Map<String, MyMessage>>) {
                var res = arrayListOf<MyMessage>()
                val myMap = map.iterator().next().value
                for (message in myMap.values) {
                    res.add(message)
                }

                recyclerView.adapter = ChatAdapter(res, sharedPreferences)

//                    usersReference.push().key?.let {
//                        val chat = Chat(it, receiver, MyMessage(sender, receiver, ""))
//                        usersReference.child(it).setValue(chat)
//                    }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun getUsers(friendId: String) {
        val usersReference = database.getReference("Users")
        val query = usersReference.orderByKey().equalTo(friendId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val map = snapshot.getValue<Map<String, User>>()
                    val iterator = map!!.iterator().next().value
                    receiver = iterator
                    nickname.text = iterator.nickname
                    profession.text = iterator.profession

//                    Glide.with(this@ChatActivity)
//                        .load(stringToBitMap(iterator.profileImageURI))
//                        .circleCrop().into(profileImage)

                    val sender = sharedPreferences.getString("nickname", "")
                    val newQuery = usersReference.orderByChild("nickname").equalTo(sender)

                    newQuery.addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {

                                val senderMap = snapshot.getValue<Map<String, User>>()
                                val senderIterator = senderMap!!.iterator().next().value
                                getChat(senderIterator, receiver)
                            } else {

                            }
                        }

                    })
                } else {

                }

            }
        })


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

    private fun initPrivateVariables() {
        userId = intent.extras?.getString("userId")!!
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
    }
}