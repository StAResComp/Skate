package uk.ac.standrews.skate.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import uk.ac.standrews.skate.db.entities.*
import java.util.*

@Database(
    entities = [
        Effort::class,
        Species::class,
        Summary::class,
        Individual::class,
        Photo::class
    ],
    version = 1
)
abstract class SkateDatabase : RoomDatabase() {
    abstract fun skateDao(): SkateDao
}

/**
 * Converter for handling dates.
 *
 * Converts [Date] to [Long] timestamp and vice-versa. [Date] is used in the application code,
 * but dates are stored as [Long] in SQLite
 */
class DateTypeConverter {

    /**
     * Converts [Long] timestamp to [Date]
     *
     * @param ts timestamp to be converted
     * @return corresponding [Date]
     */
    @TypeConverter
    fun fromTimestamp(ts: Long?): Date? {
        if (ts != null) return Date(ts)
        else return null
    }

    /**
     * Converts [Date] to [Long] timestampe
     *
     * @param date [Date] to be converted
     * @return corresponding [Long] timestamp
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time;
    }

}