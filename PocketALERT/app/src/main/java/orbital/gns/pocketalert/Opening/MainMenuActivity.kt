package orbital.gns.pocketalert.Opening

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main_menu.*
import orbital.gns.pocketalert.Friends.FriendsListActivity
import orbital.gns.pocketalert.Location.LocationActivity
import orbital.gns.pocketalert.Location.SendLocationActivity
import orbital.gns.pocketalert.PhoneCalls.PhoneDirectoryActivity
import orbital.gns.pocketalert.Profile.MyProfileActivity
import orbital.gns.pocketalert.R
import orbital.gns.pocketalert.StatusUpdates.StatusUpdateActivity

class MainMenuActivity : AppCompatActivity() {

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET)

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

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestMultiplePermissions()
            }
            else
            {
                loadActivity()
            }
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
    private fun requestMultiplePermissions() {
        val rPermissions = permissions.filter {checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED}
        requestPermissions(rPermissions.toTypedArray(), 101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101)
        {
            if (grantResults.any{ it != PackageManager.PERMISSION_GRANTED}) {
                if (permissions.any{ shouldShowRequestPermissionRationale(it)}) {
                    AlertDialog.Builder(this)
                        .setMessage("Your error message here")
                        .setPositiveButton("Allow") { dialog, which -> requestMultiplePermissions() }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                        .create()
                        .show()
                    return
                }

            }
        }
        loadActivity()
    }

    private fun loadActivity() {
        val intent = Intent(this, LocationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
