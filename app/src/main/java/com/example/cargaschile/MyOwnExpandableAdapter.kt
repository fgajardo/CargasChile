package com.example.cargaschile

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MyOwnExpandableAdapter(private val context: AppCompatActivity, var data: Tabla) :
    BaseExpandableListAdapter() {
    override fun getChild(sec: Int, row: Int): Shipment? {
        return data.itemAt(sec, row)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup?
    ): View? {
        var convertView: View? = convertView
        val s: Shipment = getChild(groupPosition, childPosition) as Shipment
        val inflater = context.layoutInflater
        if (convertView == null) {
            /*if(s.hasApe()) convertView = inflater.inflate(shipment_pic, null)
            else*/ convertView = inflater.inflate(R.layout.shipment_plain, null)
            //convertView = inflater.inflate(cell_item, null)
        }
        val titleText: String = s.title
        val subtitleText: String = s.getSubtitle()
        var item = convertView?.findViewById(R.id.title) as TextView
        titleText.also { item.text = it }
        item = convertView?.findViewById(R.id.details) as TextView
        subtitleText.also { item.text = it }
        //if(s.hasApe()) {
          //  val iItem = convertView?.findViewById(id.ape) as ImageView
            /*if(null != iItem)
                iItem.setImageDrawable(R.drawable.g)// .setImageBitmap(s.getBitmap())
             */
        //}
        return convertView
    }

    override fun getChildrenCount(sec: Int): Int {
        if(null == data)
            return 0
        return data.numberOfItemsAt(sec)
    }

    override fun getGroup(sec: Int): String? {
        if(null == data)
            return null
        return data.nameOfSectionAt(sec)
    }

    override fun getGroupCount(): Int {
        if (null == data) return 0
        return data.numberOfSections()
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View? {
        var convertView: View? = convertView
        val sectionHeaderName = getGroup(groupPosition)
        if (convertView == null) {
            val infalInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).also {
                convertView = it.inflate(
                    R.layout.section_item,
                    null
                )
            }
        }
        val item = convertView?.findViewById(R.id.lblSectionHeader) as TextView
        item.setTypeface(null, Typeface.BOLD)
        item.text = sectionHeaderName
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        super.registerDataSetObserver(observer)
    }
}