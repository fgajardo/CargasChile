package com.example.cargaschile

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cargaschile.databinding.ActivityLoginBinding
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    private var currOp = -1 // 0 == login, 1 == logFromReg, 2 == remind, 3 == adminLogin

    private lateinit var binding: ActivityLoginBinding
    var currCode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        binding.editLogin.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                updateButtons()
            }
        })
        binding.editPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                updateButtons()
            }
        })
        binding.buttonLogin.setOnClickListener { this.onLogin(view) }
        binding.buttonRegister.setOnClickListener { this.onRegister(view) }
        binding.buttonRemind.setOnClickListener { this.onRemind(view) }
        updateButtons()
        title = "CargasChile"
    }

    // onRegister always on
    // onForget at least username
    // onLogin usr+pwd>=4
    fun updateButtons() {
        val vl = binding.editLogin.text.isNotEmpty()
        val pwd = binding.editPassword.text.toString()
        val vp = pwd.isNotEmpty() && pwd.length > 3
        binding.buttonLogin.isEnabled = vl && vp
        binding.buttonRemind.isEnabled = vl
    }

    private fun cb(ret: Int) {
        println("arg is $ret")
    }

    private fun cb2(ret: String, isReg: Boolean) { // codigo correcto, cambiar password, pasar a VC si isReg
        if(isReg) println("ret = $ret, pasar a VC")
        else println("ret = $ret, pasar a cambiar password")
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            doOps(data)
        }
    }

    private fun doOps(data: Intent?) {
        println("Desde register, llego:")
        if(null != data) {
            val login = data.getStringExtra("login").toString()
            val password = data.getStringExtra("password").toString()
            val email = data.getStringExtra("email").toString()
            val isDriver = data.getBooleanExtra("isDriver", true)
            val carencia = data.getIntExtra("carencia", 30)
            val loadCap = data.getIntExtra("loadCap", 1000)
            val telNum = data.getIntExtra("telNum", 999999999)
            // set new user & load TableVCActivity
            if (login != null) {
                Model.setUser(login, isDriver, carencia)
                val i = Intent(this, TableVCActivity::class.java)
                i.putExtra("login", login)
                i.putExtra("isDriver", isDriver)
                startActivity(i)
            }
        }
    }

    private fun dataLoaded(result: Int) {
        var msg = ""
        when (result) // ok
        {
            0 -> {
                val i = Intent(this, TableVCActivity::class.java)
                i.putExtra("login", Model.currentUser.username)
                i.putExtra("isDriver", Model.currentUser.isDriver)
                startActivity(i)
            }
            1 -> msg = "Error de conexion, intente mas rato"
            2 -> msg = "Respuesta invalida, tiene minutos?"
            else -> msg = String.format("Algo raro aqui, res = %d", result)
        }
        if (msg.isNotEmpty()) {
            Prompt.inform(this@LoginActivity, "", msg, "OK", ::cb)
        }
    }
    private fun wasLoaded(res: ArrayList<HashMap<String, String>>, result: Int) {
    }

    fun cbi(i: Int) {
    }
    fun cbs(s: String) {
        println("cbs($s)")
    }

    fun cbsb(s: String, b: Boolean) {
        if(currCode == s) { // estamos listos, cerrar y entrar a la principal
            //Model.setUser(login, isDriver, carencia)
            val i = Intent(this, TableVCActivity::class.java)
            println("peek")
            i.putExtra("login", Model.currentUser.username)
            println("done")
            i.putExtra("isDriver", Model.currentUser.isDriver)
            startActivity(i)
        } else // cueck
            Prompt.inform(this@LoginActivity, "ERROR", "Codigo incorrecto", "OK", ::cbi)
    }

    private fun rawWasLoaded(res: ArrayList<HashMap<String, String>>, result: Int) {
        println("RWL result is $result")
        var msg = ""
        when (result) // ok
        {
            0 -> {
                if (0 == currOp) // login
                {
                    val map = res[0]
                    val nItems = map.size
                    if (1 == nItems) // login error
                    {
                        val rtn = map["code"]
                        //Log.d("op_login","code = "+rtn);
                        Prompt.inform(this@LoginActivity, "", rtn!!,"OK", ::cb)
                    } else if (nItems > 1) {
                        doLogin(map)
                    }
                    return
                } else if (1 == currOp) // login from register
                {
                    if (null == res) {
                        return
                    }
                    val map = res[0]
                    val nItems = map.size
                    if (nItems > 1) // user data
                    {
                        doLogin(map)
                        return
                    }
                    // else check for error
                    msg = map["failed"].toString()
                    if (null != msg && msg.length > 1) {
                        Prompt.inform(this@LoginActivity, "", msg,"OK", ::cb)
                        return
                    }
                } else if (2 == currOp) // remind
                {
                    // error | email+codigo
                    val map = res[0]
                    if(map.containsKey("code")) {
                        currCode = map["code"].toString()
                        val email = map["email"].toString()
                        Prompt.popInput(this@LoginActivity,"", "Se le ha enviado un codigo de 4 digitos a su email $email, por favor reviselo (incluyendo SPAM) e ingreselo aca", "OK",
                            10000, false, ::cbsb)
                    } else {
                        val str = map["error"].toString()
                        Prompt.inform(this@LoginActivity, "ERROR", str, "OK", ::cbi)
                    }
                } else if (3 == currOp) // to register
                {
                    val intent = Intent(this, RegisterActivity::class.java)
                    resultLauncher.launch(intent) // cargar comunas?
                } else if (4 == currOp) // confirm register
                {
                    Prompt.popInput(
                        this@LoginActivity,
                        "Confirmación",
                        "Ingrese el código de 4 digitos enviado a su correo electronico",
                        "OK",
                        3000,
                        true,
                        ::cb2
                    )
                }
                currOp = -1 // clear
                return
            }
            1 -> msg = "Error de conexion, intente mas rato"
            2 -> msg = "Respuesta invalida, tiene minutos?"
            3 -> msg = "Respuesta nula"
        }
        if(msg.isNotEmpty()) Prompt.inform(this@LoginActivity, "", msg,"OK", ::cb)
    }

    private fun onLogin(view: View?) {
        val args = String.format(
            "username_field=%s&password_field=%s&op_login=op_login",
            binding.editLogin.text.toString(),
            binding.editPassword.text.toString()
        )
        Model.setUser(binding.editLogin.text.toString(), true, 32) // TMP
        //Log.d("post op_login",args);
        val url: String = Model.getURL("login.php")
        //Log.d("site",url);
        currOp = 0
        Model.loadData(this@LoginActivity, ::rawWasLoaded, url, args)
    }

    private fun onRegister(view: View?) {
        val args = String.format("op_comunas=op_comunas")
        val url: String = Model.getURL("login.php")
        currOp = 3
        Model.loadData(this@LoginActivity, ::rawWasLoaded, url, args)
    }

    private fun onRemind(view: View?) {
        val name = binding.editLogin.text.toString()
        if (null == name || name.isEmpty()) {
            val msg = "Ingrese su nombre de usuario para recuperar su clave"
            Prompt.inform(this,"",msg, "OK", ::cb)
        } else {
            val args = String.format("username_field=%s&op_forgot=op_forgot", name)
            val url: String = Model.getURL("login.php")
            //Log.d("post op_forgot",args);
            currOp = 2
            Model.loadData(this@LoginActivity, ::rawWasLoaded, url, args)
        }
    }

    private fun doLogin(map: Map<String, String>) {
        Model.setUser(map)
        val site = map["site"]
        Model.setSite(site!!)
        doUserLogin()
    }

    private fun doUserLogin() {
        Model.dump()
        val now = Date()
        val lNow = now.time
        //if (null == m.currentUser) Log.d("ls()", "currentUser es null");
        val kf = Model.currentUser.keepFor
        val desde = lNow - (kf * 24 * 3600 * 1000)
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dDate = fmt.format(desde)
        val yesNo = if (Model.currentUser.isDriver) "1" else "0"
        val args = java.lang.String.format(
            "username=%s&isDriver=%s&deliveryDate=%s&loadShipmentsFirst=loadShipmentsFirst",
            Model.currentUser.username,
            yesNo,
            dDate
        )
        //Log.d("LA/args",args);
        val url = Model.getURL("shipment.php")
        Model.loadFirstDataTabla(this@LoginActivity, ::dataLoaded, url, args)
    }

}
// style was     <style name="Theme.CargasChile" parent="Theme.MaterialComponents.DayNight.DarkActionBar">