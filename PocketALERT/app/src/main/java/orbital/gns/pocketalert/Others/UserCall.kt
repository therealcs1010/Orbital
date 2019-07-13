package orbital.gns.pocketalert.Others


import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.individual_row_for_call.view.*
import orbital.gns.pocketalert.R

class UserCall(val user : User) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_name.text = user.username

        if (user.profileImageUrl.isNotEmpty()) {
            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.MyprofilePic_for_call)
        }
    }

    override fun getLayout(): Int {
        return R.layout.individual_row_for_call
    }
}