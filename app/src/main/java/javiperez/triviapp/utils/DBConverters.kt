package javiperez.triviapp.utils

import androidx.room.TypeConverter
import org.json.JSONArray
import org.json.JSONException
import java.util.*


object DBConverters {

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Calendar? {

        if(value == null) return null

        val cal = GregorianCalendar()
        cal.timeInMillis = value
        return cal
    }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(timestamp: Calendar?): Long? {

        if(timestamp == null) return null

        return timestamp.timeInMillis
    }

    @TypeConverter
    @JvmStatic
    fun intArraytoJSON(values: IntArray): String? {

        val jsonArray = JSONArray()

        for (value in values) {
            try {
                jsonArray.put(value.toDouble())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return jsonArray.toString()
    }

    @TypeConverter
    @JvmStatic
    fun intArrayFromJSON(value: String?): IntArray? {
        try {
            val jsonArray = JSONArray(value)
            val intArray = IntArray(jsonArray.length())
            for (i in 0 until jsonArray.length()) {
                intArray[i] = jsonArray.getString(i).toInt()
            }
            return intArray
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }
}