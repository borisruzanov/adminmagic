package com.boris.expert.adminmagic.view.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import com.boris.expert.adminmagic.R
import com.boris.expert.adminmagic.interfaces.UploadFileCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        var alert: AlertDialog? = null

        // THIS FUNCTION WILL CHECK THE INTERNET CONNECTION AVAILABLE OR NOT
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(
                    connectivityManager.activeNetwork
                )
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }

        // THIS FUNCTION WILL RETURN THE DATE TIME STRING FROM TIMESTAMP
        fun getDateTimeFromTimeStamp(timeStamp: Long): String {
            val c = Date(timeStamp)
            val df = SimpleDateFormat("yyyy-MM-dd kk:mm:ss a", Locale.getDefault())
            return df.format(c).toUpperCase(Locale.ENGLISH)
        }



        fun hideKeyboard(context: Context, activity: MainActivity) {
            val view: View? = activity.currentFocus
            if (view != null) {
                val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }



        // THIS FUNCTION WILL ALERT THE DIFFERENT MESSAGES
        fun showAlert(context: Context, message: String) {
            MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()
                }
                .create().show()
        }

        fun startLoading(context: Context) {
            if (alert == null) {
                val builder = MaterialAlertDialogBuilder(context)
                val layout = LayoutInflater.from(context).inflate(R.layout.custom_loading, null)
                builder.setView(layout)
                builder.setCancelable(false)
                alert = builder.create()
                alert!!.show()
            }
        }

        fun dismiss() {
            if (alert != null) {
                alert!!.dismiss()
                alert = null
            }
        }

        fun addDaysCalenderDate(days: Int): Calendar {
            val sdf = SimpleDateFormat("yyyy-MM-dd kk:mm a", Locale.ENGLISH)
            val c = Calendar.getInstance()
            c.time = sdf.parse(getDateTimeFromTimeStamp(System.currentTimeMillis()))!!
            c.add(Calendar.DATE, days)
            return c
        }

        fun getDateFromTimeStamp(timeStamp: Long): String {
            val c: Date = Date(timeStamp)
            val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return df.format(c).toUpperCase(Locale.ENGLISH)
        }

        fun getDateFormateFromTimeStamp(timeStamp: Long): String {
            val c: Date = Date(timeStamp)
            val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            return df.format(c).toUpperCase(Locale.ENGLISH)
        }

        fun getFormattedDate(context: Context?, smsTimeInMilis: Long): String {
            val smsTime = Calendar.getInstance()
            smsTime.timeInMillis = smsTimeInMilis
            val now = Calendar.getInstance()
            val timeFormatString = "h:mm:ss"
            val dateTimeFormatString = "EEEE, MMMM d, h:mm:ss"

            return if (now[Calendar.DATE] == smsTime[Calendar.DATE]) {
                "Today " + DateFormat.format(timeFormatString, smsTime)
            } else if (now[Calendar.DATE] - smsTime[Calendar.DATE] == 1) {
                "Yesterday " + DateFormat.format(timeFormatString, smsTime)
            } else if (now[Calendar.YEAR] == smsTime[Calendar.YEAR]) {
                DateFormat.format(dateTimeFormatString, smsTime).toString()
            } else {
                DateFormat.format("MMMM dd yyyy, h:mm:ss", smsTime).toString()
            }
        }


        fun showSoftKeyboard(context: Context, view: View) {
            if (view.requestFocus()) {
                val imm: InputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun hideSoftKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun uploadImageOnFirebaseStorage(referenceName:String,imagePath:String,listener: UploadFileCallback){
            val storageReference = FirebaseStorage.getInstance().reference
            if (FirebaseAuth.getInstance().currentUser != null) {

                val file = Uri.fromFile(File(imagePath))

                val fileRef = storageReference.child("$referenceName/${System.currentTimeMillis()}.jpg")
                val uploadTask = fileRef.putFile(file)

                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    fileRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        listener.onSuccess(downloadUri.toString())
                    }
                }
            }
        }

    }

}