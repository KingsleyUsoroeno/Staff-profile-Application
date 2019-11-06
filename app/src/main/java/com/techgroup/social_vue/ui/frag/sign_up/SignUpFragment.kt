package com.techgroup.social_vue.ui.frag.sign_up


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.squareup.picasso.Picasso
import com.techgroup.social_vue.Constant
import com.techgroup.social_vue.Constant.GALLERY_PERMISSION_CODE
import com.techgroup.social_vue.Constant.GALLERY_REQUEST_CODE
import com.techgroup.social_vue.R
import com.techgroup.social_vue.data.SharedPreferenceHelper
import com.techgroup.social_vue.data.StaffViewModel
import com.techgroup.social_vue.data.ViewModelFactoryProvider
import com.techgroup.social_vue.data.db.StaffDatabase
import com.techgroup.social_vue.databinding.FragmentSignUpBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 *
 */
class SignUpFragment : Fragment(), SignUpView {
	
	private lateinit var signUpFragmentBinding: FragmentSignUpBinding
	private lateinit var navController: NavController
	private lateinit var signUpPresenter: SignUpPresenter
	private val config = HashMap<String, String>()
	private lateinit var staffViewModel: StaffViewModel
	
	companion object {
		const val TAG = "SignUpFragment"
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}
	
	override fun onCreateView(
				inflater: LayoutInflater, container: ViewGroup?,
				savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		signUpFragmentBinding =
					DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
		navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
		
		val viewModelFactoryProvider =
					ViewModelFactoryProvider(StaffDatabase.getDatabase(this.context!!))
		
		staffViewModel =
					ViewModelProviders.of(this, viewModelFactoryProvider).get(StaffViewModel::class.java)
		signUpPresenter = SignUpPresenter(this, staffViewModel)
		
		signUpFragmentBinding.imgSelectProfile.setOnClickListener {
			startGalleryIntent()
		}
		
		signUpFragmentBinding.btnSignUp.setOnClickListener {
			signUpPresenter.saveUser()
		}
		
		return signUpFragmentBinding.root
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.staff_meu, menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.action_view_all_staff) {
			navController.navigate(R.id.staffsFragment)
		}
		return true
	}
	
	override fun getFullName(): String {
		return signUpFragmentBinding.fullNameEditText.text.toString().trim()
	}
	
	override fun getEmailAddress(): String {
		return signUpFragmentBinding.emailEditText.text.toString().trim()
	}
	
	override fun getStateOfOrigin(): String {
		return signUpFragmentBinding.stateOfOriginEditText.text.toString().trim()
	}
	
	override fun getDob(): String {
		val dayOfMonth = signUpFragmentBinding.dobDatePicker.dayOfMonth
		val year = signUpFragmentBinding.dobDatePicker.year
		val month = signUpFragmentBinding.dobDatePicker.month
		return getUserDob(month, dayOfMonth, year)
	}
	
	override fun getUserImage(): String? {
		return SharedPreferenceHelper.getString(this.context!!, Constant.PROFILE_IMAGE, "")
	}
	
	override fun navigate(direction: Int) {
		navController.navigate(direction)
	}
	
	override fun showError(error: String) {
		showToast(error)
	}
	
	override fun showSuccessMsg(error: String) {
		showToast(error)
	}
	
	private fun startGalleryIntent() {
		if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED
		) {
			ActivityCompat.requestPermissions(
						this.activity!!,
						arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
						GALLERY_PERMISSION_CODE
			)
		} else {
			pickFromGallery()
		}
	}
	
	private fun pickFromGallery() {
		//Create an Intent with action as ACTION_PICK
		val intent = Intent(Intent.ACTION_PICK)
		// Sets the type as image/*. This ensures only components of type image are selected
		intent.type = "image/*"
		// Launching the Intent
		startActivityForResult(intent, GALLERY_REQUEST_CODE)
	}
	
	override fun onRequestPermissionsResult(
				requestCode: Int,
				permissions: Array<out String>,
				grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		when (requestCode) {
			GALLERY_PERMISSION_CODE -> {
				if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(
								this.activity!!,
								arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
								GALLERY_REQUEST_CODE
					)
				} else {
					pickFromGallery()
				}
			}
		}
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK && data!!.data != null) {
			when (requestCode) {
				GALLERY_REQUEST_CODE -> {
					loadImage(data.data!!)
					val filePath = getRealPathFromURIPath(data.data!!, this.activity!!)
					GlobalScope.launch {
						uploadUserImage(File(filePath))
					}
				}
			}
		}
	}
	
	private fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String {
		val cursor = activity.contentResolver.query(contentURI, null, null, null, null)
		return if (cursor == null) {
			contentURI.path!!
		} else {
			cursor.moveToFirst()
			val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
			cursor.getString(idx)
		}
	}
	
	private fun uploadUserImage(imageFile: File) {
		config[Constant.CLOUD_NAME] = Constant.NAME
		config[Constant.API_KEY] = Constant.KEY
		config[Constant.API_SECRET] = Constant.SECRET
		
		val cloudinary = Cloudinary(config)
		try {
			Handler(Looper.getMainLooper()).post {
				signUpFragmentBinding.progressBar.visibility = View.VISIBLE
			}
			val uploadRes = cloudinary.uploader().upload(imageFile, ObjectUtils.emptyMap())
			val imageUrl = uploadRes["secure_url"].toString()
			SharedPreferenceHelper.setString(this.context!!, Constant.PROFILE_IMAGE, imageUrl)
			Log.i(TAG, "map object is $imageUrl")
			
			Handler(Looper.getMainLooper()).post {
				signUpFragmentBinding.progressBar.visibility = View.GONE
			}
			
		} catch (e: IOException) {
			Log.i(TAG, "exception caught while trying to load image ${e.message}")
			Handler(Looper.getMainLooper()).post {
				signUpFragmentBinding.progressBar.visibility = View.GONE
			}
		}
	}
	
	private fun showToast(error: String) {
		Toast.makeText(this.context, error, Toast.LENGTH_LONG).show()
	}
	
	private fun getUserDob(month: Int, dayOfMonth: Int, year: Int): String {
		val cal = Calendar.getInstance()
		cal.set(Calendar.YEAR, year)
		cal.set(Calendar.MONTH, month)
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
		val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
		return simpleDateFormat.format(cal.time)
	}
	
	private fun loadImage(imageUri: Uri) {
		Picasso.get()
					.load(imageUri)
					.placeholder(R.drawable.loading_animation)
					.into(signUpFragmentBinding.imgContactImage)
	}
}


