package com.example.wakemeup.ui.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ItemMemberBinding
import com.example.wakemeup.domain.FriendModel
import java.text.SimpleDateFormat
import java.util.Date

class MemberAdapter(): RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    private val data: ArrayList<FriendModel> = ArrayList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMemberBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.friendName.text = data[position].name
        holder.binding.profilePic.setImageBitmap(data[position].profilePic)
        val sdf = SimpleDateFormat("HH:mm")
        holder.binding.wakeupTime.text = sdf.format(data[position].wakingTime.time)
        if (data[position].isAwake) {
            holder.binding.wakeupTime.visibility = View.INVISIBLE
            holder.binding.wakeUpStatus.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.context, R.drawable.open_eye))
        } else {
            holder.binding.wakeupTime.visibility = View.VISIBLE
            holder.binding.wakeUpStatus.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.context, R.drawable.closed_eye))
        }
    }

    fun setData(data: List<FriendModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}