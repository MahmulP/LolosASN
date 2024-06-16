package com.lolos.asn.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.DrillingViewModel
import com.lolos.asn.databinding.ActivityDrillingResultBinding
import com.lolos.asn.utils.formatSecondsToMinutesSeconds

class DrillingResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrillingResultBinding

    private val drillingViewModel by viewModels<DrillingViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(this.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    private var totalMudah: Int? = 0
    private var totalSedang: Int? = 0
    private var totalSusah: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrillingResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationIconTint(ContextCompat.getColor(this, R.color.white))

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val historyLatId = intent.getStringExtra("latHistory_id")

        authViewModel.getAuthUser().observe(this) {
            val userId = it.userId
            val token = "Bearer ${it.token}"

            if (userId != null) {
                drillingViewModel.getHistoryDetailDrilling(historyLatId = historyLatId, userId = userId, token = token)
            }
        }

        drillingViewModel.drillingHistoryDetai.observe(this) { history ->
            totalMudah = history?.data?.totalMudah
            totalSedang = history?.data?.totalSedang
            totalSusah = history?.data?.totalSusah

            binding.tvScore.text = history?.data?.totalScore.toString()
            binding.tvSkorEasy.text = getString(R.string.total_soal, totalMudah)
            binding.tvSkorMedium.text = getString(R.string.total_soal, totalSedang)
            binding.tvSkorHard.text = getString(R.string.total_soal, totalSusah)
            binding.tvTotalKosong.text = history?.data?.totalKosong.toString()
            binding.tvTotalBenar.text = history?.data?.totalBenar.toString()
            binding.tvTotalSalah.text = history?.data?.totalSalah.toString()
            binding.tvTime.text = formatSecondsToMinutesSeconds(history?.data?.totalPengerjaan ?: 0)

            setupPieChart()
        }

        binding.btnDone.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navigateFrom = intent.getStringExtra("navigate_from")

        return when (item.itemId) {
            android.R.id.home -> {
                if (navigateFrom == "exam") {
                    val intent = Intent(this@DrillingResultActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupPieChart() {
        val pieChart: PieChart = binding.chartData

        val totalMudahFloat = totalMudah?.toFloat()
        val totalSedangFloat = totalSedang?.toFloat()
        val totalSulitFloat = totalSusah?.toFloat()

        val entries = listOf(
            totalMudahFloat?.let { PieEntry(it, null as String?) },
            totalSedangFloat?.let { PieEntry(it, null as String?) },
            totalSulitFloat?.let { PieEntry(it, null as String?) }
        )

        val dataSet = PieDataSet(entries, "Difficulty Levels")
        val greenColor = ContextCompat.getColor(this, R.color.green_400)
        val primaryColor = ContextCompat.getColor(this, R.color.primaryColor)
        val redColor = ContextCompat.getColor(this, R.color.red)

        dataSet.colors = listOf(greenColor, primaryColor, redColor)
        dataSet.setDrawValues(false)

        val pieData = PieData(dataSet)
        pieChart.data = pieData

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = false
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.legend.isEnabled = false

        pieChart.invalidate()
    }
}