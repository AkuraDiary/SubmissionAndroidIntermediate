package com.asthiseta.submissionintermediate.ui.addStory

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.asthiseta.submissionintermediate.data.preferences.DataStoreVM
import com.asthiseta.submissionintermediate.databinding.ActivityUploadStoryBinding
import com.asthiseta.submissionintermediate.ui.activities.MainActivity
import com.asthiseta.submissionintermediate.ui.maps.StoryMapsActivity
import com.asthiseta.submissionintermediate.utilities.UploadStoryUtilities
import com.asthiseta.submissionintermediate.utilities.UploadStoryUtilities.reduceFileImage
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UploadStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStoryBinding
    private lateinit var currentPath: String
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var getFile: File? = null
    private var location: Location? = null
    private var _latitude: Double? = null
    private var _longitude: Double? = null

    //View Models
    private val dataStoreVM by viewModels<DataStoreVM>()
    private val uploadVM by viewModels<UploadVM>()

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        supportActionBar?.title = "Upload Story"
        initView()

    }

    private fun checkForPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun initView() {
        binding.apply {
            btnOpenCamera.setOnClickListener { openCamera() }
            btnOpenGallery.setOnClickListener { openGallery() }
            btnUpload.setOnClickListener {
                uploadStory()
            }
            fabAddLocation.setOnClickListener {
                getMyLocationToShare()

            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocationToShare()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocationToShare()
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

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private val getMyLocLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val lat = it.data?.getDoubleExtra("latitude", 0.0)
            val lng = it.data?.getDoubleExtra("longitude", 0.0)
            _latitude = lat
            _longitude = lng
            Log.d("Location", "$_latitude, $_longitude")
            FancyToast.makeText(
                this@UploadStoryActivity,
                "Longitude: $_longitude\nLatitude: $_latitude",
                FancyToast.LENGTH_LONG,
                FancyToast.INFO,
                false
            ).show()
        }
    }
    private fun getMyLocationToShare() {
        if (!isLocationEnabled()) {
            showLocationNotEnabledDialog()
        } else {

            val intent = Intent(this@UploadStoryActivity, StoryMapsActivity::class.java)
            getMyLocLauncher.launch(intent)
        }
//        val locationRequest = LocationRequest.create().apply {
//            priority = Priority.PRIORITY_HIGH_ACCURACY
//            maxWaitTime = 100
//        }
        /*
        if (
            checkForPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            &&
            checkForPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            if (!isLocationEnabled()) {
                showLocationNotEnabledDialog()
            } else {
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
                ).addOnSuccessListener {
//                    _latitude = it?.latitude
//                    _longitude = it?.longitude
                    FancyToast.makeText(
                        this,
                        "Location found",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                }.addOnFailureListener {
                    FancyToast.makeText(
                        this,
                        "Location not found",
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,
                        false
                    ).show()
                }.addOnCompleteListener{
                    _latitude = it.result.latitude
                    _longitude = it.result.longitude
                    FancyToast.makeText(
                        this@UploadStoryActivity,
                        "Longitude: $_longitude\nLatitude: $_latitude",
                        FancyToast.LENGTH_LONG,
                        FancyToast.INFO,
                        false
                    ).show()

                }
//                fusedLocationClient.requestLocationUpdates(
//                    locationRequest,
//                    object : LocationCallback() {
//                        override fun onLocationResult(locationResult: LocationResult) {
//                            super.onLocationResult(locationResult)
//                            for (location in locationResult.locations) {
//                                Log.d("Location", "Location: $location")
//                                _latitude = location.latitude
//                                _longitude = location.longitude
//
//                            }
//                        }
//                    },
//                    Looper.getMainLooper()
//                )

            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }*/
    }


    private fun showLocationNotEnabledDialog() {
        AlertDialog.Builder(this@UploadStoryActivity).apply {
            setTitle("Enable Location")
            setMessage("Please Enable Location To Share Your Location")
            setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            setNegativeButton("Cancel") { _, _ ->
                FancyToast.makeText(
                    this@UploadStoryActivity,
                    "Location is not enabled",
                    FancyToast.LENGTH_LONG,
                    FancyToast.INFO,
                    false
                ).show()
            }
            create()
            show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                FancyToast.makeText(
                    this,
                    "Permission is not granted",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,
                    false
                ).show()
                finish()
            }
        }
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        UploadStoryUtilities.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadStoryActivity,
                "com.asthiseta.submissionintermediate",
                it
            )
            currentPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraIntentLauncher.launch(intent)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun openGallery() {
        val intent = Intent()
        intent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }

        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        galleryIntentLauncher.launch(chooser)

    }

    private val cameraIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.imageViewPreview.setImageBitmap(result)
        }
    }

    private val galleryIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = UploadStoryUtilities.uriToFile(selectedImg, this@UploadStoryActivity)

            getFile = myFile

            binding.imageViewPreview.setImageURI(selectedImg)
        }
    }


    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val descriptionText = binding.editTextAddDescription.text.toString()
            dataStoreVM.getLoginSession().observe(this) {

                uploadVM.apply {
                    if (_latitude != null && _longitude != null) {
                        uploadStoryWithLocation(
                            "Bearer ${it.token}",
                            file,
                            descriptionText,
                            _latitude!!,
                            _longitude!!
                        )
                    } else {
                        uploadStory("Bearer ${it.token}", file, descriptionText)
                    }
                    //uploadStory("Bearer ${it.token}", file, descriptionText)
                    message.observe(this@UploadStoryActivity) {
                        FancyToast.makeText(
                            this@UploadStoryActivity,
                            it,
                            FancyToast.LENGTH_SHORT,
                            FancyToast.INFO,
                            false
                        ).show()
                    }
                }
            }
            onBackPressed()
        } else {
            FancyToast.makeText(
                this@UploadStoryActivity,
                "Silahkan masukkan gambar terlebih dahulu.",
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,
                false
            ).show()
        }

    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}