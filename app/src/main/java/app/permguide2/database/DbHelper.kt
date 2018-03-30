package app.permguide2.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import app.permguide2.Context.model.ShowPlace
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.COLUMN_DESCRIPTION
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.COLUMN_ID
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.COLUMN_IMAGE
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.COLUMN_LOCATION_X
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.COLUMN_LOCATION_Y
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.COLUMN_NAME
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.COLUMN_SHOWPLACE_TYPE
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.TABLE_NAME
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.COLUMN_VISITED
import java.io.ByteArrayOutputStream


/**
 * Created by burla on 18.03.2018.
 */

const val PRIMARY_KEY = " PRIMARY KEY"
const val AUTOINCREMENT = " AUTOINCREMENT"
const val INT_TYPE = " INTEGER"
const val TEXT_TYPE = " TEXT"
const val IMAGE_TYPE = " BLOB"  // binary large object
const val COMMA_SEP = ", "


private const val SQL_CREATE_SHOWPLACE =
        " CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_ID + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                COLUMN_IMAGE + IMAGE_TYPE + COMMA_SEP +
                COLUMN_LOCATION_X + INT_TYPE + COMMA_SEP +
                COLUMN_LOCATION_Y + INT_TYPE + COMMA_SEP +
                COLUMN_SHOWPLACE_TYPE + TEXT_TYPE + COMMA_SEP +
                COLUMN_VISITED + INT_TYPE + " ) "

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

private const val SQL_CREATE_ENTRIES = SQL_CREATE_SHOWPLACE


class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    fun insertData(showPlace: ShowPlace) {
        if (showPlace in allData)
            return
        val values = ContentValues()
        with(values) {
            put(COLUMN_ID, showPlace.id)
            put(COLUMN_NAME, showPlace.name)
            put(COLUMN_DESCRIPTION, showPlace.description)
            put(COLUMN_SHOWPLACE_TYPE, showPlace.type)
            put(COLUMN_IMAGE, showPlace.image?.toByteArray())
            put(COLUMN_LOCATION_X, showPlace.locationLat)
            put(COLUMN_LOCATION_Y, showPlace.locationLong)
            put(COLUMN_VISITED, showPlace.visited)
        }

        this.writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun deleteData(showPlace: ShowPlace?) {
       // this.writableDatabase.delete(TABLE_NAME, "$COLUMN_ID == ${showPlace?.id}", null)
        this.writableDatabase.execSQL(" DELETE FROM $TABLE_NAME WHERE $COLUMN_ID == '${showPlace?.id}'")
    }

    fun contains(showPlace: ShowPlace): Boolean {
        for (a in allData)
            if (a.id == showPlace.id) return true
        return false
    }

    fun visited(showPlace: ShowPlace, visited: Int){
        if (contains(showPlace))
            deleteData(showPlace)
        insertData(showPlace)
    }

    fun updateShowPlace(showPlace: ShowPlace) {
        if (contains(showPlace))
            deleteData(showPlace)
        insertData(showPlace)
    }

    fun recreate(db: SQLiteDatabase) {
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    val allData: ArrayList<ShowPlace>
        get() {
            val db = this.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
            var showPlaces = ArrayList<ShowPlace>()
            while (cursor.moveToNext())
                showPlaces.add(
                        ShowPlace(
                                cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                                cursor.getFloat(cursor.getColumnIndex(COLUMN_LOCATION_X)),
                                cursor.getFloat(cursor.getColumnIndex(COLUMN_LOCATION_Y)),
                                cursor?.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))?.toBitmap(),
                                cursor.getString(cursor.getColumnIndex(COLUMN_SHOWPLACE_TYPE)),
                                cursor.getInt(cursor.getColumnIndex(COLUMN_VISITED))
                        )
                )
            cursor.close()
            db.close()
            return showPlaces
        }


    private fun Bitmap?.toByteArray(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        this?.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream?.toByteArray()
    }

    private fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "ShowPlacesDB.db"
    }
}

