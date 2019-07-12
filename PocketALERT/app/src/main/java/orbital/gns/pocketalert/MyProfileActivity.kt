package orbital.gns.pocketalert

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_profile.*
import orbital.gns.pocketalert.Others.User

class MyProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        getUserInfo()
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun getUserInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val uid = FirebaseAuth.getInstance().uid
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user!!.uid == uid)
                    {
                        textView_username.text = "Username : ${user.username}"
                        textView_email.text = "Email : ${user.email}"
                        textView_password.text = "Password : ${user.password}"
                        textView_phonenumber.text = "Phone Number : ${user.phoneNumber}"
                        Picasso.get().load(user.profileImageUrl).into(profilePic)
                        return
                    }
                }
            }
        })
    }
}
