package orbital.gns.pocketalert.Others

import java.io.Serializable

class User (val uid : String, val username : String , val profileImageUrl : String, val phoneNumber : String, val password : String, val email : String ) : Serializable {
    constructor() : this ("", "" , "", "", "", "")
    val friends : HashMap<String, String> = HashMap()
    var status : String ?= "I am fine."
    var online : Boolean ?= true
    var friendReqs : HashMap<String, String> = HashMap()
    var longitude : Double ?= null
    var latitude : Double ?= null
    var friendsLat : HashMap< String, Double> = HashMap()
    var friendsLong : HashMap<String, Double> = HashMap()
}