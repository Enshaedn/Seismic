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
import com.enshaedn.seismic.database.SeismicDB
import com.enshaedn.seismic.R
import com.enshaedn.seismic.viewModels.SeismicViewModel
import com.enshaedn.seismic.viewModels.SeismicViewModelFactory
import com.enshaedn.seismic.databinding.FragmentSeismicHomeBinding

class SeismicHome : Fragment() {
    private val TAG = "SEISMIC_LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Seismic Home Create")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Log.d(TAG, "Seismic Home Create View")
        val binding: FragmentSeismicHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seismic_home, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = SeismicDB.getInstance(application).seismicDao

        val viewModelFactory = SeismicViewModelFactory(dataSource, application)

        val seismicViewModel = ViewModelProvider(this, viewModelFactory).get(SeismicViewModel::class.java)

        seismicViewModel.navigateToFinalize.observe(viewLifecycleOwner, { session ->
            session?.let {
                this.findNavController().navigate(SeismicHomeDirections.actionSeismicHomeToSeismicFinalize(session.sessionID))
                seismicViewModel.doneNavigating()
            }
        })

        seismicViewModel.navigateToSessionsList.observe(viewLifecycleOwner, {
            if(it == true) {
                this.findNavController().navigate(SeismicHomeDirections.actionSeismicHomeToSessionsList())
                seismicViewModel.doneNavigating()
            }
        })

//        seismicViewModel.getActiveSessionData().observe(viewLifecycleOwner, {
//            it?.let {
//                Log.d(TAG, it.session.sessionID.toString())
//                seismicViewModel.gatherDataPoints()
//            }
//        })

//        seismicViewModel.getDataPoints().observe(viewLifecycleOwner, {
//            if(it != null) {
//                Log.d(TAG, "Data Points")
//                it.getValues(it.lowestValueX, it.highestValueX).forEach { Log.d(TAG, it.toString()) }
//            }
//        })

        binding.setLifecycleOwner(this)

        binding.seismicViewModel = seismicViewModel

        return binding.root
    }
}