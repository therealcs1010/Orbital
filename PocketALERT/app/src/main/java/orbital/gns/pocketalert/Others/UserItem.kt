package orbital.gns.pocketalert.Others

import android.util.Log
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.individual_row_for_friends_list.view.*
import orbital.gns.pocketalert.R

class UserItem(val user : User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_username.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.MyprofilePic_for_call)
        Log.d("debug", "DOne")
    }

    override fun getLayout(): Int {
        return R.layout.individual_row_for_friends_list
    }
}