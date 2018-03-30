package app.permguide2

import android.graphics.Bitmap
import android.os.AsyncTask
import app.permguide2.Context.model.ShowPlace
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

/**
 * Created by Evgeniy Burlakov on 28.03.2018.
 */
class AsyncLoad(var context: MainActivity) : AsyncTask<Unit, Unit, ArrayList<ShowPlace>>() {

    private lateinit var googleApiClient: GoogleApiClient
    var resultList = ArrayList<ShowPlace>()

    override fun doInBackground(vararg params: Unit?): ArrayList<ShowPlace>? {

        googleApiClient = GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(context, context)
                .build()

        loadPlaces()

        return resultList
    }

    private fun loadPlaces() {
        //PendingResult<AutocompletePredictionBuffer>

        var filterTypes = ArrayList<String>()
        filterTypes.add("museum")
        filterTypes.add("amusement_park")
        filterTypes.add("art_gallery")
        filterTypes.add("park")


        var photosMap = HashMap<String, Bitmap>()

        filterTypes.forEach {
            var bounds = LatLngBounds(LatLng(55.9904836, 56.0197154), LatLng(59.0407044, 56.6799287))
            var results = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, it, null, null)

            results.setResultCallback { value ->
                for (a in value) {

                    var places = Places.GeoDataApi.getPlaceById(googleApiClient, a.placeId)
                    var photos = Places.GeoDataApi.getPlacePhotos(googleApiClient, a.placeId!!)

                    places.setResultCallback { res ->
                        res.mapTo(resultList) { p -> ShowPlace(p.id, p.name.toString(), p.address.toString(), 1f, 1f, null, it, 0) }
                    }

                    photos.setResultCallback { res ->
                        for (photo in res.photoMetadata)
                            photosMap[a.placeId.toString()] = photo.getPhoto(googleApiClient).await().bitmap
                    }
                }
            }
        }
    }


    override fun onPreExecute() {
        super.onPreExecute()
        //
    }

    override fun onPostExecute(result: ArrayList<ShowPlace>?) {
        super.onPostExecute(result)



    }

}