package ge.takoba18ibare18.messengerapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ge.takoba18ibare18.messengerapplication.models.User

class LoginActivity : AppCompatActivity() {

    private lateinit var nicknameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initPrivateVariables()
        logInChecker()
        setListeners()
    }

    private fun logInChecker() {
        if (sharedPreferences.getString("nickname", "") != "") {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(
            findViewById(R.id.main_layout),
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun clearEditTexts() {
        nicknameEditText.text.clear()
        passwordEditText.text.clear()
    }

    private fun signIn() {
        val nickname = nicknameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (nickname.isEmpty() || password.isEmpty()) {
            showSnackBar("Some fields are empty!")
        } else {
            val usersReference = database.getReference("Users")
            val query = usersReference.orderByChild("nickname").equalTo(nickname)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        showSnackBar("Either nickname or password is incorrect")
                    } else {
                        val tmp = snapshot.getValue<Map<String, User>>()
                        val user = tmp!!.iterator().next().value
                        savePreferences(user)
                        clearEditTexts()
                        if (user.password == password) {
                            val intent = Intent(this@LoginActivity, MainPageActivity::class.java)
                            startActivity(intent)
                        } else {
                            showSnackBar("Either nickname or password is incorrect")
                        }
                    }
                }

                private fun savePreferences(user: User) {
                    with(sharedPreferences.edit()) {
                        putString("id", user.id)
                        apply()
                    }

                    with(sharedPreferences.edit()) {
                        putString("nickname", user.nickname)
                        apply()
                    }

                    with(sharedPreferences.edit()) {
                        putString("password", user.password)
                        apply()
                    }

                    with(sharedPreferences.edit()) {
                        putString("profession", user.profession)
                        apply()
                    }

                    if (user.profileImageURI == null) {
                        with(sharedPreferences.edit()) {
                            putString("imageUri", "")
                            apply()
                        }
                    } else {
                        with(sharedPreferences.edit()) {
                            putString("imageUri", user.profileImageURI)
                            apply()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }


    }

    private fun setListeners() {
        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun initPrivateVariables() {
        database = Firebase.database
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        nicknameEditText = findViewById(R.id.nickname)
        passwordEditText = findViewById(R.id.password)
        signInButton = findViewById(R.id.signInButton)
        signUpButton = findViewById(R.id.signUpButton)
    }
}