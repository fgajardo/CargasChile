package com.example.cargaschile

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cargaschile.databinding.ActivityShipmentBinding
import java.util.*

class ShipmentActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityShipmentBinding

    private lateinit var shipmentName: String

    var origen = " "
    var destino = " "
    var esNuevo = true
    var existingID = ""
    var mYear = -1
    var mMonth = -1
    var mDay = -1
    var mHour = -1
    var mMinute = -1

    private lateinit var cs: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        cs = Bundle()
        cs!!.putInt("saved", -1)

        binding = ActivityShipmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        esNuevo = intent.getBooleanExtra("isNew", true)

        binding.shipmentLayout.buttonDay.setOnClickListener { showDate() }
        binding.shipmentLayout.buttonTime.setOnClickListener { showTime() }
        binding.shipmentLayout.buttonOrigin.setOnClickListener { showLocation(true,::cbi) }
        binding.shipmentLayout.buttonDestination.setOnClickListener { showLocation(false,::cbi) }

        binding.shipmentLayout.editPayload.addTextChangedListener(PrettyNumber(binding.shipmentLayout.editPayload))
        binding.shipmentLayout.editPayment.addTextChangedListener(PrettyNumber(binding.shipmentLayout.editPayment))

        binding.fab.setOnClickListener { view ->
            validate()
        }
        setup()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun validate() {
        var msg = ""
        val cal = Calendar.getInstance()
        if (binding.shipmentLayout.editTitle.text.toString().isEmpty()) msg += "Debe ingresar titulo\n"
        if (origen.isEmpty()) msg += "Debe ingresar comuna de origen\n"
        if (destino.isEmpty()) msg += "Debe ingresar comuna de destino\n"
        if (mYear < 0 || mMonth < 0 || mDay < 0) msg += "Debe ingresar fecha\n"
        if (mHour < 0 || mMinute < 0) msg += "Debe ingresar hora\n"
        if (binding.shipmentLayout.editPayload.text.toString().isEmpty()) msg += "Debe ingresar carga\n"
        if (binding.shipmentLayout.editPayment.text.toString().isEmpty()) msg += "Debe ingresar precio\n"
        cal[mYear, mMonth, mDay, mHour] = mMinute
        if (msg.length > 0) {
            Prompt.inform(this@ShipmentActivity, "", msg, "OK", ::cbi)
            return
        } else {
            //String yesNo = (m.currentUser.isClient? "0":"1");
            val fmt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val strDate = fmt.format(cal.time)
            val now = Date()
            val lNow = now.time
            val kf = Model.currentUser.keepFor as Long
            val desde = lNow - (kf * 24 * 3600 * 1000)
            val since = Date(desde)
            val dDate = fmt.format(since.time)
            val strIsNew = if (esNuevo) "1" else "0" // is not already present, insert
            val args = String.format("title_field=%s&owner_field=%s&cargo_field=%d&origin_field=%s&destination_field=%s&startingDate_field=%s&maxPayment_field=%d&comments_field=%s&isNew_field=%s&id_field=%s&start_field=%s&insShipment=insShipment",
                binding.shipmentLayout.editTitle.text.toString(),
                Model.getUser(),
                PrettyNumber.toInt(binding.shipmentLayout.editPayload.text.toString()),
                origen, destino, strDate,
                PrettyNumber.toInt(binding.shipmentLayout.editPayment.text.toString()),
                binding.shipmentLayout.editRemarks!!.text.toString(),
                strIsNew, existingID, dDate
            )
            //Log.d("SHPgs",args);
            val url: String = Model.getURL("shipment.php")
            Log.d("SHPgs", "$url -> $args")
            Model.loadDataTabla(this@ShipmentActivity, url, args, ::cbi)
        }
    }

    fun cbi(ret: Int) {
    }

    fun setup() {
        esNuevo = Model.newShipment
        if (esNuevo) {
            shipmentName = "Nuevo envío"
        } else  // fill fields
        {
            val s: Shipment = Model.currentShipment
            existingID = s.id.toString()
            binding.shipmentLayout.editTitle.setText(s.title)
            binding.shipmentLayout.editPayload.setText(PrettyNumber.toAmount(s.load))
            binding.shipmentLayout.editPayment.setText(PrettyNumber.toAmount(s.maxPayment))
            binding.shipmentLayout.editRemarks.setText(s.comments)
            origen = s.origin
            binding.shipmentLayout.buttonOrigin.text = origen
            destino = s.destination
            binding.shipmentLayout.buttonDestination.text = destino
            val cal = Calendar.getInstance()
            cal.time = s.startingDate
            mYear = cal[Calendar.YEAR]
            mMonth = cal[Calendar.MONTH]
            mDay = cal[Calendar.DAY_OF_MONTH]
            mHour = cal[Calendar.HOUR_OF_DAY]
            mMinute = cal[Calendar.MINUTE]
            binding.shipmentLayout.buttonTime.text = mDay.toString() + "-" + (mMonth + 1) + "-" + mYear
            if (mMinute < 10) binding.shipmentLayout.buttonTime.text =
                "$mHour:0$mMinute" else binding.shipmentLayout.buttonTime.text =
                "$mHour:$mMinute"
            binding.shipmentLayout.editTitle.setSelection(binding.shipmentLayout.editTitle.length())
            binding.shipmentLayout.editPayload.setSelection(binding.shipmentLayout.editPayload.length())
            binding.shipmentLayout.editPayment.setSelection(binding.shipmentLayout.editPayload.length())
            binding.shipmentLayout.editRemarks.setSelection(binding.shipmentLayout.editRemarks.length())
        }
    }

    fun wasLoaded(res: Int) {
        var msg = "OK"
        if (0 == res) // ok
        {
            onBackPressed()
            return
        } else if (1 == res) msg =
            "Error de conexion, intente mas rato" else if (2 == res) msg =
            "Respuesta invalida, tiene minutos?" else if (3 == res) msg = "Respuesta nula"
        Prompt.inform(this, "", msg, "OK", ::cbi)
    }

    private fun showDate() {
        cs.putInt("saved", 345)
        if (-1 == mYear && -1 == mMonth && -1 == mDay) {
            // Get Current Date
            val c = Calendar.getInstance()
            mYear = c[Calendar.YEAR]
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]
            //Log.d("click","dd/mm/yy now");
        }
        // else, already assigned.
        Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                binding.shipmentLayout.buttonDay.text =
                    dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                mYear = year
                mMonth = monthOfYear
                mDay = dayOfMonth
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    fun showTime() {
        cs.putInt("saved", 345)
        if (-1 == mHour && -1 == mMinute) {
            // Get Current Time
            val c = Calendar.getInstance()
            mHour = c[Calendar.HOUR_OF_DAY]
            mMinute = c[Calendar.MINUTE]
            //Log.d("click","hh:mm now");
        }
        // else, already assigned
        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                if (minute < 10) binding.shipmentLayout.buttonTime.text =
                    "$hourOfDay:0$minute" else binding.shipmentLayout.buttonTime.text =
                    "$hourOfDay:$minute"
                mHour = hourOfDay
                mMinute = minute
            }, mHour, mMinute, false
        )
        timePickerDialog.show()
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val c = result.data!!.getStringExtra("comuna")
            val p = result.data!!.getStringExtra("provincia")
            val r = result.data!!.getStringExtra("region")
            if(result.data!!.getBooleanExtra("isOrigin", false)) {
                origen = "$c, $p, $r"
                binding.shipmentLayout.buttonOrigin.text = origen
            }
            else {
                destino = "$c, $p, $r"
                binding.shipmentLayout.buttonDestination.text = destino
            }
        }
    }

    private fun showLocation(origin: Boolean, cb:(id: Int)->Unit) {
        val intent = Intent(this@ShipmentActivity, LocationActivity::class.java)
        intent.putExtra("isOrigin", origin)
        if(origin) {
            cs.putInt("saved", 678)
            intent.putExtra("title","Seleccione origen")
        }
        else {
            cs.putInt("saved", 999)
            intent.putExtra("title","Seleccione destino")
        }
        resultLauncher.launch(intent)
    }

    public override fun onResume() {
        super.onResume() // Always call the superclass method first
        title = shipmentName
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        var isNew = 0
        if (esNuevo) isNew = 1
        savedInstanceState.putInt("esNuevo", isNew)
        savedInstanceState.putString("titulo", binding.shipmentLayout.editTitle.text.toString())
        savedInstanceState.putString("origen", origen)
        savedInstanceState.putString("destino", destino)
        savedInstanceState.putInt("yr", mYear)
        savedInstanceState.putInt("mo", mMonth)
        savedInstanceState.putInt("dy", mDay)
        savedInstanceState.putInt("hh", mHour)
        savedInstanceState.putInt("mm", mMinute)
        savedInstanceState.putString("carga", binding.shipmentLayout.editPayload.text.toString())
        savedInstanceState.putString("monto", binding.shipmentLayout.editPayment.text.toString())
        savedInstanceState.putString("obs", binding.shipmentLayout.editRemarks.text.toString())
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val ii = savedInstanceState.getInt("esNuevo")
        esNuevo = 1 == ii
        var s = savedInstanceState.getString("titulo")
        binding.shipmentLayout.editTitle.setText(s)
        origen = savedInstanceState.getString("origen").toString()
        destino = savedInstanceState.getString("destino").toString()
        mYear = savedInstanceState.getInt("yr")
        mMonth = savedInstanceState.getInt("mo")
        mDay = savedInstanceState.getInt("dy")
        //DatePicker dp = btnDatePicker.getDatePicker();
        //dp.updateDate(mYear, mMonth, mDay);
        var bText = "Fecha"
        if (mDay > 0 && mMonth > -1 && mYear > 0) bText =
            mDay.toString() + "-" + (mMonth + 1) + "-" + mYear
        binding.shipmentLayout.buttonDay.text = bText
        mHour = savedInstanceState.getInt("hh")
        mMinute = savedInstanceState.getInt("mm")
        //TimePicker tp = btnTimePicker.getTime
        //btnTimePicker.setCurrentHour(mHour);
        //btnTimePicker.setCurrentMinute(mMinute);
        bText = "Hora"
        if (mHour > -1 && mMinute > -1) {
            bText = if (mMinute < 10) "$mHour:0$mMinute" else "$mHour:$mMinute"
        }
        binding.shipmentLayout.buttonTime.text = bText
        s = savedInstanceState.getString("carga")
        binding.shipmentLayout.editPayload.setText(s)
        s = savedInstanceState.getString("monto")
        binding.shipmentLayout.editPayment.setText(s)
        s = savedInstanceState.getString("obs")
        binding.shipmentLayout.editRemarks.setText(s)
        binding.shipmentLayout.editTitle.setSelection(binding.shipmentLayout.editTitle.length())
        binding.shipmentLayout.editPayload.setSelection(binding.shipmentLayout.editPayload.length())
        binding.shipmentLayout.editPayment.setSelection(binding.shipmentLayout.editPayment.length())
        binding.shipmentLayout.editRemarks.setSelection(binding.shipmentLayout.editRemarks.length())
    }

}