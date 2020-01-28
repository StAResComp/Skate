package uk.ac.standrews.skate.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import uk.ac.standrews.skate.db.entities.*
import java.util.*
import java.util.concurrent.Executors

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
@TypeConverters(DateTypeConverter::class)
abstract class SkateDatabase : RoomDatabase() {

    abstract fun skateDao() : SkateDao

    companion object {
        @Volatile
        private var INSTANCE: SkateDatabase? = null

        fun getSkateDatabase(context: Context) : SkateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SkateDatabase::class.java,
                    "skate.sqlite"
                )
                    .addCallback(seedDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        fun seedDatabaseCallback(context: Context) : Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadExecutor().execute {
                        val dao = getSkateDatabase(context).skateDao()
                        val currentSpecies = dao.getSpecies()
                        if (currentSpecies.isEmpty()) {
                            val speciesNames = arrayOf(
                                Species(0, "Flapper skate"),
                                Species(0, "Thornback ray"),
                                Species(0, "Spurdog"),
                                Species(0, "Lesser spotted dogfish"),
                                Species(0, "Blackmouth catshark"),
                                Species(0, "Cuckoo Ray"),
                                Species(0, "Bull Huss"),
                                Species(0, "Tope"),
                                Species(0, "Other skate"),
                                Species(0, "Other shark")
                            )
                            dao.insertSpecies(speciesNames)
                        }
                    }
                }
            }
        }

    }

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