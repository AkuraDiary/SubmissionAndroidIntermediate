package com.asthiseta.submissionintermediate.ui.maps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.asthiseta.submissionintermediate.R
import com.asthiseta.submissionintermediate.data.preferences.DataStoreVM
import com.asthiseta.submissionintermediate.databinding.ActivityStoryMapsBinding
import com.asthiseta.submissionintermediate.ui.activities.MainActivity
import com.asthiseta.submissionintermediate.ui.addStory.UploadStoryActivity.Companion.MY_LOCATION_TO_SHARE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val dataStoreVm by viewModels<DataStoreVM>()
    private val mapsVM by viewModels<MapsVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }



    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
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

        setMyMapStyle()
        getMyLocation()
        showStoriesMarker()

        if (intent.getIntExtra("UPLOAD_REQUEST_CODE", 0) == MY_LOCATION_TO_SHARE) {
            getMyLatlon()
            intent.removeExtra("UPLOAD_REQUEST_CODE")
        }

    }

    private fun setMyMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle))
            if (!success) {
                Log.e("Maps Activity", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("Maps Activity", "Can't find style. Error: ", exception)
        }
    }

    private fun checkForPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocation()
                }
                else -> {
                    // No location access granted.
                    FancyToast.makeText(
                        this,
                        "Location permission not granted",
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,
                        false
                    ).show()
                }
            }
        }



    private fun getMyLocation() {
        if (
            checkForPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            &&
            checkForPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            mMap.isMyLocationEnabled = true


        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private fun getMyLatlon() {
        if (
            checkForPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            &&
            checkForPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object  : CancellationToken() {
                    override fun onCanceledRequested(onTokenCanceledListener: OnTokenCanceledListener): CancellationToken {
                        Log.d("Cancelled", onTokenCanceledListener.toString())
                        return this
                    }

                    override fun isCancellationRequested(): Boolean {
                        return false
                    }
                }
            ).addOnCompleteListener{
                val myLatitude = it.result.latitude
                val myLongitude = it.result.longitude

                val output = Intent()
                output.putExtra("latitude", myLatitude)
                output.putExtra("longitude", myLongitude)
                setResult(Activity.RESULT_OK, output)
                finish()
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }



    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)

    }

    private val boundsBuilder = LatLngBounds.Builder()

    private fun showStoriesMarker() {
        dataStoreVm.getLoginSession().observe(this) {
            mapsVM.getStoriesWithLocations(it.token)
        }

        mapsVM.storyList.observe(this) {
            for (i in it.listIterator()) {

                val latLng = LatLng(i.lat, i.lon)
                if (latLng.latitude != 0.0 && latLng.longitude != 0.0) {
                    boundsBuilder.include(latLng)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(i.name)
                            .snippet(i.description)
                    )
                }

            }
            try {
                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
            } catch (e: Exception) {
                FancyToast.makeText(
                    this,
                    e.message,
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }

        }
    }

}