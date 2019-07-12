package orbital.gns.pocketalert

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import orbital.gns.pocketalert.Others.User
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = editText_name.text.toString()
        val email = editText_email.text.toString()
        val password = editText_password.text.toString()

        registerButton.setOnClickListener {
            registerUser()
        }

        backButton.setOnClickListener {
            finish()
        }

        photoButton.setOnClickListener {
            pickPhoto()
        }
    }

    var selectedPhotoUri : Uri?= null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null)
        {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectphotoImageView_register.setImageBitmap(bitmap)
            photoButton.alpha = 0f
        }
    }

    private fun pickPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun registerUser() {
        val email = editText_email.text.toString()
        val password = editText_password.text.toString()
        val username = editText_name.text.toString()
        if (email.isEmpty() || password.isEmpty() || username.isEmpty())
        {
            if (username.isEmpty())
            {
                Toast.makeText(this@RegisterActivity, "Please fill up username", Toast.LENGTH_SHORT).show()
            }
            else if (email.isEmpty())
            {
                Toast.makeText(this@RegisterActivity, "Please fill up email", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this@RegisterActivity, "Please fill up password", Toast.LENGTH_SHORT).show()
            }
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    //Toast.makeText(this@RegisterActivity, "Not successful", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                uploadImagetoFirebase()
            }
            .addOnFailureListener {
                Log.d("Debug", "create failed")
            }
    }

    private fun uploadImagetoFirebase() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveUsertoFirebase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("Debug", "uploadImagetoFirebase failed")
            }
    }

    private fun saveUsertoFirebase(profileImageUrl: String) {
        val uid= FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, editText_name.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Debug", "Sucessfully added to database")
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(this@RegisterActivity, "Successfully Created", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("Debug", "saveUsertoFirebase failed")
            }
    }
}
