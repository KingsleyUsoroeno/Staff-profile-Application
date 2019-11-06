package com.techgroup.social_vue.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.techgroup.social_vue.data.model.Staff

@Dao
interface StaffDao {
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertStaff(staff: Staff): Long
	
	@Delete
	fun deleteStaff(staff: Staff)
	
	@Query("SELECT * FROM staff ORDER BY id ASC")
	fun getAllStaff(): LiveData<List<Staff>>
	
	@Query("UPDATE staff SET firstName=:firstName WHERE id = :id AND stateOfOrigin = :stateOfOrigin AND email = :email")
	fun updateStaff(id: Int, firstName: String, email: String, stateOfOrigin: String)
	
	
	@Query("DELETE FROM staff")
	fun deleteAllStaff()
}