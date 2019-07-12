package orbital.gns.pocketalert.Others

import android.util.Log
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.individual_row_for_status_update.view.*
import orbital.gns.pocketalert.R

class UserStatus(private val user : User) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_username.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.myprofilePic)
        viewHolder.itemView.textView_status.text = user.status
        Log.d("debug", "${viewHolder.itemView.textView_status.text}")
//        if (user.online!!)
//        {
//            viewHolder.itemView.MyprofilePic.borderColor = 255
//        }
//        else
//        {
//            viewHolder.itemView.MyprofilePic.borderColor = 0
//        }
    }

    override fun getLayout(): Int {
        return R.layout.individual_row_for_status_update
    }
}