package com.ajisansin.iqraquran.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ajisansin.iqraquran.worker.QuranReminderWorker
import com.ajisansin.iqraquran.worker.QuranReminderWorker.Companion.nameKey
import java.util.concurrent.TimeUnit

class QuranReminderViewModel(application: Application) : ViewModel() {

    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        quranName: String
    ) {

        val data = Data.Builder()
            .putString(nameKey, quranName)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<QuranReminderWorker>()
            .setInitialDelay(duration, unit)
            .setInputData(data)
            .build()

        WorkManager.getInstance().enqueueUniqueWork(
            quranName,
            ExistingWorkPolicy.REPLACE, oneTimeWorkRequest
        )

    }
}

class QuranReminderViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(QuranReminderViewModel::class.java)) {
            QuranReminderViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}