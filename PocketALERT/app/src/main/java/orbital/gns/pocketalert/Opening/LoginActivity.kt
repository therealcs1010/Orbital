package orbital.gns.pocketalert.Opening

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import orbital.gns.pocketalert.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        to_register_Button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            Log.d("Debug", "Pressed login button")
            mylogin()
        }
    }

    private fun mylogin() {
        val email = editText_email.text.toString()
        val password = editText_password.text.toString()

        if (email.isEmpty() || password.isEmpty())
        {
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = FirebaseAuth.getInstance().uid
                FirebaseDatabase.getInstance().reference.child("users").child("$uid").child("online").setValue(true)
                val intent = Intent(this, MainMenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this@LoginActivity, "Wrong Email/Password", Toast.LENGTH_SHORT).show()
            }
    }
}
