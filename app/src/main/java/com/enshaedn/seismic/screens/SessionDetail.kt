package com.enshaedn.seismic.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.enshaedn.seismic.R
import com.enshaedn.seismic.database.SeismicDB
import com.enshaedn.seismic.databinding.FragmentSessionDetailBinding
import com.enshaedn.seismic.viewModels.SessionDetailViewModel
import com.enshaedn.seismic.viewModels.SessionDetailViewModelFactory

class SessionDetail : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSessionDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_session_detail, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = SessionDetailArgs.fromBundle(requireArguments())

        val dataSource = SeismicDB.getInstance(application).seismicDao

        val viewModelFactory = SessionDetailViewModelFactory(arguments.sessionKey, dataSource)

        val sessionDetailViewModel = ViewModelProvider(this, viewModelFactory).get(SessionDetailViewModel::class.java)

        binding.setLifecycleOwner(this)

        binding.sessionDetailViewModel = sessionDetailViewModel

        return binding.root
    }
}