package com.enshaedn.seismic.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enshaedn.seismic.R
import com.enshaedn.seismic.database.SeismicDB
import com.enshaedn.seismic.databinding.FragmentSeismicActiveBinding
import com.enshaedn.seismic.viewModels.SeismicActiveViewModel
import com.enshaedn.seismic.viewModels.SeismicActiveViewModelFactory

class SeismicActive : Fragment() {
    private val TAG = "SEISMIC_LOG"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "Active Screen")
        val binding: FragmentSeismicActiveBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seismic_active, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = SeismicActiveArgs.fromBundle(requireArguments())

        val dataSource = SeismicDB.getInstance(application).seismicDao

        val viewModelFactory = SeismicActiveViewModelFactory(arguments.sessionKey, dataSource)

        val seismicActiveViewModel = ViewModelProvider(this, viewModelFactory).get(SeismicActiveViewModel::class.java)

        // Navigate to Finalize Screen with active session primary key
        seismicActiveViewModel.navigateToFinalize.observe(viewLifecycleOwner, { activeKey ->
            activeKey?.let {
                this.findNavController().navigate(SeismicActiveDirections.actionSeismicActiveToSeismicFinalize(activeKey))
                seismicActiveViewModel.doneNavigating()
            }
        })

        // Observe the active session's SessionMeasurement
        seismicActiveViewModel.getActiveSession().observe(viewLifecycleOwner, {
            it?.let {
                Log.d(TAG, "${it.session.sessionID}")
                it.sessionMeasurements.forEach {
                    Log.d(TAG, "${it.measurementID} : ${it.measurement}")
                }
            }
        })

        binding.setLifecycleOwner(this)

        binding.seismicActiveViewModel = seismicActiveViewModel

        return binding.root
    }
}