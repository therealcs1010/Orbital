package orbital.gns.pocketalert

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

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
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this@LoginActivity, "Wrong Email/Password", Toast.LENGTH_SHORT).show()
            }
    }
}
