package javiperez.triviapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import javiperez.triviapp.model.Category

@Dao
interface CategoryDao {

    @Insert
    fun insert(category: Category)

    @Query("SELECT * FROM category WHERE name LIKE :name")
    fun get(name : String) : Category

    @Query("SELECT * FROM category WHERE id = :id")
    fun getByID(id : Int) : Category

    @Query("SELECT * FROM category")
    fun getAll() : LiveData<List<Category>>
}
