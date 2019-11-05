package com.techgroup.social_vue.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techgroup.social_vue.R
import com.techgroup.social_vue.data.model.Staff
import com.techgroup.social_vue.databinding.LayoutStaffListBinding
import de.hdodenhof.circleimageview.CircleImageView

class StaffRecyclerAdapter :
    ListAdapter<Staff, StaffRecyclerAdapter.StaffViewHolder>(DiffCallback()) {

    private lateinit var viewBinding: LayoutStaffListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        viewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_staff_list,
            parent,
            false
        )
        return StaffViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        holder.setData(viewBinding, getItem(position))
    }

    inner class StaffViewHolder(viewBinding: LayoutStaffListBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun setData(viewBinding: LayoutStaffListBinding, staff: Staff) {
            viewBinding.staff = staff
            viewBinding.executePendingBindings()
            viewBinding.icMoreVert.setOnClickListener {
                //creating a popup menu
                val popup = PopupMenu(viewBinding.root.context, viewBinding.icMoreVert)
                //inflating menu from xml resource
                popup.inflate(R.menu.recycler_view_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_update -> Toast.makeText(
                            viewBinding.root.context,
                            "Clicked",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    false
                }
                popup.show()
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Staff>() {
    override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("profileImage")
fun setProfileImage(view: CircleImageView, url: String) {
    Glide.with(view.context)
        .asBitmap()
        .load(url)
        .placeholder(R.drawable.loading_animation)
        .into(view)
}

