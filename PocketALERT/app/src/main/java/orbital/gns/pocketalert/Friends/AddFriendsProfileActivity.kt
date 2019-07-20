package orbital.gns.pocketalert.Friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_friends_profile.*
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.R

class AddFriendsProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_profile)

        val friend = intent.extras.get("friend") as User
        textView_username.text = "Username : ${friend.username}"
        Picasso.get().load(friend.profileImageUrl).into(MyprofilePic_for_call)

        confirmButton.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("/users/${friend.uid}")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }
                    override fun onDataChange(p0: DataSnapshot) {
                        val uid = FirebaseAuth.getInstance().uid
                        var friendUser = p0.getValue(User::class.java)
                        friendUser!!.friendReqs[uid.toString()] = uid.toString()
                        FirebaseDatabase.getInstance().reference.child("users").child("${friendUser.uid}").setValue(friendUser)
                        Toast.makeText(this@AddFriendsProfileActivity, "Waiting for friend request confirmation", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}
