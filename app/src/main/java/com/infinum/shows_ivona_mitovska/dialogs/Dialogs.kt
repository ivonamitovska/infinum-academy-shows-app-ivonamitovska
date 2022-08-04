package com.infinum.shows_ivona_mitovska.dialogs

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.infinum.shows_ivona_mitovska.R

object Dialogs {

    fun showLogOutDialog(context: Context, listener: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle(context.getString(R.string.confirm))
            setMessage(R.string.do_you_want_to_log_out)
            setPositiveButton(R.string.yes, listener)
            setNegativeButton(R.string.no) { dialog, id ->
                dialog.cancel()
            }
            create().show()
        }
    }

    fun showQuitAppDialog(context: Context, listener: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle(context.getString(R.string.confirm))
            setMessage(context.getString(R.string.quit_app))
            setPositiveButton(R.string.yes, listener)
            setNegativeButton(R.string.no) { dialog, id ->
                dialog.cancel()
            }
            create().show()
        }
    }

}