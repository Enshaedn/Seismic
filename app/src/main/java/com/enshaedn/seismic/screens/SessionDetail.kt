package com.enshaedn.seismic.screens

import android.os.Bundle
import android.util.Log
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
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter

class SessionDetail : Fragment() {
    private val TAG = "SEISMIC_LOG"

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

        sessionDetailViewModel.getSession().observe(viewLifecycleOwner, {
            it?.let {
                Log.d(TAG, "Session Loaded?")
                sessionDetailViewModel.gatherDataPoints()
            }
        })

        sessionDetailViewModel.getDataPoints().observe(viewLifecycleOwner, {
            if(it != null) {
                binding.sessionDetailGraph.apply {
                    addSeries(it)
//                    gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
//                    gridLabelRenderer.numHorizontalLabels = 3
//                    viewport.setMinX(it.lowestValueX)
//                    viewport.setMaxX(it.highestValueX)
//                    viewport.isXAxisBoundsManual = true
//                    gridLabelRenderer.setHumanRounding(false)
//                    viewport.setScalableY(true)
                    viewport.isScalable = true
                    viewport.isScrollable = true
                }
            }
        })

        binding.lifecycleOwner = this

        binding.sessionDetailViewModel = sessionDetailViewModel

        return binding.root
    }
}