package orbital.gns.pocketalert

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_friends.*
import orbital.gns.pocketalert.Others.User

class FriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        search_button.setOnClickListener {
            friendsSearch()
        }

        back_button.setOnClickListener {
            finish()
        }
    }

    private fun friendsSearch() {
        val name = editText_name.text.toString()
        val number = editText_number.text.toString()
        val email = editText_email.text.toString()
        if (name.isEmpty() && number.isEmpty() && email.isEmpty())
        {
            Toast.makeText(this@FriendsActivity, "Please input at least one field", Toast.LENGTH_SHORT).show()
            return
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        var friend : User ?= null
        var found = false
        if (name.isNotEmpty())
        {
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        val user = it.getValue(User::class.java)
                        if (user!!.username == name)
                        {
                            friend = user
                            found = true
                            return
                        }
                    }
                    Toast.makeText(this@FriendsActivity, "Could not find friend", Toast.LENGTH_SHORT).show()
                    return
                }
            })
        }
        else if (number.isNotEmpty())
        {
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        val user = it.getValue(User::class.java)
                        if (user!!.phoneNumber == number)
                        {
                            friend = user
                            found = true
                            return
                        }
                    }
                    Toast.makeText(this@FriendsActivity, "Could not find friend", Toast.LENGTH_SHORT).show()
                    return
                }
            })
        }
        else
        {
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        val user = it.getValue(User::class.java)
                        if (user!!.email == email)
                        {
                            friend = user
                            found = true
                            return
                        }
                    }
                    Toast.makeText(this@FriendsActivity, "Could not find friend", Toast.LENGTH_SHORT).show()
                    return
                }
            })
        }
        if (!found) return



    }
}
