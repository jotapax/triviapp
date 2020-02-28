package javiperez.triviapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "category")
data class Category (

    @PrimaryKey val id : Int,
    val easy : Int,
    val medium : Int,
    val hard : Int,
    val name : String,
    val color : String,
    val icon : Int
) : Parcelable