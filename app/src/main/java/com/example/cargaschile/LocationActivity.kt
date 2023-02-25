package com.example.cargaschile

import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cargaschile.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var isOrigin = true
    var comuna = ""
    var provincia = ""
    var region = ""

    fun cb(id: Int) {
        comuna = Shared.comuna(id)
        provincia = Shared.provincia(id)
        region = Shared.region(id)
        binding.locationLayout.selCol.text = "Seleccionada ${Shared.comuna(id)}, ${Shared.provincia(id)}, ${Shared.region(id)}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Shared.cb = ::cb
        binding.locationLayout.rvRegiones.adapter = RegionAdapter(GeoData.regionesZCS)

        binding.locationLayout.fromCurrentPos.setOnClickListener { onSel() }
        isOrigin = intent.getBooleanExtra("isOrigin", false)
        title = intent.getStringExtra("title")
        println("title asgn 2 '$title'")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.fab.setOnClickListener { view ->
            if(comuna.isEmpty()||provincia.isEmpty()||region.isEmpty())
                Prompt.inform(this@LocationActivity,"ERROR","Debe seleccionar una comuna, presione <- para cancelar", "OK", ::cbi)
            else {
                intent.putExtra("isOrigin", isOrigin)
                intent.putExtra("comuna", comuna)
                intent.putExtra("provincia", provincia)
                intent.putExtra("region", region)
                setResult(RESULT_OK, intent)
            }
        }
    }

    private fun cbi(res: Int){}

    fun locateStuff() {
        println("locateStuff")
        if (ContextCompat.checkSelfPermission(this@LocationActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@LocationActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@LocationActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@LocationActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        } else println("Already GRANTED")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        println("oRPR, rc=$requestCode")
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@LocationActivity,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        println("Permission Granted")
                        getLastKnownLocation()
                    }
                } else {
                    println("Permission Denied")
                }
                return
            }
        }
    }

    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    location.altitude = 254.3
                    location.latitude = -35.8206
                    location.longitude = -71.4580
                    // https://www.latlong.net/c/?lat=0.000000&long=0.000000
                    // use your location object
                    // get latitude , longitude and other info from this
                    println("h=${location.altitude}, la=${location.latitude}, lo=${location.longitude}")
                }
            }
    }

    fun onSel() {
        if(binding.locationLayout.fromCurrentPos.isChecked) locateStuff()
    }


}