package ge.takoba18ibare18.messengerapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class MainPageActivity : AppCompatActivity() {

    private lateinit var profileButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        initializePrivateVariables()
        profileButton.setOnClickListener {
            val newIntent = Intent(this, ProfileActivity::class.java)
            startActivity(newIntent)
        }
    }

    private fun initializePrivateVariables() {
        recyclerView = findViewById(R.id.recyclerView)
        val adapter = MainPageAdapter()
        recyclerView.adapter = adapter
        profileButton = findViewById(R.id.profileButton)
    }
}