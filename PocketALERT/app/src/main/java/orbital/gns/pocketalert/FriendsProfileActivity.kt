package orbital.gns.pocketalert

import android.content.Intent
import android.support.v7.app.AppCompatActivity
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

class FriendsProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_profile)

        val friend = intent.extras.get("friend") as User
        textView_username.text = "Username : ${friend.username}"
        textView_email.text = "Email : ${friend.email}"
        Picasso.get().load(friend.profileImageUrl).into(MyprofilePic)

        addFriendButton.setOnClickListener {
            val uid = FirebaseAuth.getInstance().uid //my user id
            //To update the Hashmap for friend and to update Firebase
            FirebaseDatabase.getInstance().getReference("/users/$uid")
                    .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        var myUser = p0.getValue(User::class.java)
                        myUser!!.friends[friend.uid] = friend.uid
                        FirebaseDatabase.getInstance().reference.child("users").child("$uid").setValue(myUser)
//                        Log.d("debug", "${myUser.toString()}")
                        Toast.makeText(this@FriendsProfileActivity, "Added as friend", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    override fun onCancelled(p0: DatabaseError) {
                        //
                    }
                })


            Toast.makeText(this@FriendsProfileActivity, "Added as friend", Toast.LENGTH_SHORT).show()

        }
        backButton.setOnClickListener {
            finish()
        }
    }
}
