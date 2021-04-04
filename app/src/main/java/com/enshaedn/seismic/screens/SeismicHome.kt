package com.enshaedn.seismic.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enshaedn.seismic.database.SeismicDB
import com.enshaedn.seismic.R
import com.enshaedn.seismic.viewModels.SeismicViewModel
import com.enshaedn.seismic.viewModels.SeismicViewModelFactory
import com.enshaedn.seismic.databinding.FragmentSeismicHomeBinding

class SeismicHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSeismicHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seismic_home, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = SeismicDB.getInstance(application).seismicDao

        val viewModelFactory = SeismicViewModelFactory(dataSource, application)

        val seismicViewModel = ViewModelProvider(this, viewModelFactory).get(SeismicViewModel::class.java)

        // Navigate to Active Screen with Active Session Key
        seismicViewModel.navigateToActive.observe(viewLifecycleOwner, { activeKey ->
            activeKey?.let {
                this.findNavController().navigate(SeismicHomeDirections.actionSeismicHomeToSeismicActive(activeKey))
                seismicViewModel.doneNavigating()
            }
        })

        // Navigate to the List Screen
        seismicViewModel.navigateToSessionsList.observe(viewLifecycleOwner, {
            if(it == true) {
                this.findNavController().navigate(SeismicHomeDirections.actionSeismicHomeToSessionsList())
                seismicViewModel.doneNavigating()
            }
        })

        binding.setLifecycleOwner(this)

        binding.seismicViewModel = seismicViewModel

        return binding.root
    }
}