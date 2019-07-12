package orbital.gns.pocketalert.Others

import java.io.Serializable

class User (val uid : String, val username : String , val profileImageUrl : String, val phoneNumber : String, val password : String, val email : String ) : Serializable {
    constructor() : this ("", "" , "", "", "", "")
    val friends : HashMap<String, String> = HashMap()
}