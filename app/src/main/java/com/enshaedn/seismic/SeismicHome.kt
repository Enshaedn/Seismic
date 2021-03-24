package com.enshaedn.seismic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class SeismicHome : Fragment() {
    private val TAG = "SEISMIC_LOG"
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Seismic Home Create")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "Seismic Home Create View")
        return inflater.inflate(R.layout.fragment_seismic_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Seismic Home View Created")

        startButton = view.findViewById<Button>(R.id.startSession)
        startButton.setOnClickListener {
            findNavController().navigate(R.id.action_seismicHome_to_seismic)
        }
    }
}