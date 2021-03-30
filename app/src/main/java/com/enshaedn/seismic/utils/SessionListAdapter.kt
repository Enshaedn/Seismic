package com.enshaedn.seismic.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.enshaedn.seismic.database.Session
import com.enshaedn.seismic.databinding.SessionItemViewBinding

class SessionListAdapter(val clickListener: SessionDetailListener): ListAdapter<Session, SessionListAdapter.ViewHolder>(SessionListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: SessionItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Session, clickListener: SessionDetailListener) {
            // Res needed for resource access - ie. R.drawable etc - not currently necessary for this app
            // val res = holder.itemView.context.resources
            binding.sessionItem = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SessionItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SessionListDiffCallback : DiffUtil.ItemCallback<Session>() {
    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem.sessionID == newItem.sessionID
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem == newItem
    }
}

class SessionDetailListener(val clickListener: (sessionID: Long) -> Unit) {
    fun onClick(session: Session) = clickListener(session.sessionID)
}