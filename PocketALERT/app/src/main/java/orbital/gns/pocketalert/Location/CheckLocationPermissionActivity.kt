package orbital.gns.pocketalert.Location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import orbital.gns.pocketalert.Opening.MainMenuActivity
import orbital.gns.pocketalert.R

private const val PERMISSION_REQUEST = 10

class CheckLocationPermissionActivity : AppCompatActivity() {

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_location_permission)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermission(permissions)) {
            requestPermissions(permissions, PERMISSION_REQUEST)
            if (!checkPermission(permissions))
            {
                val intent = Intent(this, MainMenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
                NextActivity()

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


    private fun NextActivity() {
        val intent = Intent(this, LocationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
