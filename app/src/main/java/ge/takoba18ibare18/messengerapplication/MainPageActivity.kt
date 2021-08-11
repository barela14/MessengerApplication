package ge.takoba18ibare18.messengerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        var adapter = MainPageAdapter()
        recyclerView.adapter = adapter
    }
}