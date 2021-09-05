package ge.takoba18ibare18.messengerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ge.takoba18ibare18.messengerapplication.models.User

class FriendsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FriendsAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var backButton: ImageButton
    private lateinit var users: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        initPrivateVariables()
        searchDatabase()
        setListeners()
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun initPrivateVariables() {
        database = Firebase.database
        recyclerView = findViewById(R.id.recyclerView)
        backButton = findViewById(R.id.backButton)
        users = arrayListOf()
        adapter = FriendsAdapter(users)
        recyclerView.adapter = adapter
    }

    private fun searchDatabase() {

        val usersReference = database.getReference("Users")
        val query = usersReference.orderByKey()

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val map = snapshot.getValue<Map<String, User>>()
                    if (map != null) {

                        users.addAll(map.values)

                        if (users.size > 0) {
                            (recyclerView.adapter as FriendsAdapter).usersList = users
                            adapter.notifyDataSetChanged()
                        }
                        return
                    }
                } else {
                    //
                }
            }
        })
    }
}