package ge.takoba18ibare18.messengerapplication

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainPageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var profileButton: ImageButton
    private lateinit var homeButton: ImageButton
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        initializePrivateVariables()
        setListeners()
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

        homeButton.setOnClickListener {

        }
    }

    private fun initializePrivateVariables() {
        recyclerView = findViewById(R.id.recyclerView)
        val adapter = MainPageAdapter()
        recyclerView.adapter = adapter
        profileButton = findViewById(R.id.profileButton)
        homeButton = findViewById(R.id.homeButton)
        fab = findViewById(R.id.fab_button)
        searchEditText = findViewById(R.id.editText)

    }
}