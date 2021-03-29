package com.enshaedn.seismic.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.enshaedn.seismic.R
import com.enshaedn.seismic.database.SeismicDB
import com.enshaedn.seismic.databinding.FragmentSessionsListBinding
import com.enshaedn.seismic.viewModels.SessionsListViewModel
import com.enshaedn.seismic.viewModels.SessionsListViewModelFactory

class SessionsList : Fragment() {
    private val TAG = "SEISMIC_LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Sessions List Create")
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSessionsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sessions_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = SeismicDB.getInstance(application).seismicDao

        val viewModelFactory = SessionsListViewModelFactory(dataSource, application)

        val sessionsListViewModel = ViewModelProvider(this, viewModelFactory).get(SessionsListViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.sessionsListViewModel = sessionsListViewModel

        return binding.root
    }
}