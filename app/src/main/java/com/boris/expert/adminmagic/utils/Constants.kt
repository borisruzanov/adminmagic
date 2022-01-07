package com.boris.expert.adminmagic.utils

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class Constants {

    // HERE WE WILL CREATE ALL THE CONSTANT DATA
    companion object {
        const val ticketsReference = "SupportTickets"
        const val supportChat = "SupportChat"
        const val READ_STORAGE_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 101
        const val READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE"
        const val CAMERA_PERMISSION = "android.permission.CAMERA"
        const val BASE_URL = "https://pages.qrmagicapp.com/"
        var isLogin: String = "is_login"
        var user: String = "user"
        var email: String = "email"
        var firebaseUserId = "agent123"
        var adminEmail = "admin@gmail.com"
        var adminPassword = "admin123"
        val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun transLit(str: String): String {
            var tempStr = str
            val lat_up = arrayOf("A", "B", "V", "G", "D", "E", "Yo", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "Kh", "Ts", "Ch", "Sh", "Shch", "\"", "Y", "'", "E", "Yu", "Ya")
            val lat_low = arrayOf("a", "b", "v", "g", "d", "e", "yo", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "kh", "ts", "ch", "sh", "shch", "\"", "y", "'", "e", "yu", "ya")
            val rus_up = arrayOf("А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь", "Э", "Ю", "Я")
            val rus_low = arrayOf("а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я")

            for (i in 0..32){
                tempStr = tempStr.replace(rus_up[i],lat_up[i]).replace(rus_low[i],lat_low[i])
            }
            return tempStr
        }

        fun calculateDays(createdAt: Long, expiredAtx: Long): Int {
            val diff: Long = expiredAtx - createdAt
            val totalDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
            val diff1 = System.currentTimeMillis() - createdAt
            val goneDays = TimeUnit.DAYS.convert(diff1, TimeUnit.MILLISECONDS).toInt()
            return totalDays - goneDays
        }

        fun getDateFromDays(days: Int):String{
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val c = Calendar.getInstance()
            c.add(Calendar.DATE, days)
            return sdf.format(c.time)
        }

        fun getCurrentDateString():String{
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            return sdf.format(System.currentTimeMillis())
        }


        fun openKeyboar(context: Context) {
            val imm: InputMethodManager? =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

            imm!!.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        fun hideKeyboar(context: Context) {
            val imm: InputMethodManager? =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

            imm!!.toggleSoftInput(
                InputMethodManager.HIDE_IMPLICIT_ONLY,
                0
            )
        }

        fun d(TAG: String?, message: String) {
            val maxLogSize = 20000
            for (i in 0..message.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > message.length) message.length else end
                Log.d(TAG, message.substring(start, end))
            }
        }
    }
}