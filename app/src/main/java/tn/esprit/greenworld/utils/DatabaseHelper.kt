package tn.esprit.greenworld.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import tn.esprit.greenworld.models.UserR


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDatabase"
        private const val TABLE_USER = "user"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_REMEMBER_ME = "remember_me"
        private const val COLUMN_PASSWORD = "password"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = (
                "CREATE TABLE $TABLE_USER (" +
                        "$COLUMN_ID INTEGER PRIMARY KEY, " +
                        "$COLUMN_EMAIL TEXT, " +
                        "$COLUMN_PASSWORD TEXT, " +
                        "$COLUMN_REMEMBER_ME INTEGER)"
                )
        db.execSQL(createTable)
    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }


    fun addUser(email: String, password: String, rememberMe: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_REMEMBER_ME, if (rememberMe) 1 else 0)
        }
        db.insert(TABLE_USER, null, values)
        db.close()
    }


    fun updateUser(email: String, rememberMe: Boolean) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_REMEMBER_ME, if (rememberMe) 1 else 0)
        }

        db.update(TABLE_USER, values, "$COLUMN_EMAIL = ?", arrayOf(email))
        db.close()
    }

    fun getUser(email: String): UserR? {
        val db = this.readableDatabase

        val cursor: Cursor? = db.query(
            TABLE_USER, arrayOf(COLUMN_ID, COLUMN_EMAIL, COLUMN_REMEMBER_ME),
            "$COLUMN_EMAIL = ?", arrayOf(email), null, null, null, null
        )

        var user: UserR? = null

        cursor?.use {
            if (it.moveToFirst()) {
                user = UserR(it.getString(1), it.getInt(2) == 1)
            }
        }

        cursor?.close()
        return user
    }




    fun deleteUserCredentials(username: String) {
        val db = this.writableDatabase
        db.delete(TABLE_USER, null, null)
        db.close()
    }

}
