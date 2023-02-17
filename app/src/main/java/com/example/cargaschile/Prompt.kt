package com.example.cargaschile

import android.content.Context
import android.content.DialogInterface
import android.os.CountDownTimer
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import java.util.*
import java.util.concurrent.TimeUnit

object Prompt {
    fun inform(ctx: Context, title: String, message: String, btnText: String, cb: (ret: Int) -> Unit) {
        stuff(ctx, title, message, true, btnText, "OK", cb)
    }

    fun confirm(ctx: Context, title: String, message: String, yesText: String, noText: String, cb: (ret: Int) -> Unit) {
        stuff(ctx, title, message, false, yesText, noText, cb)
    }

    fun popInput(ctx: Context, title: String, message: String, yesText: String, timeOut: Long, isRegister: Boolean, cb: (ret: String, isReg: Boolean) -> Unit) {
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(title)
        builder.setMessage(message)
        val input = EditText(ctx)
        // +  or InputType.TYPE_TEXT_VARIATION_PASSWORD
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton(yesText){dialogInterface, which ->
            cb(input.text.toString(), isRegister)
        }
        builder.setNegativeButton("Cancelar"){dialogInterface, which ->
            cb("", isRegister)
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)

        alertDialog.setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dialog: DialogInterface) {
                val defaultButton: Button =
                    (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
                val negativeButtonText: CharSequence = defaultButton.getText()
                object : CountDownTimer(timeOut, 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        defaultButton.setText(
                            java.lang.String.format(
                                Locale.getDefault(), "%s (%d)",
                                negativeButtonText,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                            )
                        )
                    }

                    override fun onFinish() {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                }.start()
            }
        })
        alertDialog.show()
    }

    fun stuff(ctx: Context, title: String, message: String, isOne: Boolean, yesText: String, noText:String, cb: (ret: Int) -> Unit) {
        val builder = AlertDialog.Builder(ctx)
        //set title for alert dialog
        builder.setTitle(title)
        //set message for alert dialog
        builder.setMessage(message)
        //builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(yesText){dialogInterface, which ->
            cb(0)//println("Fue uno")//Toast.makeText(applicationContext,"clicked yes", Toast.LENGTH_LONG).show()
        }
        if(!isOne) {
            builder.setNegativeButton(noText){dialogInterface, which ->
                cb(1)//println("Fue dos")//Toast.makeText(applicationContext,"clicked No", Toast.LENGTH_LONG).show()
            }
        }
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}