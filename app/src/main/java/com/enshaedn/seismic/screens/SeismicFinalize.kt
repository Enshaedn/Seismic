package com.enshaedn.seismic.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enshaedn.seismic.R
import com.enshaedn.seismic.database.SeismicDB
import com.enshaedn.seismic.databinding.FragmentSeismicFinalizeBinding
import com.enshaedn.seismic.viewModels.SeismicFinalizeViewModel
import com.enshaedn.seismic.viewModels.SeismicFinalizeViewModelFactory

class SeismicFinalize : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSeismicFinalizeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seismic_finalize, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = SeismicFinalizeArgs.fromBundle(requireArguments())

        val dataSource = SeismicDB.getInstance(application).seismicDao

        val viewModelFactory = SeismicFinalizeViewModelFactory(arguments.sessionKey, dataSource)

        val seismicFinalizeViewModel = ViewModelProvider(this, viewModelFactory).get(SeismicFinalizeViewModel::class.java)

        seismicFinalizeViewModel.navigateToHome.observe(viewLifecycleOwner, {
            if(it == true) {
                this.findNavController().navigate(SeismicFinalizeDirections.actionSeismicFinalizeToSeismicHome())
                seismicFinalizeViewModel.doneNavigating()
            }
        })

        binding.setLifecycleOwner(this)

        binding.seismicFinalizeViewModel = seismicFinalizeViewModel

        return binding.root
    }
}