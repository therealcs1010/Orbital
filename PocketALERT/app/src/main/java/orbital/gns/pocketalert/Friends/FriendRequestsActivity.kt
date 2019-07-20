package orbital.gns.pocketalert.Friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_friend_requests.*
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.Others.UserItem
import orbital.gns.pocketalert.R

class FriendRequestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_requests)

        loadRequests()

        back_button.setOnClickListener {
            val intent = Intent(this, FriendsListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun loadRequests() {
        val uid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().getReference("/users/$uid").
                addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val user = p0.getValue(User::class.java)
                        //Log.d("debug", "${user!!.username}")
                        val adapter = GroupAdapter<ViewHolder>()
                            for (key in user!!.friendReqs.keys)
                            {
                                FirebaseDatabase.getInstance().getReference("/users/$key").
                                        addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(p0: DataSnapshot) {
                                                Log.d("debug", "$key")
                                                val friend = p0.getValue(User::class.java)
                                                if (friend != null)
                                                adapter.add(UserItem(friend))
                                            }

                                            override fun onCancelled(p0: DatabaseError) {

                                            }
                                        })
                            }
                        RecyclerView_friendreqs.adapter = adapter
                        adapter.setOnItemClickListener { item, view ->
                            val friend = item as UserItem
                            val intent = Intent(view.context, ConfirmFriendRequestActivity::class.java)
                            intent.putExtra("extra", friend.user)
                            startActivity(intent)
                        }
                    }
                })
    }
}
