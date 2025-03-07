package com.example.cryptocurrency

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptocurrency.databinding.DialogProgressbarBinding
import com.example.cryptocurrency.utilities.Utility

interface OnOkClickListener {
    fun onOkClick()
}

abstract class BaseAppCompatActivity : AppCompatActivity() {
    private lateinit var dialogBinding: DialogProgressbarBinding
    private lateinit var pdProgress: Dialog
    private lateinit var dialog_alert: Dialog

    override fun onDestroy() {
        super.onDestroy()

        if (::dialog_alert.isInitialized && dialog_alert.isShowing) {
            dialog_alert.dismiss()
        }
    }

    open fun showProgress(context: Activity?) {
        try {
            if (!::pdProgress.isInitialized) {
                pdProgress = createProgressDialog(context)
            }
            pdProgress.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun showProgress(context: Activity?, msg: String?) {
        try {
            if (!::pdProgress.isInitialized) {
                pdProgress = createProgressDialog(context)
            }
            pdProgress.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun hideProgress() {
        try {
            if (::pdProgress.isInitialized && pdProgress.isShowing) {
                pdProgress.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun getRootView(): View {
        var rootView = findViewById<View>(R.id.ll_root)
        if (rootView == null) {
            rootView = window.decorView.rootView
        }
        return rootView
    }

    open fun createProgressDialog(mContext: Context?): Dialog {
        val dialog = Dialog(mContext!!, R.style.ActivityTheme)
        dialogBinding = DialogProgressbarBinding.inflate(LayoutInflater.from(mContext))
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)
        return dialog
    }

    open fun isNetworkAvailable(context: Context, showErrorDialog: Boolean): Boolean {
        var networkStatus = false
        if (Utility.isNetworkAvailable(context)) {
            networkStatus = true
        } else {
            // show error
            if (showErrorDialog) {
                dialogAlert(
                    context,
                    context.getString(R.string.app_name),
                    context.getString(R.string.msg_no_internet),
                    context.getString(R.string.ok),
                    null
                )
            }
        }
        return networkStatus
    }

    open fun showError(context: Activity, message: String) {
        try {
            Utility.showSnackBar(getRootView(), message)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    open fun showExceptionError(context: Activity) {
        try {
            Utility.showSnackBar(getRootView(), getString(R.string.error_standard_error_0))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    open fun dialogAlert(
        mContext: Context,
        title: String,
        message: String,
        okString: String,
        listener: OnOkClickListener?
    ) {
        try {
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle(title)
            builder.setMessage(message)

            builder.setPositiveButton(R.string.ok) { _, _ ->
                listener?.onOkClick()
            }
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}