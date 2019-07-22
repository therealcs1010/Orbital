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
    private var hasGps = false
    private var hasNetwork = false
    lateinit var locationManager : LocationManager
    private var locationGps : Location ?= null
    private var locationNetwork : Location ?= null
    var firstUpdate = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        //Log.d("debug", "Created")
        Toast.makeText(this@LocationActivity, "Please wait till we track your location...", Toast.LENGTH_LONG).show()
        FirebaseDatabase.getInstance().getReference("users/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val myUser = p0.getValue(User::class.java)
                    getLocationButton.setText("GET LOCATION(${myUser!!.friendsLocation.size})" )
                }
            })

        getLocation()

        sendLocationButton.setOnClickListener {
            val intent = Intent(this, SendLocationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {
            if (hasGps) {
                //Toast.makeText(this@LocationActivity, "Uploading coordinates to database..", Toast.LENGTH_SHORT).show()
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    100,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                          //  Toast.makeText(this@LocationActivity, "wahoo", Toast.LENGTH_SHORT).show()
                            if (location != null) {
                                locationGps = location
                            }
                            myLocations(locationGps, locationNetwork)
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {
                            Toast.makeText(this@LocationActivity, "Nope", Toast.LENGTH_SHORT).show()
                        }
                    })

                val localGpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (localGpslocation != null) {
                    locationGps = localGpslocation
                }

            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    100,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            //Toast.makeText(this@LocationActivity, "wahoo", Toast.LENGTH_SHORT).show()
                            if (location != null) {
                                Log.d("debug", "here for network!")
                                locationNetwork = location
                            }
                            myLocations(locationGps, locationNetwork)
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {
                            Toast.makeText(this@LocationActivity, "Nope", Toast.LENGTH_SHORT).show()
                        }
                    })
                val localGpsNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localGpsNetwork != null) {
                    locationNetwork = localGpsNetwork
                }
            }
        }
        else
        {
          //  Toast.makeText(this@LocationActivity, "No signal", Toast.LENGTH_SHORT).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun myLocations(locationGps: Location?, locationNetwork: Location?) {

        if (locationGps != null && locationNetwork != null) {
            if (locationGps.accuracy > locationNetwork.accuracy) {
               // Toast.makeText(this@LocationActivity, "Network stronger", Toast.LENGTH_SHORT).show()
                updateLocation(locationNetwork.longitude, locationNetwork.latitude)
            } else {
               // Toast.makeText(this@LocationActivity, "Gps", Toast.LENGTH_SHORT).show()
                updateLocation(locationGps.longitude, locationGps.latitude)
            }
        }
        else if (locationGps == null)
        {
           // Toast.makeText(this@LocationActivity, "Network only", Toast.LENGTH_SHORT).show()
            updateLocation(locationNetwork!!.longitude, locationNetwork.latitude)
        }
        else if (locationNetwork == null)
        {
           // Toast.makeText(this@LocationActivity, "Gps only", Toast.LENGTH_SHORT).show()
            updateLocation(locationGps.longitude, locationGps.latitude)
        }
       // else
        //Toast.makeText(this@LocationActivity, "None", Toast.LENGTH_SHORT).show()
    }

    private fun updateLocation(longitude: Double, latitude: Double) {
        if (firstUpdate) {
            Toast.makeText(this@LocationActivity, "Done!", Toast.LENGTH_SHORT).show()
            firstUpdate = false
        }
            FirebaseDatabase.getInstance().reference.child("users").child(uid!!).child("latitude").setValue(latitude)
        FirebaseDatabase.getInstance().reference.child("users").child(uid!!).child("longitude").setValue(longitude)
    }



}
