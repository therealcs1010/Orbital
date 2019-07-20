package orbital.gns.pocketalert.Friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_friend_profile.*
import orbital.gns.pocketalert.R

class ViewFriendProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_friend_profile)


        val friend = intent.extras.get("friend") as String
        val friendno = intent.extras.get("friendno") as String
        val friendimg = intent.extras.get("friendimg") as String
        val friendemail = intent.extras.get("friendemail") as String

        textView_email.text = "Email : $friendemail"
        textView_number.text = "Number : $friendno"
        textView_username.text = "Username : $friend"
        Picasso.get().load(friendimg).into(MyprofilePic_for_call)

        backButton.setOnClickListener {
            finish()
        }
    }
}
