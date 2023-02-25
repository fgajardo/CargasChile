package com.example.cargaschile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ExpandableListView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.cargaschile.databinding.ActivityTableVcactivityBinding

class TableVCActivity : AppCompatActivity() {

    var context: AppCompatActivity? = null
    var expListView: ExpandableListView? = null
    var data: Tabla? = null
    var moea: MyOwnExpandableAdapter? = null
    var isDriverTable = false
    private var currSection = 0
    private var currRow = 0
    private val vcFlag = "NOT INITIALISED YET"
    private var caller = -1

    private val LIST_STATE_KEY = "listState"
    private val LIST_POSITION_KEY = "listPosition"
    private val ITEM_POSITION_KEY = "itemPosition"

    private var mListState: Parcelable? = null
    private var mListPosition = 0
    private var mItemPosition = 0

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: com.example.cargaschile.databinding.ActivityTableVcactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        isDriverTable = Model.getUser().isDriver
        isDriverTable = true
        title = Model.getUser().username
        binding = com.example.cargaschile.databinding.ActivityTableVcactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add))
        if(isDriverTable) // Add defined in layout
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search))

        binding.fab.setOnClickListener { view ->
            if(isDriverTable) println("A SEARCH")
            else println("A ADD")
        }
    }

    override fun onResume() {
        super.onResume() // Always call the superclass method first
        //Log.d("DBG","OnResume TVCA");
        expListView = findViewById<View>(R.id.tableView) as ExpandableListView
        context = this@TableVCActivity
        data = Model.getDataTable()
        //Log.d("TVCoR","dumping...");
        //Log.d("DMP",data.dump());
        //Log.d("TVCoR","dumped.");
        moea = data?.let { MyOwnExpandableAdapter(context = context as TableVCActivity, data = it) }
        expListView!!.setAdapter(moea)
        registerForContextMenu(expListView)
        expListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            Model.currentShipment = moea?.getChild(groupPosition, childPosition) as Shipment
            currSection = groupPosition
            currRow = childPosition
            var args: String? = ""
            var url: String = Model.getURL("shipment.php")
            if (0 == Model.currentShipment.status) {
                if (isDriverTable) // bid for current shipment
                {
                    args = String.format(
                        "sID=%d&bidder=%s&loadChildClick=loadChildClick",
                        Model.currentShipment.id,
                        Model.currentUser.username
                    )
                    caller = 2
                } // isDriverTable
                else  // bidders for current shipment
                {
                    args = String.format(
                        "sID=%d&bidsForShipment=bidsForShipment",
                        Model.currentShipment.id
                    )
                    caller = 3
                } // click to bidders
            } // 0 == status
            else  // load StatusActivity
            {
                val isDriver = if (isDriverTable) "1" else "0"
                args = String.format(
                    "sid=%d&isDriver=%s&loadForStatus=loadForStatus",
                    Model.currentShipment.id,
                    isDriver
                )
                url = Model.getURL("status.php")
                caller = 4
            }
            val ms = String.format("caller=%d, args=%s, url=%s", caller, args, url)
            //Log.d("DBG",ms);
            Model.loadData(this@TableVCActivity, ::rawWasLoaded, url, args)
            true
        } // onChildClick
        // setOnChildClickListener
        expListView!!.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ): Boolean {
                val packedPosition = expListView!!.getExpandableListPosition(position)
                val itemType = ExpandableListView.getPackedPositionType(packedPosition)
                currSection = ExpandableListView.getPackedPositionGroup(packedPosition)
                currRow = ExpandableListView.getPackedPositionChild(packedPosition)

                //  GROUP-item clicked, do nothing but consume event nonetheless
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    return true
                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    if (ShipmentType.Pendiente != data?.typeOfSectionAt(currSection) ) {
                        //Log.d("onItemLongClick", "no corresponde y chao");
                        return true // nothing else
                    }
                    //Log.d("onItemLongClick","menu con edit/delete");
                    val msg = "Esta seguro que quiere borrar el envio $currRow de la seccion $currSection?"
                    Prompt.confirm(this@TableVCActivity, "Confirmación", msg, "Si", "No", ::cb)
                }
                return false // default processing
            }
        }) // setOnItemLongClickListener
        //Log.d("DBG","Leaving onResume TVCA");
    }

    fun cb(ret: Int) { // 0 == ret => se borra
        if(true == data?.removeItemAt(currSection, currRow))
            moea?.notifyDataSetChanged()
        println("Borrado de la tabla, de la DB tambien")
    }

    fun cbi(ret: Int) {

    }
    fun rawWasLoaded(res: ArrayList<HashMap<String, String>>, result: Int) {
        println("TableVC, RWL result is $result")
        var msg = ""
        if (0 == result) // ok
        {
            //val map = res[0]
            // drv + pending
            val intent = Intent(this@TableVCActivity, StatusActivity::class.java)
            intent.putExtra("title", "Show shipment")
            intent.putExtra("contents", "Descripcion del envio seleccionado\nLinea 1\nLinea 2\nLinea 3")
            intent.putExtra("buttonText", "Nada")
            //intent.putExtra("simple", true)
            startActivity(intent)
        }
        else if (1 == result) msg = "Error de conexion, intente mas rato"
        else if (2 == result) msg = "Respuesta invalida, tiene minutos?"
        else if (3 == result) msg = "Respuesta nula"
        if(msg.isNotEmpty()) Prompt.inform(this@TableVCActivity, "", msg, "OK", ::cbi)

    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)

        // Save list state
        //ExpandableListView listView = getExpandableListView();
        mListState = expListView!!.onSaveInstanceState()
        state.putParcelable(LIST_STATE_KEY, mListState)

        // Save position of first visible item
        mListPosition = expListView!!.firstVisiblePosition
        state.putInt(LIST_POSITION_KEY, mListPosition)

        // Save scroll position of item
        val itemView: View? = expListView!!.getChildAt(0)
        mItemPosition = if (itemView == null) 0 else itemView.getTop()
        state.putInt(ITEM_POSITION_KEY, mItemPosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val i = Intent(this, SettingsActivity::class.java)
            i.putExtra("isDriver", isDriverTable)
            startActivity(i)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun updateSettings(data: Intent?) {

    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            updateSettings(data)
        }
    }


}