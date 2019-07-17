package orbital.gns.pocketalert.Friends

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_friends.*
import orbital.gns.pocketalert.Others.User
import orbital.gns.pocketalert.R
import java.io.Serializable

class AddFriendsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        search_button.setOnClickListener {
            friendsSearch()
        }

        back_button.setOnClickListener {
            val intent = Intent(this, FriendsListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private fun friendsSearch() {
        val name = editText_name.text.toString()
        val number = editText_number.text.toString()
        val email = editText_email.text.toString()
        if (name.isEmpty() && number.isEmpty() && email.isEmpty())
        {
            Toast.makeText(this@AddFriendsActivity, "Please input at least one field", Toast.LENGTH_SHORT).show()
            return
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users")
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
                            getInfo(user)
                        }
                    }
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
                            getInfo(user)
                        }
                    }

                }
            })
        }
        else
        {
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(p0: DataSnapshot) {
                    for (child in p0.children){
                        val user = child.getValue(User::class.java)
                        if (user!!.email == email)
                        {
                            Log.d("debug","found")
                            getInfo(user)
                        }
                    }

                }
            })
        }

        Toast.makeText(this@AddFriendsActivity, "User Not Found", Toast.LENGTH_SHORT).show()
        editText_name.setText("")
        editText_email.setText("")
        editText_number.setText("")
        return

    }

    private fun getInfo(friend : User) {
        Toast.makeText(this@AddFriendsActivity, "Found friend", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, AddFriendsProfileActivity::class.java)
        intent.putExtra("friend", friend as Serializable)
        editText_name.setText("")
        editText_email.setText("")
        editText_number.setText("")
        startActivity(intent)
    }
}
