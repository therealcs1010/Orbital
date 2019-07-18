package orbital.gns.pocketalert.Location

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import orbital.gns.pocketalert.R

class GetLocationActivity : AppCompatActivity() {

    val uid = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_location)

    }
}
