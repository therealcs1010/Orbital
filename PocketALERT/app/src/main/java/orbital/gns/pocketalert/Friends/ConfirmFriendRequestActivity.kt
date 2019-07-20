package orbital.gns.pocketalert.Friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_confirm_friend_request.*
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.R

class ConfirmFriendRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_friend_request)
        val id = intent.extras.get("extra") as User
        val uid = FirebaseAuth.getInstance().uid
        textView_username.text = "Username : ${id.username}"
        Picasso.get().load(id.profileImageUrl).into(MyprofilePic_for_call)
        var myFriend : User ?= null
        var myUser : User ?= null

        FirebaseDatabase.getInstance().getReference("/users/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    myUser = p0.getValue(User::class.java)
                }
            })

        FirebaseDatabase.getInstance().getReference("/users/${id.uid}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    myFriend = p0.getValue(User::class.java)
                }
            })

        confirmButton.setOnClickListener {
            myUser!!.friendReqs.remove(myFriend!!.uid)
            myUser!!.friends[myFriend!!.uid] = myFriend!!.uid
            myFriend!!.friends[myUser!!.uid] = myUser!!.uid
            FirebaseDatabase.getInstance().reference.child("users").child(uid!!).setValue(myUser)
            FirebaseDatabase.getInstance().reference.child("users").child(id.uid).setValue(myFriend)
            myUser = null
            myFriend = null
            finish()
        }
        deleteButton.setOnClickListener {
            myUser!!.friendReqs.remove(myFriend!!.uid)
            Log.d("debug", "This is my ${myFriend!!.uid}")
            FirebaseDatabase.getInstance().reference.child("users").child("$uid").setValue(myUser)
            myUser = null
            myFriend = null
            finish()
        }
        backButton.setOnClickListener {
            finish()
        }
    }

}
