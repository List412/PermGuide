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
import java.io.ByteArrayOutputStream


/**
 * Created by burla on 18.03.2018.
 */

const val PRIMARY_KEY = " PRIMARY KEY"
const val AUTOINCREMENT = " AUTOINCREMENT"
const val INT_TYPE = " INTEGER"
const val TEXT_TYPE = " TEXT"
const val IMAGE_TYPE = " BLOB"  // binary large object
const val COMMA_SEP = ","


private const val SQL_CREATE_SHOWPLACE =
        "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + INT_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                COLUMN_IMAGE + IMAGE_TYPE + COMMA_SEP +
                COLUMN_LOCATION_X + INT_TYPE + COMMA_SEP +
                COLUMN_LOCATION_Y + INT_TYPE + COMMA_SEP +
                COLUMN_SHOWPLACE_TYPE + TEXT_TYPE +  " )"

private const val SQL_CREATE_ENTRIES = SQL_CREATE_SHOWPLACE

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"




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

    fun insertData(showPlace: ShowPlace) : Int
    {
        val values = ContentValues()
        with(values) {
            put(COLUMN_NAME, showPlace.name)
            put(COLUMN_DESCRIPTION, showPlace.description)
            put(COLUMN_SHOWPLACE_TYPE, showPlace.type)
            put(COLUMN_IMAGE, showPlace.image.toByteArray())
            put(COLUMN_LOCATION_X, showPlace.locationX)
            put(COLUMN_LOCATION_Y, showPlace.locationY)
        }

        this.writableDatabase.insert(TABLE_NAME, null, values)
        var db = this.readableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC LIMIT 5"
        var cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        return cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
    }

    val allData : ArrayList<ShowPlace>
        get(){
            val db = this.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME",null)
            var showPlaces = ArrayList<ShowPlace>()
            while (cursor.moveToNext())
                showPlaces.add(
                        ShowPlace(
                                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                                cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_X)),
                                cursor.getInt(cursor.getColumnIndex(COLUMN_LOCATION_Y)),
                                cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)).toBitmap(),
                                cursor.getString(cursor.getColumnIndex(COLUMN_SHOWPLACE_TYPE))
                        )
                )
            return showPlaces
        }


    private fun Bitmap.toByteArray() : ByteArray {
        val outputStream = ByteArrayOutputStream()
        this.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    private fun ByteArray.toBitmap() : Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ShowPlacesDB.db"
    }
}

