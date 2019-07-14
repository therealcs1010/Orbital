package orbital.gns.pocketalert.Profile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_profile.*
import orbital.gns.pocketalert.Opening.MainMenuActivity
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.R

class MyProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        getUserInfo()
        backButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun getUserInfo() {

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    Log.d("debug", "$user")
                    if (user!!.uid == uid)
                    {
                        textView_username.text = "Username : ${user.username}"
                        textView_number.text = "Email : ${user.email}"
                        textView_password.text = "Password : ${user.password}"
                        textView_phonenumber.text = "Phone Number : ${user.phoneNumber}"
                        textView_status.text = "Status : ${user.status}"
                        Log.d("debug", "$user.status")
                        Picasso.get().load(user.profileImageUrl).into(MyprofilePic_for_call)
                        return
                    }
            }
        })
    }
}
