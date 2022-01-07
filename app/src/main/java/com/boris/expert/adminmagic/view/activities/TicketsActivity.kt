package com.boris.expert.adminmagic.view.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boris.expert.adminmagic.R
import com.boris.expert.adminmagic.adapters.TicketsAdapter
import com.boris.expert.adminmagic.model.SupportTicket
import com.boris.expert.adminmagic.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TicketsActivity : BaseActivity(), TicketsAdapter.OnItemClickListener {


    private lateinit var context: Context
    private lateinit var toolbar: Toolbar
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var ticketList = mutableListOf<SupportTicket>()
    private lateinit var ticketsRecyclerView: RecyclerView
    private lateinit var adapter: TicketsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        initViews()
        setUpToolbar()
    }

    private fun initViews() {
        context = this
        toolbar = findViewById(R.id.toolbar)
        ticketsRecyclerView = findViewById(R.id.tickets_recyclerview)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        ticketsRecyclerView.layoutManager = LinearLayoutManager(context)
        ticketsRecyclerView.hasFixedSize()
        adapter = TicketsAdapter(context, ticketList as ArrayList<SupportTicket>)
        ticketsRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)

    }

    // THIS FUNCTION WILL RENDER THE ACTION BAR/TOOLBAR
    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.tickets_text)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.black))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getAllTickets()
    }

    private fun getAllTickets() {
        startLoading(context)
        databaseReference.child(Constants.ticketsReference).orderByChild("timeStamp")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dismiss()
                    if (snapshot.hasChildren()) {
                        ticketList.clear()
                        for (dataSnap: DataSnapshot in snapshot.children) {
                            ticketList.add(dataSnap.getValue(SupportTicket::class.java) as SupportTicket)
                        }

                        if (ticketList.isNotEmpty()) {
                            adapter.notifyItemRangeChanged(0, ticketList.size)
                        }
                    } else {
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onItemClick(position: Int) {
        val item = ticketList[position]
        startActivity(Intent(context,ChatActivity::class.java).apply {
            putExtra("S_TICKET",item)
        })
    }
}