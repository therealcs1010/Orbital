package orbital.gns.pocketalert.Friends

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_friends_list.*
import orbital.gns.pocketalert.Opening.MainMenuActivity
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.Others.UserItem
import orbital.gns.pocketalert.R

class FriendsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)
        fetchFriends()

        button_back.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        button_find_friends.setOnClickListener {
            val intent = Intent(this, AddFriendsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun fetchFriends() {
        val uid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().getReference("/users/$uid/friends")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val adapter= GroupAdapter<ViewHolder>()
                    p0.children.forEach {
                        val usernamefriend = it.getValue(String::class.java)
                        if (usernamefriend != null)
                        {
                            FirebaseDatabase.getInstance().getReference("/users/$usernamefriend")
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
                                        val friend = p0.getValue(User::class.java)
                                        adapter.add(UserItem(friend!!))
                                    }

                                    override fun onCancelled(p0: DatabaseError) {

                                    }
                                })
                        }
                        recycler_view_for_friends.adapter = adapter

                    }
                }
            })
    }
}
