package orbital.gns.pocketalert.Opening

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main_menu.*
import orbital.gns.pocketalert.Friends.FriendsListActivity
import orbital.gns.pocketalert.PhoneCalls.PhoneDirectoryActivity
import orbital.gns.pocketalert.Profile.MyProfileActivity
import orbital.gns.pocketalert.R
import orbital.gns.pocketalert.StatusUpdates.StatusUpdateActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        verifyUserisLoggedIn()
    }

    private fun verifyUserisLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null)
        {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        button_call_authorities.setOnClickListener {
            val intent = Intent(this, PhoneDirectoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        button_nearby_alert.setOnClickListener {

        }

        button_status_updates.setOnClickListener {
            val intent = Intent(this, StatusUpdateActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        button_add_friends.setOnClickListener {
            val intent = Intent(this, FriendsListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        button_my_profile.setOnClickListener {
            val intent = Intent(this, MyProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        button_sign_out.setOnClickListener {
            signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun signOut() {
        val uid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().reference.child("users").child("$uid").child("online").setValue(false)
        FirebaseAuth.getInstance().signOut()
    }
}