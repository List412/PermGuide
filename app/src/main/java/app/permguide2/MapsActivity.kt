package app.permguide2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val cemetery = LatLng(58.0105464,56.259627)
        val museum = LatLng(58.0097555,56.2391954)
        val perm = LatLng(58.0046382,56.194254)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                museum, 2f))

        mMap.addMarker(MarkerOptions().title("Парк декабристов").position(cemetery))
        mMap.addMarker(MarkerOptions().title("Музей пермских древностей").position(museum))
        mMap.addMarker(MarkerOptions().title("Пермские ворота").position(perm))

        // Polylines are useful for marking paths and routes on the map.
        mMap.addPolyline(PolylineOptions().geodesic(true)
                .add(cemetery)  // Sydney
                .add(museum)  // Fiji
                .add(perm)  // Hawaii
        )
    }

}
