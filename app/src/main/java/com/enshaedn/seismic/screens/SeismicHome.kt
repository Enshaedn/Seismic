package com.enshaedn.seismic.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enshaedn.seismic.database.SeismicDB
import com.enshaedn.seismic.R
import com.enshaedn.seismic.viewModels.SeismicViewModel
import com.enshaedn.seismic.viewModels.SeismicViewModelFactory
import com.enshaedn.seismic.databinding.FragmentSeismicHomeBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

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

        seismicViewModel.navigateToFinalize.observe(viewLifecycleOwner, Observer { session ->
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

//        val graph: GraphView = binding.activeGraph
//        graph.addSeries(seismicViewModel.graphData)

//        seismicViewModel.cM.observe(viewLifecycleOwner, {
//            it?.forEach {
//                it.sessionMeasurements.forEach {
//                    Log.d(TAG, "${it.sessionID} : ${it.measurementID} : ${it.measurement}")
//                }
//            }
//        })

        binding.setLifecycleOwner(this)

        binding.seismicViewModel = seismicViewModel

        return binding.root
    }
}