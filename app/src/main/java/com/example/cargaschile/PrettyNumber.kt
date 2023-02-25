package com.example.cargaschile

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class PrettyNumber(var editText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        try {
            editText.removeTextChangedListener(this)
            val value = editText.text.toString()
            if (value != null && value != "") {
                if (value.startsWith("0")) editText.setText("")
                val dfs = DecimalFormatSymbols()
                val separator = dfs.groupingSeparator.toString()
                val str = editText.text.toString().replace(separator, "")
                if (value != "") {
                    val str2: String =
                        NumberFormat.getNumberInstance(Locale.getDefault()).format(Otros.s2i(str))
                    editText.setText(str2)
                }
                editText.setSelection(editText.text.toString().length)
            }
            editText.addTextChangedListener(this)
            return
        } catch (ex: Exception) {
            ex.printStackTrace()
            editText.addTextChangedListener(this)
        }
    }

    companion object {
        fun toInt(str: String?): Int {
            if((null == str)||(str.isEmpty())) return 0
            val dfs = DecimalFormatSymbols()
            val separator = dfs.groupingSeparator.toString()
            return Otros.s2i(str.replace(separator, ""))
        }

        fun toAmount(qtty: Int): String {
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(qtty.toLong())
        }
    }
}