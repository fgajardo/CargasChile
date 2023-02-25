package com.example.cargaschile

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cargaschile.databinding.ActivityStatusBinding

class StatusActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = intent.getStringExtra("title")
        binding.contents.text.text = intent.getStringExtra("contents")
        if (intent.getBooleanExtra("simple", false)) {
            binding.contents.button.isVisible = false
            binding.fab.isVisible = false

        } else {
            binding.contents.button.text = intent.getStringExtra("buttonText")
            binding.contents.button.setOnClickListener { detallesReadonly("Peras pa curico") }
            binding.fab.setOnClickListener { view ->
                Prompt.confirm(
                    this@StatusActivity,
                    "",
                    "Seguro que quiere postular a este envio?",
                    "SI",
                    "NO",
                    ::cbi
                )
            }
        }
    }

    fun cbsb(ret: String, result: Boolean) {
        // llamar a modelo, nueva postulacion por $ret, salir
        println("cbsb($ret,$result)")
        if(ret.isEmpty() || ret.toInt() <= 0)
            Prompt.inform(this@StatusActivity,"ERROR", "La oferta debe ser de al menos $1", "OK", ::cbi)
        else finish()
    }

    fun cbi(ret: Int) {
        // 0 -> si
        // 1 -> no
        if(0 == ret) {
            Prompt.popInput(
                this@StatusActivity,
                "",
                "Cuanto ofrece, en pesos?",
                "OK",
                10000,
                false,
                ::cbsb
            )
        }
        println("cbi($ret)")
    }

    fun detallesReadonly(arg: String) {
        val intent = Intent(this@StatusActivity, StatusActivity::class.java)
        intent.putExtra("title", "Show shipment")
        intent.putExtra("contents", "Descripcion del envio $arg\nLinea 1\nLinea 2\nLinea 3")
        intent.putExtra("buttonText", "Nada")
        intent.putExtra("simple", true)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}