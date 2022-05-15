package ru.pixelshop.database.data.database

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.pixelshop.PixelshopApp
import ru.pixelshop.R
import ru.pixelshop.database.data.dao.ProjectDao
import ru.pixelshop.database.data.entities.ProjectEntity
import ru.pixelshop.database.data.entities.TemplateEntity
import ru.pixelshop.utils.BitmapUtilities.Companion.toBase64

@Database(
    entities = [ProjectEntity::class, TemplateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: ProjectDatabase? = null

        private const val DATABASE_NAME = "projects_database"

        fun getInstance(application: Application): ProjectDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(application).also { INSTANCE = it }
            }

        private fun buildDatabase(application: Application) = Room.databaseBuilder(
            application,
            ProjectDatabase::class.java,
            DATABASE_NAME
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val handler = CoroutineExceptionHandler { _, exception ->
                    Log.i("ERROR", "Caught during database creation --> $exception")
                }

                CoroutineScope(Dispatchers.IO).launch(handler) {
                    prePopulateAppDatabase(getInstance(application = application).projectDao())
                }
            }
        }).build()

        suspend fun prePopulateAppDatabase(projectDao: ProjectDao) {
            val items = listOf(
                Pair("Mario", R.drawable.mario_templates),
                Pair("Alex", R.drawable.alex_template),
                Pair("Steve", R.drawable.steve_template),
                Pair("Zombie", R.drawable.zombie_template),
                Pair("Apple", R.drawable.apple_template),
                Pair("Bookshelf", R.drawable.bookshelf_template),
                Pair("Map", R.drawable.map_template)
            )

            for (item in items) {
                val bitmap = (ResourcesCompat.getDrawable(
                    PixelshopApp.context.resources,
                    item.second,
                    null
                ) as BitmapDrawable).bitmap
                projectDao.insertTemplate(
                    TemplateEntity(
                        name = item.first,
                        width = bitmap.width,
                        height = bitmap.height,
                        matrix = bitmap.toBase64()
                    )
                )
            }
        }
    }
}
