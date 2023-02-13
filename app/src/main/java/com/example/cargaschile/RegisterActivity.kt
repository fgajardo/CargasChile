package com.example.cargaschile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cargaschile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Registro"
        binding.fab.setOnClickListener { view ->
            checkValues()
        }
        binding.cbDriver.setOnClickListener { this.onCheckDriver(binding.root) }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    fun cb(ret: String, isReg: Boolean) {
    }

    fun onCheckDriver(view: View) {
        var isDriver = false
        if(binding.cbDriver.isChecked) isDriver = true
        binding.loadCap.isEnabled = isDriver
        binding.telNum.isEnabled = isDriver
    }

    private fun checkValues() {
        val login = binding.loginName.text.toString()
        val password = binding.password.text.toString()
        val email = binding.email.text.toString()
        val driver = binding.cbDriver.isChecked
        var carencia = 0
        var str = binding.carencia.text.toString()
        if(str.isNotEmpty()) carencia = binding.carencia.text.toString().toInt()
        var loadCap = 0
        str = binding.loadCap.text.toString()
        if(str.isNotEmpty()) loadCap = binding.loadCap.text.toString().toInt()
        var telNum = 0
        str = binding.telNum.text.toString()
        if(str.isNotEmpty()) telNum = binding.telNum.text.toString().toInt()
        var rtn = ""
        if (driver) {
            if (null == telNum || telNum < 100000000) rtn = "Telefono invalido, deben ser 9 dígitos"
            if (0 == loadCap) rtn = "Capacidad de carga no puede ser cero"
        }
        if (carencia <= 0) rtn = "Carencia debe ser mayor que cero dias"
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) rtn = "Correo electronico inválido"
        if (null == email || email.isEmpty()) rtn = "Correo electrónico nulo"
        if (null == password || password.isEmpty()) rtn = "Clave nula"
        else if (password.length < 4) rtn = "Clave muy corta, minimo 4"
        if (null == login || login.isEmpty()) rtn = "Nombre de usuario nulo"
        // check if login, email or phone are not already present
        Model.checkPresent(this@RegisterActivity, rtn, login, email, driver, telNum, ::presentCB)
    }

    private fun presentCB(rtn: String?) {
        if (rtn != null) {
            if (rtn.isNotEmpty()) {
                Prompt.popInput(
                    this@RegisterActivity,
                    "Error",
                    rtn,
                    "OK",
                    "code",
                    3000,
                    true,
                    ::cb
                )
                //return@setOnClickListener
            }
        }
        val data = Intent()
        packData(data)
        setResult(RESULT_OK, data)
        finish()
    }

    fun packData(data: Intent) {
        data.putExtra("login", binding.loginName.text.toString())
        data.putExtra("password", binding.password.text.toString())
        data.putExtra("email", binding.email.text.toString())
        data.putExtra("isDriver", binding.cbDriver.isChecked)
        var carencia = 0
        var str = binding.carencia.text.toString()
        if(str.isNotEmpty()) carencia = binding.carencia.text.toString().toInt()
        var loadCap = 0
        str = binding.loadCap.text.toString()
        if(str.isNotEmpty()) loadCap = binding.loadCap.text.toString().toInt()
        var telNum = 0
        str = binding.telNum.text.toString()
        if(str.isNotEmpty()) telNum = binding.telNum.text.toString().toInt()
        data.putExtra("carencia", carencia)
        data.putExtra("loadCap", loadCap)
        data.putExtra("telNum", telNum)
    }
}