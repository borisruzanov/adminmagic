package com.boris.expert.adminmagic.view.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.boris.expert.adminmagic.R
import com.boris.expert.adminmagic.utils.Constants
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var context: Context
    private lateinit var ticketsBtn: MaterialButton
    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setUpToolbar()
    }

    private fun initViews() {
        context = this
        ticketsBtn = findViewById(R.id.tickets_btn)
        toolbar = findViewById(R.id.toolbar)
        ticketsBtn.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser
        if (currentUser == null) {
            adminLogin(Constants.adminEmail, Constants.adminPassword)
        }
        else{
            Constants.firebaseUserId = currentUser.uid
        }


    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.admin_console_text)
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.black))
    }

    private fun adminLogin(email: String, password: String) {
        startLoading(context)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    val user = auth.currentUser
                    if (user != null){
                        Constants.firebaseUserId = user.uid
                    }
                    dismiss()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    dismiss()
                }
            }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tickets_btn -> {
                startActivity(Intent(context, TicketsActivity::class.java))
            }
            else -> {

            }
        }
    }
}