package com.enshaedn.seismic.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.enshaedn.seismic.database.SeismicDao

class SeismicViewModel(
    val database: SeismicDao, application: Application
    ) : AndroidViewModel(application) {

}