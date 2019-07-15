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
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import orbital.gns.pocketalert.Opening.MainMenuActivity
import orbital.gns.pocketalert.PhoneCalls.PhoneDirectoryActivity
import orbital.gns.pocketalert.R


private const val PERMISSION_REQUEST = 10
class LocationActivity : AppCompatActivity() {

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var hasGps = false
    private var hasNetwork = false
    lateinit var locationManager : LocationManager
    private var locationGps : Location ?= null
    private var locationNetwork : Location ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        //Log.d("debug", "Created")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkPermission(permissions) == false) {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        }

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 1)
//        }
        //Log.d("debug", "hERE!")
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
//        {
//            //Toast.makeText(this@LocationActivity, "Need to enable all permissions", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, MainMenuActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//        }

        getLocation()

    }

    private fun checkPermission(permissionArray : Array<String>) : Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
            {
                allSuccess = false
            }
        }
        return allSuccess
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {
            if (hasGps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                Log.d("debug", "here for gps!")
                                locationGps = location
                            }
                            myLocations(locationGps, locationNetwork)
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {

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
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
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
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun myLocations(locationGps: Location?, locationNetwork: Location?) {
        if (locationGps != null && locationNetwork != null) {
            if (locationGps!!.accuracy > locationNetwork!!.accuracy) {
                updateLocation(locationNetwork!!.longitude, locationNetwork!!.latitude)
            } else {
                updateLocation(locationGps!!.longitude, locationGps!!.latitude)
            }
        }
        else if (locationGps == null)
        {
            updateLocation(locationNetwork!!.longitude, locationNetwork!!.latitude)
        }
        else if (locationNetwork == null)
        {
            updateLocation(locationGps!!.longitude, locationGps!!.latitude)
        }
    }

    private fun updateLocation(longitude: Double, latitude: Double) {
        val uid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().reference.child("users").child(uid!!).child("latitude").setValue(latitude)
        FirebaseDatabase.getInstance().reference.child("users").child(uid!!).child("longitude").setValue(longitude)
    }

}
