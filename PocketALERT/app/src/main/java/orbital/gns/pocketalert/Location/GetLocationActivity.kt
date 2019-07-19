package orbital.gns.pocketalert.Location

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_get_location.*
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.Others.UserItem
import orbital.gns.pocketalert.R

class GetLocationActivity : AppCompatActivity() {

    val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_location)

        val adapter = GroupAdapter<ViewHolder>()
        FirebaseDatabase.getInstance().getReference("users/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    val user = p0.getValue(User::class.java)
                    Log.d("debug", "hw")
                    user!!.friendsLong.forEach {

                        val frienduid = it.key
                        FirebaseDatabase.getInstance().getReference("users/$frienduid")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    Log.d("debug", "hello there")
                                    val friend = p0.getValue(User::class.java)
                                    Log.d("debug", friend!!.username)
                                    adapter.add(UserItem(friend!!))
                                }
                            })

                    }
                }
            })

        adapter.setOnItemClickListener { item, view ->
            val friend = item as UserItem
        }
        friendsRecyclerView.adapter = adapter

    }

    private fun getClass(key: String): User {
        var friend : User ?= null

        return friend!!
    }
}
