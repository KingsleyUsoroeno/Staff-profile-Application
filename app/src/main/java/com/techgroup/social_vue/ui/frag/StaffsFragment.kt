package com.techgroup.social_vue.ui.frag


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.techgroup.social_vue.R
import com.techgroup.social_vue.data.StaffViewModel
import com.techgroup.social_vue.data.ViewModelFactoryProvider
import com.techgroup.social_vue.data.db.StaffDatabase
import com.techgroup.social_vue.data.model.Staff
import com.techgroup.social_vue.databinding.FragmentStaffsBinding
import com.techgroup.social_vue.ui.StaffRecyclerAdapter


/**
 * A simple [Fragment] subclass.
 */
class StaffsFragment : Fragment(), StaffRecyclerAdapter.OnClickListener {
	
	private lateinit var viewBinding: FragmentStaffsBinding
	private lateinit var viewModel: StaffViewModel
	
	override fun onCreateView(
				inflater: LayoutInflater, container: ViewGroup?,
				savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_staffs, container, false)
		val viewModelFactoryProvider =
					ViewModelFactoryProvider(StaffDatabase.getDatabase(this.context!!))
		viewModel =
					ViewModelProviders.of(this, viewModelFactoryProvider).get(StaffViewModel::class.java)
		return viewBinding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val staffAdapter = StaffRecyclerAdapter()
		staffAdapter.setonClickListener(this)
		viewBinding.staffsRecyclerView.addItemDecoration(
					DividerItemDecoration(
								this.context, DividerItemDecoration.VERTICAL
					)
		)
		
		viewModel.getAllStaff().observe(this, Observer {
			if (it.isNotEmpty()) {
				staffAdapter.submitList(it)
				viewBinding.staffsRecyclerView.adapter = staffAdapter
			}
		})
		
		viewBinding.addStaff.setOnClickListener {
			it.findNavController().navigate(R.id.action_staffsFragment_to_signUpFragment)
		}
	}
	
	override fun onDeleteClicked(staff: Staff) {
		viewModel.deleteStaff(staff)
		showToast("Staff deleted successfully")
	}
	
	override fun onUpdateClicked(staff: Staff) {
		createAlertDialog(staff)
	}
	
	private fun showToast(msg: String) {
		Toast.makeText(context!!, msg, Toast.LENGTH_SHORT).show()
	}
	
	private fun createAlertDialog(staff: Staff) {
		val builder = AlertDialog.Builder(context!!)
		builder.setTitle("Update ${staff.firstName} Profile")
		builder.setMessage("These is a secure channel")
//		val layoutView = LayoutInflater.from(context).inflate(R.layout.update_staff_layout, null)
//		builder.setView(layoutView)
		builder.setCancelable(false)
		builder.setPositiveButton("Update") { dialog, which ->
			// Do something when click positive button
		}
		
		builder.setNegativeButton("Cancel") { dialog, _ ->
			dialog.dismiss()
		}
		val dialog = builder.create()
		// Display the alert dialog on interface
		dialog.show()
	}
}
