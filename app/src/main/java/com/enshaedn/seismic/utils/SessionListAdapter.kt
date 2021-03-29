package com.enshaedn.seismic.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enshaedn.seismic.R
import com.enshaedn.seismic.database.Session

class SessionListAdapter: RecyclerView.Adapter<SessionItemViewHolder>() {
    var data = listOf<Session>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SessionItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = "${item.sessionID} : ${item.title}"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionItemViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.session_item_view, parent, false) as TextView
        return SessionItemViewHolder(view)
    }
}