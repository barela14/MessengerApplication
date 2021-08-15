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
import com.google.firebase.ktx.Firebase
import ge.takoba18ibare18.messengerapplication.models.User

class SignUpActivity : AppCompatActivity() {

    private lateinit var nicknameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var professionEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initPrivateVariables()
        setListeners()
    }

    private fun setListeners() {
        signUpButton.setOnClickListener {
            signUp()
        }
    }

    private fun initPrivateVariables() {
        database = Firebase.database
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        nicknameEditText = findViewById(R.id.nickname)
        passwordEditText = findViewById(R.id.password)
        professionEditText = findViewById(R.id.profession)
        signUpButton = findViewById(R.id.signUpButton)
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
        professionEditText.text.clear()
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

    private fun signUp() {
        val nickname = nicknameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val profession = professionEditText.text.toString()

        if (nickname.isEmpty() || password.isEmpty() || profession.isEmpty()) {
            showSnackBar("Some fields are empty!")
        } else {
            val usersReference = database.getReference("Users")
            val query = usersReference.orderByChild("nickname").equalTo(nickname)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        createUser()
                        showSnackBar("Registration successful!")
                        clearEditTexts()
                        startActivity(Intent(this@SignUpActivity, MainPageActivity::class.java))
                    } else {
                        showSnackBar("User with that nickname already exists!")
                    }
                }

                private fun createUser() {
                    usersReference.push().key?.let {
                        val myUser = User(it, nickname, password, profession)
                        usersReference.child(it).setValue(myUser)
                        savePreferences(User(it, nickname, password, profession))
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}