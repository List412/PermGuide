package app.permguide2

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.view.View
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import app.permguide2.Context.model.ShowPlace
import android.net.Uri
import android.os.AsyncTask
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import android.widget.*
import app.permguide2.database.DbContract.ShowplaceEntry.Companion.TABLE_NAME
import app.permguide2.database.DbHelper
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.*
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnConnectionFailedListener {

    private val PLACE_PICKER_REQUEST = 1
    private val QR_SCANNER_REQUEST = 0
    private val GOOGLE_API_CLIENT_ID = 0
    private val rnd = Random()

    /**
     * Handles autocomplete requests.
     */
    private lateinit var googleApiClient: GoogleApiClient

    private var mResultList: ArrayList<ShowPlace>? = null
    private var filterTypes: ArrayList<String>? = null

    private val mBounds: LatLngBounds? = null
    private val mPlaceFilter: AutocompleteFilter? = null
    private var db: DbHelper? = null

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        mResultList = ArrayList()
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()

        filterTypes = ArrayList()

        filterTypes?.add("art_gallery")
        filterTypes?.add("museum")
        filterTypes?.add("church")
        filterTypes?.add("amusement_park")
        filterTypes?.add("park")
        filterTypes?.add("cemetery")
        filterTypes?.add("church")
        filterTypes?.add("city_hall")
        filterTypes?.add("embassy")
        filterTypes?.add("hindu_temple")
        filterTypes?.add("mosque")
        filterTypes?.add("painter")
        filterTypes?.add("synagogue")

        loadPlaces()


        db = DbHelper(this)
        //db?.recreate(db?.writableDatabase!!)
        db?.close()

        //db?.writableDatabase?.delete(TABLE_NAME, null, null)
//
//        for (a in 1..10)
//        db.insertData(ShowPlace(0, "$a place", "long $a description", 12f, -45f, null, "museum"))
//
//        db.allData.forEach { value -> addCard(value) }

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun loadPlaces() {


        var resultList = ArrayList<ShowPlace>()

        for (type in filterTypes!!) {

            var southwest = LatLng(57.230407, 55.388949)
            var northeast = LatLng(58.631843, 57.251615)

            val mPlaceFilter = AutocompleteFilter.Builder()

            var bounds = LatLngBounds(southwest, northeast)
            var results = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, type, bounds, null)


            results.setResultCallback { it ->
                run {
                    for (a in it) {

                        var places = Places.GeoDataApi.getPlaceById(googleApiClient, a.placeId)

                        places.setResultCallback { res ->

                            run {
                                if ("Пермский край" in res[0].address) {
                                    var showPlace = ShowPlace(res[0].id, res[0].name.toString(), res[0].address.toString(), 1f, 1f, placePhotoAsync(res[0].id, null), type.toRus(), 0)
                                    if (!contains(resultList, showPlace)) {
                                        //db?.insertData(showPlace)
                                        resultList.add(showPlace)
                                        addCard(showPlace)
                                    }
                                }
                                res.release()
                            }
                        }
                    }
                    it.release()
                }
            }


            // results.setResultCallback{placeResult(it)}

        }
        mResultList?.addAll(resultList)
    }

    private fun contains(list: ArrayList<ShowPlace>, showPlace: ShowPlace): Boolean {
        for (a in list)
            if (a.id == showPlace.id) return true
        return false
    }

    private fun String.toRus(): String {
        when (this) {
            "museum" -> return "Музей"
            "amusement_park" -> return "Парк развлечений"
            "art_gallery" -> return "Художественная галерея"
            "park" -> return "Парк"
            "cemetery" -> return "Кладбище"
            "church" -> return "Церковь"
            "city_hall" -> return "Ратуша"
            "embassy" -> return "Посольство"
            "hindu_temple" -> return "Индуистский храм"
            "mosque" -> return "Мечеть"
            "painter" -> return "Художник"
            "synagogue" -> return "Синагога"
            "zoo" -> return "Зоопарк"
        }
        return this
    }

    private fun placePhotosTask(showplace: ShowPlace): Bitmap? {
        var bitmap = PhotoTask(googleApiClient).execute(showplace.id).get()
        if (bitmap != null) showplace.image = bitmap
        return bitmap
    }

    private fun placePhotoAsync(id: String, view: View?): Bitmap? {
        var bitmap: Bitmap? = null
        var photos = Places.GeoDataApi.getPlacePhotos(googleApiClient, id)
        photos.setResultCallback { image ->
            if (image.status.isSuccess && image.photoMetadata.count > 0)
                image.photoMetadata[rnd.nextInt(image.photoMetadata.count)].getPhoto(googleApiClient).setResultCallback { photoData ->
                    if (photoData.status.isSuccess) {
                        bitmap = photoData.bitmap
                        var img = view?.findViewById<ImageView>(R.id.image)
                        img?.setImageBitmap(bitmap)
                    }
                }
        }

        return bitmap
    }

    private fun placeResult(value: AutocompletePredictionBuffer) {
        var count = value.count

        for (p in value) {
            var place = Places.GeoDataApi.getPlaceById(googleApiClient, p.placeId)
            place.setResultCallback {
                var showPlace = ShowPlace(it[0].id, it[0].name.toString(), it[0].address.toString(), 1f, 1f, null, "", 0)
                mResultList?.add(showPlace)
                addCard(showPlace)
            }
        }
    }

    private fun placeAgain(value: PlaceBuffer) {
        var count = value.count
        var place = value[0]

        var showPlace = ShowPlace(place.id, place.name.toString(), place.address.toString(), 1f, 1f, null, "", 0)
        mResultList?.add(showPlace)
        addCard(showPlace)
    }


    private fun addCard(showPlace: ShowPlace) {

        val linLayout = findViewById<LinearLayout>(R.id.contentPanel)

        val ltInflater = layoutInflater

        var item = ltInflater.inflate(R.layout.sample_content_shower, linLayout, false)

        var txtName = item.findViewById<TextView>(R.id.name)
        var txtDescription = item.findViewById<TextView>(R.id.description)
        var imageView = item.findViewById<ImageView>(R.id.image)
        var txtType = item.findViewById<TextView>(R.id.type)


        var bitmap = placePhotosTask(showPlace)

        showPlace.image = bitmap
        db?.updateShowPlace(showPlace)
        db?.close()
        val clickListener = View.OnClickListener { view ->
            if (view is ImageView) onImagePressed((item.findViewById(R.id.image) as ImageView).drawingCache, showPlace) else onCardPressed(showPlace)
        }
        imageView.setOnClickListener(clickListener)
        item.setOnClickListener(clickListener)
        if (showPlace.image != null)
            imageView.setImageBitmap(showPlace.image)
        txtName.text = showPlace.name
        txtDescription.text = showPlace.description
        txtType.text = showPlace.type

        linLayout.addView(item)
    }

    private fun onCardPressed(showPlace: ShowPlace) {
        val card = Intent(applicationContext, CardShowActivity::class.java)
        card.putExtra("cardID", showPlace.id)
        startActivity(card)
    }

    fun onImagePressed(image: Bitmap?, showPlace: ShowPlace) {

        var bitmap = image

        if (image == null) {
            bitmap = placePhotosTask(showPlace)
        }

        var builder = AlertDialog.Builder(this)
        var alert = builder.create()

        val ltInflater = layoutInflater

        val item = ltInflater.inflate(R.layout.alert_dialog_photo_viewer, null)
        var imageView = item.findViewById<ImageView>(R.id.image)
        imageView.setImageBitmap(bitmap)
        alert.setView(item)
        alert.show()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> displayQRReader()
            R.id.map -> displayPlacePicker()
            R.id.nav_slideshow -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displayQRReader() {
        try {
            val intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            startActivityForResult(intent, 0)
        } catch (e: Exception) {
            val marketUri = Uri.parse("market://details?id=com.google.zxing.client.android")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        }
    }

    private fun displayPlacePicker() {
        if (googleApiClient == null || !googleApiClient.isConnected)
            return

        var intent = PlacePicker.IntentBuilder()

        try {
            startActivityForResult(intent.build(this), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PLACE_PICKER_REQUEST -> if (resultCode == -1) {
                var place = PlacePicker.getPlace(data, this)
                Toast.makeText(this, place.name.toString(), Toast.LENGTH_LONG).show()
            }
            QR_SCANNER_REQUEST -> if (resultCode == -1) {
                Toast.makeText(this, data?.getStringExtra("SCAN_RESULT"), Toast.LENGTH_LONG).show()
            }
        }
    }
}


class PhotoTask : AsyncTask<String, Void, Bitmap?> {
    override fun doInBackground(vararg params: String?): Bitmap? {
        var placeID = params[0]!!
        var result: PlacePhotoMetadataResult = Places.GeoDataApi.getPlacePhotos(googleApiClient, placeID).await()
        var bitmap: Bitmap? = null


        if (result.status.isSuccess) {

            var photoMetadataBuffer = result.photoMetadata

            if (photoMetadataBuffer.count > 0 && !isCancelled) {

                var photo = photoMetadataBuffer.get(0)
                bitmap = photo.getScaledPhoto(googleApiClient, 600, 600).await().bitmap
            }
            photoMetadataBuffer.release()
        }
        return bitmap
    }

    private var googleApiClient: GoogleApiClient

    constructor  (googleApiClient: GoogleApiClient) {
        this.googleApiClient = googleApiClient
    }
}



