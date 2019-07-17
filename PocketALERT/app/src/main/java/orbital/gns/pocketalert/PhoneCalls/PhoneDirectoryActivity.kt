package orbital.gns.pocketalert.PhoneCalls

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
import kotlinx.android.synthetic.main.activity_phone_directory.*
import kotlinx.android.synthetic.main.activity_phone_directory.backButton
import orbital.gns.pocketalert.Opening.MainMenuActivity
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.Others.UserCall
import orbital.gns.pocketalert.R

class PhoneDirectoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_directory)

        findFriends()


        backButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


    }

    private fun findFriends() {
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
                                        adapter.add(UserCall(friend!!))
                                        Log.d("debug", "Here i am")
                                    }
                                    override fun onCancelled(p0: DatabaseError) {

                                    }
                                })
                        }

                    }

                    val temp1 = User("","Police", "", "999", "", "")
                    val temp2 = User("","SCDF", "", "995", "", "")
                    val temp3 = User("","Campus Security", "", "6874 1616", "", "")

                    adapter.add(UserCall(temp1))
                    adapter.add(UserCall(temp2))
                    adapter.add(UserCall(temp3))
                    adapter.setOnItemClickListener { item, view ->
                        val friend = item as UserCall
                        val intent = Intent(view.context , FriendCallActivity::class.java)
                        //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("friend", friend.user.username)
                        intent.putExtra("friendno", friend.user.phoneNumber)
                        intent.putExtra("friendimg", friend.user.profileImageUrl)
                        Log.d("debug", "Clicked!")
                        startActivity(intent)
                    }
                    recyclerView_contacts.adapter = adapter
                }
            })
    }
}
