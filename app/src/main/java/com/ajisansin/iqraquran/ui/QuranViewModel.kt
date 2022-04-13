package com.ajisansin.iqraquran.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajisansin.iqraquran.data.source.QuranApi
import com.ajisansin.iqraquran.data.source.QuranEntity
import kotlinx.coroutines.launch

enum class QuranApiStatus {LOADING, ERROR, DONE}

class QuranViewModel : ViewModel() {

    private val _status = MutableLiveData<QuranApiStatus>()
    val status: LiveData<QuranApiStatus> = _status

    private val _quranEntities = MutableLiveData<List<QuranEntity>>()
    val quranEntities: LiveData<List<QuranEntity>> = _quranEntities

    private val _quran = MutableLiveData<QuranEntity>()
    val quran: LiveData<QuranEntity> = _quran

    fun getQuranList() {
        viewModelScope.launch {
            _status.value = QuranApiStatus.LOADING
            Log.d("Quran" , "Loading")
            try {
                _quranEntities.value = QuranApi.retrofitService.getSurah()
                _status.value = QuranApiStatus.DONE
                Log.d("Quran" , "Success$_quranEntities")
            } catch (e: Exception) {
                _status.value = QuranApiStatus.ERROR
                _quranEntities.value = listOf()
                Log.d("Quran" , "Failed")
            }
        }
    }

    fun onQuranClicked(quran: QuranEntity) {
        // Set the amphibian object
        _quran.value = quran
    }
}