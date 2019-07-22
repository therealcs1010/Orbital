package orbital.gns.pocketalert.Location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_location.*
import orbital.gns.pocketalert.Opening.MainMenuActivity
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.PhoneCalls.PhoneDirectoryActivity
import orbital.gns.pocketalert.R



class LocationActivity : AppCompatActivity() {


    val uid = FirebaseAuth.getInstance().uid
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        //Log.d("debug", "Created")

        FirebaseDatabase.getInstance().getReference("users/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val myUser = p0.getValue(User::class.java)
                    getLocationButton.setText("GET LOCATION(${myUser!!.friendsLocation.size})" )
                }
            })


        sendLocationButton.setOnClickListener {
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

        getLocationButton.setOnClickListener {
            val intent = Intent(this, GetLocationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

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
        val intent = Intent(this, SendLocationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
