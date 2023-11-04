package tn.esprit.greenworld.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tn.esprit.greenworld.dao.ProduitDao
import tn.esprit.greenworld.models.Produit

@Database(entities = [Produit::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {


    abstract fun produitDao(): ProduitDao

    companion object {
        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "db_produit").allowMainThreadQueries().build()
        }

    }
}