package com.example.cargaschile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cargaschile.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySettingsBinding

    var isDriver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)


        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.fab.setOnClickListener { view ->
            val data = Intent()
            if(packData(data)) {
                setResult(RESULT_OK, data)
                finish()
            }
        }

        isDriver = intent.getBooleanExtra("isDriver", false)
        if(isDriver) {
            binding.contents.phone.isVisible = true
            binding.contents.load.isVisible = true
        }
        title = "Configuración"
    }

    fun packData(data: Intent) : Boolean {
        val pw = binding.contents.password.text
        val pwc = binding.contents.passwordConfirmation.text
        val eml = binding.contents.email.text
        val car = binding.contents.carencia.text
        val tel = binding.contents.phone.text
        val loa = binding.contents.load.text
        var msg = ""
        if(pw.isNotEmpty() && pw != pwc) msg = "Las cuevas claves no coinciden"
        if(eml.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(eml).matches()) msg = "Correo electronico inválido"

        if(pw.isNotEmpty()) data.putExtra("password", pw)
        if(eml.isNotEmpty()) data.putExtra("email", eml)

        if(car.isNotEmpty()) {
            val cr = car.toString().toInt()
            data.putExtra("carencia", cr)
            if(cr <= 0) msg = "La carencia debe ser positiva, en numero de dias"
        }
        if(isDriver) {
            if(tel.isNotEmpty()) {
                val tl = tel.toString().toInt()
                data.putExtra("phone", tl)
                if(tl < 100000000 || tl > 999999999) msg  = "Telefono invalido, deben ser 9 dígitos"
            }

            if(loa.isNotEmpty()) {
                val cq = loa.toString().toInt()
                data.putExtra("load", cq)
                if(cq <= 0) msg = "La capacidad de carga, en kg, debe ser positiva"
            }
        }
        if(msg.isNotEmpty()) {
            Prompt.inform(this@SettingsActivity, "ERROR", msg, "OK", ::cbi)
            return false
        }
        return true
    }

    fun cbi(i: Int) {
    }

}