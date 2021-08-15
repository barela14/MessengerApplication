package ge.takoba18ibare18.messengerapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ProfileActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nicknameEditText: EditText
    private lateinit var professionEditText: EditText
    private lateinit var profileImage: ImageView
    private lateinit var database: FirebaseDatabase
    private lateinit var id: String
    private lateinit var nickname: String
    private lateinit var profession: String
    private lateinit var imageUri: Uri
    private lateinit var imageUriString: String
    private lateinit var updateButton: Button
    private lateinit var signOutButton: Button
    private lateinit var homeButton: ImageButton
    private lateinit var fab: FloatingActionButton

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initPrivateVariables()
        nicknameEditText.setText(nickname)
        professionEditText.setText(profession)

        setProfileImage()
        setListeners()
    }

    private fun setProfileImage() {
        if (imageUriString == "") {
            Glide.with(this)
                .load(R.drawable.avatar_image_placeholder)
                .circleCrop().into(profileImage)
        } else {
            Glide.with(this)
                .load(Uri.parse(imageUriString))
                .circleCrop().into(profileImage)
        }
    }

    private fun setListeners() {
        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        signOutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            val newIntent = Intent(this, LoginActivity::class.java)
            startActivity(newIntent)
        }

        homeButton.setOnClickListener {
            val newIntent = Intent(this, MainPageActivity::class.java)
            startActivity(newIntent)
        }

        updateButton.setOnClickListener {
            updateUserInfo()
        }

        fab.setOnClickListener {
            val newIntent = Intent(this, FriendsActivity::class.java)
            startActivity(newIntent)
        }
    }

    private fun updateUserInfo() {
        val newNickname = nicknameEditText.text.toString()
        val newProfession = professionEditText.text.toString()

        if (newNickname.isEmpty() || newProfession.isEmpty()) {
            showSnackBar("Some fields are empty!")
            return
        }
        if (nickname == newNickname) {
            updateImageAndProfession()
        } else {
            updateValues()
        }
    }

    private fun updateImageAndProfession() {
        val newProfession = professionEditText.text.toString()
        val usersReference = database.getReference("Users")

        usersReference.child(id).child("profession").setValue(newProfession)
        usersReference.child(id).child("profileImageURI").setValue(imageUriString)

        with(sharedPreferences.edit()) {
            putString("profession", newProfession)
            apply()
        }

        with(sharedPreferences.edit()) {
            putString("imageUri", imageUriString)
            apply()
        }

        showSnackBar("Values updated successfully")
    }


    private fun updateValues() {
        val newNickname = nicknameEditText.text.toString()
        val newProfession = professionEditText.text.toString()

        val usersReference = database.getReference("Users")
        val query = usersReference.orderByChild("nickname").equalTo(newNickname)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists() || newNickname == nickname) {
                    usersReference.child(id).child("nickname").setValue(newNickname)
                    usersReference.child(id).child("profession").setValue(newProfession)
                    usersReference.child(id).child("profileImageURI").setValue(imageUriString)
                    savePreferences()
                    showSnackBar("Values updated successfully")
                } else {
                    showSnackBar("User with this nickname already exists!")
                }
            }

            private fun savePreferences() {
                with(sharedPreferences.edit()) {
                    putString("nickname", newNickname)
                    apply()
                }

                with(sharedPreferences.edit()) {
                    putString("profession", newProfession)
                    apply()
                }

                with(sharedPreferences.edit()) {
                    putString("imageUri", imageUriString)
                    apply()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun showSnackBar(text: String) {
        Snackbar.make(
            findViewById(R.id.main_layout),
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!

            Glide.with(this)
                .load(imageUri)
                .circleCrop()
                .into(profileImage)

            imageUriString = imageUri.toString()
            uploadImageToFirebaseStorage(imageUri)
        }
    }

    private fun uploadImageToFirebaseStorage(uri: Uri) {
        val filename = sharedPreferences.getString("id", "")
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                imageUriString = it.toString()
            }
        }
    }

    private fun initPrivateVariables() {
        database = Firebase.database
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        nicknameEditText = findViewById(R.id.nickname)
        professionEditText = findViewById(R.id.profession)
        profileImage = findViewById(R.id.profile_image)
        updateButton = findViewById(R.id.update)
        signOutButton = findViewById(R.id.signOutButton)
        homeButton = findViewById(R.id.homeButton)
        fab = findViewById(R.id.fab_button)

        id = sharedPreferences.getString("id", "")!!
        nickname = sharedPreferences.getString("nickname", "")!!
        profession = sharedPreferences.getString("profession", "")!!
        imageUriString = sharedPreferences.getString("imageUri", "")!!
    }
}