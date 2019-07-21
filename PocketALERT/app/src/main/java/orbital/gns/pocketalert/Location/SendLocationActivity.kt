package orbital.gns.pocketalert.Location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_send_location.*
import orbital.gns.pocketalert.Opening.MainMenuActivity
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.Others.UserItem
import orbital.gns.pocketalert.R

class SendLocationActivity : AppCompatActivity() {

    private var hasGps = false
    private var hasNetwork = false
    lateinit var locationManager : LocationManager
    private var locationGps : Location ?= null
    private var locationNetwork : Location ?= null
    var finalCoord : Pair<Double, Double> ?= Pair(-1.00, -1.00)
    val uid = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_location)

        getLocation()

        backButton.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        FirebaseDatabase.getInstance().getReference("/users/$uid/friends")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val adapter = GroupAdapter<ViewHolder>()
                    p0.children.forEach {
                        val userid = it.getValue(String::class.java)
                        Log.d("debug", "$userid")
                        if (userid != null)
                        {
                            FirebaseDatabase.getInstance().getReference("users/$userid")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {

                                        val friend = p0.getValue(User::class.java)
                                        Log.d("debug", "yay")
                                        adapter.add(UserItem(friend!!))

                                    }

                                    override fun onCancelled(p0: DatabaseError) {
                                        Log.d("debug", "yikes")
                                    }
                                })
                        }

                    }
                    adapter.setOnItemClickListener { item, view ->
                        val friend = item as UserItem
                        friend.user.friendsLocation[uid.toString()]= uid.toString()
                        FirebaseDatabase.getInstance().reference.child("users").child(friend.user.uid).setValue(friend.user)

                        Toast.makeText(this@SendLocationActivity, "Sent Location to ${friend.user.username}", Toast.LENGTH_SHORT).show()
                    }
                    friendsRecyclerView.adapter = adapter

                }
            })
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
            if (locationGps.accuracy > locationNetwork.accuracy) {
                updateLocation(locationNetwork.longitude, locationNetwork.latitude)
            } else {
                updateLocation(locationGps.longitude, locationGps.latitude)
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
        finalCoord = Pair(longitude, latitude)
        FirebaseDatabase.getInstance().reference.child("users").child(uid!!).child("latitude").setValue(latitude)
        FirebaseDatabase.getInstance().reference.child("users").child(uid!!).child("longitude").setValue(longitude)
    }

}
