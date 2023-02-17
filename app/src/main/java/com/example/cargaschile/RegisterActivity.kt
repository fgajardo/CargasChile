package com.example.cargaschile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cargaschile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegisterBinding
    private var currCode = "0"
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
        binding.loadCap.isVisible = false
        binding.telNum.isVisible = false

        binding.cbDriver.setOnClickListener { this.onCheckDriver(binding.root) }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    fun cb(ret: String, isReg: Boolean) {
    }

    fun cbi(i: Int) {
    }
    fun cbs(s: String) {
        println("cbs($s)")
    }

    fun cbsb(s: String, b: Boolean) {
        if(currCode == s) { // estamos listos, cerrar y entrar a la principal
            val data = Intent()
            packData(data)
            setResult(RESULT_OK, data)
            finish()
        } else // cueck
            Prompt.inform(this@RegisterActivity, "ERROR", "Codigo incorrecto", "OK", ::cbi)
    }

    fun cbm(res: ArrayList<HashMap<String,String>>, result: Int)
    {
        var msg = ""
        if (0 == result) // ok
        {
            val map = res[0]
            if(map.containsKey("code")) {
                currCode = map["code"].toString()
                Prompt.popInput(this@RegisterActivity,"", "Se le ha enviado un codigo de 4 digitos a su email, por favor reviselo (incluyendo SPAM) e ingreselo aca", "OK",
                        10000, false, ::cbsb)
            } else {
                val str = map["present"].toString()
                Prompt.inform(this@RegisterActivity, "ERROR", str, "OK", ::cbi)
            }
        }
        else if (1 == result) msg = "Error de conexion, intente mas rato"
        else if (2 == result) msg = "Respuesta invalida, tiene minutos?"
        else if (3 == result) msg = "Respuesta nula"
        if(msg.isNotEmpty()) Prompt.inform(this@RegisterActivity, "", msg, "OK", ::cbi)
    }

    fun onCheckDriver(view: View) {
        var isDriver = false
        if(binding.cbDriver.isChecked) isDriver = true
        binding.loadCap.isVisible = isDriver
        binding.loadCap.isEnabled = isDriver
        binding.telNum.isVisible = isDriver
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
        if(rtn.isNotEmpty()) {
            Prompt.inform(this@RegisterActivity, "ERROR", rtn, "OK", ::cbi)
            return
        }
        // else, check if login, email or phone are not already present
        Model.setCallback(::cbm)
        Model.checkPresent(this@RegisterActivity, rtn, login, email, driver, telNum, ::cbs)
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