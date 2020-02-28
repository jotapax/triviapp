package javiperez.triviapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import javiperez.triviapp.utils.DBConverters
import kotlinx.android.parcel.Parcelize
import java.util.*

@TypeConverters(DBConverters::class)
@Entity(tableName = "score")
@Parcelize
data class Score(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val mode : Int,
    val points : Int,
    val totalPoints : Int,
    val percentage : Int,
    val numberQuestions : Int,
    val categoryId: Int,
    val difficulty : String,
    val correct : IntArray,
    val wrong : IntArray,
    val noAnswer : IntArray,
    val calendar : Calendar
) : Parcelable {

    companion object {
        const val NORMAL_MODE = 0
        const val COMPETITION_MODE = 1
    }
}