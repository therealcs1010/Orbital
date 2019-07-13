package orbital.gns.pocketalert.PhoneCalls

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_friend_call.*
import orbital.gns.pocketalert.R


class FriendCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_call)

        val friend = intent.extras.get("friend") as String
        val friendno = intent.extras.get("friendno") as String
        val friendimg = intent.extras.get("friendimg") as String

        val REQUEST_PHONE_CALL = 1
        textView_username.text = "Username : $friend"
        textView_number.text = "$friendno"
        if (friendimg.isNotEmpty())
        Picasso.get().load(friendimg).into(MyprofilePic_for_call)

        callButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            }
            else {
                mycall()
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, PhoneDirectoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun mycall() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:${textView_number.text}")
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                mycall()
            }
        }
    }
}
