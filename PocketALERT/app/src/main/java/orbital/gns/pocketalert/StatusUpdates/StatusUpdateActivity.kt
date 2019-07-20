package orbital.gns.pocketalert.StatusUpdates

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_status_update.*
import orbital.gns.pocketalert.Opening.MainMenuActivity
import orbital.gns.pocketalert.R

class StatusUpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_update)


        updateButton.setOnClickListener {
            updateMyStatus()
        }

        statusButton.setOnClickListener {
            val intent = Intent(this, FriendsStatus::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun updateMyStatus() {
        val uid = FirebaseAuth.getInstance().uid
        val status = editText_status_updates.text.toString()
        if (status.isEmpty())
        {
            Toast.makeText(this@StatusUpdateActivity, "Nothing to update", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseDatabase.getInstance().reference.child("users").child("$uid").child("status").setValue(status)
        Toast.makeText(this@StatusUpdateActivity, "Status updated!", Toast.LENGTH_SHORT).show()
        editText_status_updates.setText("")
    }
}
