package com.ajisansin.iqraquran.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.ajisansin.iqraquran.R
import com.ajisansin.iqraquran.viewmodel.QuranReminderViewModel
import com.ajisansin.iqraquran.viewmodel.QuranReminderViewModelFactory
import java.util.concurrent.TimeUnit

class ReminderDialogFragment(private val quranName: String) : DialogFragment() {

    private val viewModel: QuranReminderViewModel by viewModels {
        QuranReminderViewModelFactory(requireActivity().application)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
                .setTitle(R.string.quran_reminder)
                .setItems(R.array.quran_schedule_array) { _, position ->
                    when (position) {
                        0 ->
                            viewModel
                                .scheduleReminder(5, TimeUnit.SECONDS, quranName)
                        1 ->
                            viewModel
                                .scheduleReminder(1, TimeUnit.DAYS, quranName)
                        2 ->
                            viewModel
                                .scheduleReminder(7, TimeUnit.DAYS, quranName)
                        3 ->
                            viewModel
                                .scheduleReminder(30, TimeUnit.DAYS, quranName)
                    }
                    Toast.makeText(context, "Reminder Successfully Created", Toast.LENGTH_LONG).show()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}