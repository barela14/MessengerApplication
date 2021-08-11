package ge.takoba18ibare18.messengerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        var adapter = ChatAdapter()
        recyclerView.adapter = adapter
    }
}