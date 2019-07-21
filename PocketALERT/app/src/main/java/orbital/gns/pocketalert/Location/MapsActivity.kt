package orbital.gns.pocketalert.Location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        var friendUser = intent.extras.get("friend") as User
        var me = intent.extras.get("me") as User
        Log.d("debug", "cool")
//        var friend : User ?= null
//        FirebaseDatabase.getInstance().getReference("/users/$frienduid")
//            .addListenerForSingleValueEvent(object: ValueEventListener {
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    friend = p0.getValue(User::class.java)
//                }
//            })
        mMap = googleMap
        //Log.d("debug", friendUser!!.latitude.toString())
        // Add a marker in Sydney and move the camera
        val myfriend = LatLng(friendUser!!.latitude!!, friendUser!!.longitude!!)
        val meme = LatLng(me!!.latitude!!, me!!.longitude!!)
        mMap.addMarker(MarkerOptions().position(myfriend).title( "${friendUser.username}"))
        mMap.addMarker(MarkerOptions().position(meme).title("Me"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myfriend, 15f))
    }
}
