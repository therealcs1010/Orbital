package orbital.gns.pocketalert.Others

class User(val uid : String, val username : String , val profileImageUrl : String, val phoneNumber : String, val password : String, val email : String ) {
    constructor() : this ("", "" , "", "", "", "")
    val friends : HashMap<String, User> = HashMap()
}