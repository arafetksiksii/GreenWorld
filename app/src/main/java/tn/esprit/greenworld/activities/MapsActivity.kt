package tn.esprit.greenworld.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tn.esprit.greenworld.R

class MapsActivity : AppCompatActivity() {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var buttonZoomIn: Button
    private lateinit var buttonZoomOut: Button
    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val  mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        buttonZoomIn = findViewById(R.id.buttonZoomIn)
        buttonZoomOut = findViewById(R.id.buttonZoomOut)

        mapFragment.getMapAsync { map ->
            googleMap = map

            val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
            val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)

            val location = LatLng(latitude, longitude)
            map.addMarker(MarkerOptions().position(location).title("Marker in Location"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

            buttonZoomIn.setOnClickListener {
                googleMap.animateCamera(CameraUpdateFactory.zoomIn())
            }

            buttonZoomOut.setOnClickListener {
                googleMap.animateCamera(CameraUpdateFactory.zoomOut())
            }
        }
    }
}