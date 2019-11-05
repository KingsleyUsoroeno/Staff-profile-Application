package com.techgroup.social_vue.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.techgroup.social_vue.data.db.StaffDatabase
import com.techgroup.social_vue.data.model.Staff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StaffViewModel(private val database: StaffDatabase) : ViewModel() {

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    fun insertStaff(staff: Staff) {
        viewModelScope.launch {
            database.staffDao().insertStaff(staff)
        }
    }

    fun getAllStaff(): LiveData<List<Staff>> {
        return database.staffDao().getAllStaff()
    }
}

class ViewModelFactoryProvider(private val database: StaffDatabase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StaffViewModel::class.java)) {
            return StaffViewModel(database) as T
        } else {
            throw IllegalArgumentException("Unknown viewModel")
        }
    }

}