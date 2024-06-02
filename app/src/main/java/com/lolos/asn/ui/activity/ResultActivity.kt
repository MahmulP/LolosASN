package com.lolos.asn.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lolos.asn.R
import com.lolos.asn.data.preference.UserPreferences
import com.lolos.asn.data.preference.userPreferencesDataStore
import com.lolos.asn.data.response.FinishedTryoutResponse
import com.lolos.asn.data.response.TryoutResultResponse
import com.lolos.asn.data.viewmodel.factory.AuthViewModelFactory
import com.lolos.asn.data.viewmodel.model.AuthViewModel
import com.lolos.asn.data.viewmodel.model.TryoutViewModel
import com.lolos.asn.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val tryoutViewModel by viewModels<TryoutViewModel>()
    private val authViewModel: AuthViewModel by viewModels {
        val pref = UserPreferences.getInstance(application.userPreferencesDataStore)
        AuthViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tryoutId = intent.getStringExtra("tryout_id")
        authViewModel.getAuthUser().observe(this) { userData ->
            if (userData.userId != null) {
                val userId = userData.userId
                tryoutViewModel.getResultTryout(tryoutId = tryoutId, userId = userId)
                tryoutViewModel.getFinishedTryout(userId = userId)
            }
        }

        tryoutViewModel.resultTryout.observe(this) { tryoutResult ->
            if (tryoutResult != null) {
                val resultText = tryoutResult.data.tryoutScore
                val twkScore = tryoutResult.data.twkScore
                val tkpScore = tryoutResult.data.tkpScore
                val tiuScore = tryoutResult.data.tiuScore
                val redColorStateList = ContextCompat.getColorStateList(this, R.color.red)
                val redColor = ContextCompat.getColor(this, R.color.red)

                binding.tvPoint.text = getString(R.string.total_point, resultText)
                binding.toolbar.title = tryoutResult.data.tryout?.tryoutTitle
                binding.tvTwkPoint.text = twkScore.toString()
                binding.tvTkpPoint.text = tkpScore.toString()
                binding.tvTiuPoint.text = tiuScore.toString()

                if ((twkScore ?: 0) < 65) {
                    binding.tvTwk.backgroundTintList = redColorStateList
                    binding.cvTwk.strokeColor = redColor
                }

                if ((tkpScore ?: 0) < 165) {
                    binding.tvTkp.backgroundTintList = redColorStateList
                    binding.cvTkp.strokeColor = redColor
                }

                if ((tiuScore ?: 0) < 80) {
                    binding.tvTiu.backgroundTintList = redColorStateList
                    binding.cvTiu.strokeColor = redColor
                }

                if (tryoutResult.data.tryoutPassed == "Failed") {
                    binding.tvPoint.backgroundTintList = redColorStateList
                    binding.ivStatus.setImageResource(R.drawable.ic_failed)
                    binding.tvInfo.text = getString(R.string.failed)
                    binding.tvInfo.setTextColor(redColorStateList)
                }

                showChartSubtest(tryoutResult)
            }
        }

        tryoutViewModel.finishedTryout.observe(this) { tryoutResponse ->
            if (tryoutResponse != null) {
                showChartTryout(tryoutResponse)
            }
        }

        binding.tvDetailRank.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            intent.putExtra("tryout_id", tryoutId)
            startActivity(intent)
        }

        binding.tvDetailPembahasan.setOnClickListener {
            startActivity(Intent(this, ResultDiscussionActivity::class.java))
        }

        binding.tvDetailAi.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        binding.btnDone.setOnClickListener {
            val intent = Intent(this, ResultHistoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showChartSubtest(tryoutResult: TryoutResultResponse) {
        val chart: BarChart = binding.chartSubtest

        // Extract scores from tryoutResult
        val twkScore = tryoutResult.data.twkScore ?: 0
        val tiuScore = tryoutResult.data.tiuScore ?: 0
        val tkpScore = tryoutResult.data.tkpScore ?: 0

        // Create BarEntry objects using the extracted scores
        val entries = listOf(
            BarEntry(0f, 0f),                  // Dummy entry for padding
            BarEntry(1f, twkScore.toFloat()),  // TWK
            BarEntry(2f, tiuScore.toFloat()),  // TIU
            BarEntry(3f, tkpScore.toFloat()),  // TKP
            BarEntry(4f, 0f)                   // Dummy entry for padding
        )

        // Set colors based on conditions
        val colors = listOf(
            Color.TRANSPARENT,
            if (twkScore < 65) {
                ContextCompat.getColor(this, R.color.red)
            } else {
                ContextCompat.getColor(this, R.color.primaryColor)
            },
            if (tiuScore < 80) {
                ContextCompat.getColor(this, R.color.red)
            } else {
                ContextCompat.getColor(this, R.color.primaryColor)
            },
            if (tkpScore < 165) {
                ContextCompat.getColor(this, R.color.red)
            } else {
                ContextCompat.getColor(this, R.color.primaryColor)
            },
            Color.TRANSPARENT
        )


        // Create a BarDataSet
        val dataSet = BarDataSet(entries, "Passing Grades")
        val formatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value == 0f) "" else value.toString()
            }
        }
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueFormatter = formatter

        // Create BarData
        val barData = BarData(dataSet)
        barData.barWidth = 0.9f

        // Set BarData to the chart
        chart.data = barData

        // Customize the chart
        chart.setFitBars(true)
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.xAxis.granularity = 1f
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            private val labels = arrayOf("", "TWK", "TIU", "TKP", "")

            override fun getFormattedValue(value: Float): String {
                return labels[value.toInt()]
            }
        }

        chart.axisLeft.granularity = 1f
        chart.axisLeft.axisMinimum = 0f

        val topPadding = 30f
        val maxYValue = entries.maxByOrNull { it.y }?.y ?: 0f
        chart.axisLeft.axisMaximum = maxYValue + topPadding

        // Add Limit Lines
        val limitLineTWK = LimitLine(65f, "TWK")
        limitLineTWK.lineWidth = 2f
        limitLineTWK.lineColor = Color.GRAY
        limitLineTWK.enableDashedLine(10f, 10f, 0f)
        limitLineTWK.labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
        limitLineTWK.textSize = 10f
        limitLineTWK.textColor = Color.BLACK

        val limitLineTIU = LimitLine(80f, "TIU")
        limitLineTIU.lineWidth = 2f
        limitLineTIU.lineColor = Color.GRAY
        limitLineTIU.enableDashedLine(10f, 10f, 0f)
        limitLineTIU.labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
        limitLineTIU.textSize = 10f
        limitLineTIU.textColor = Color.BLACK

        val limitLineTKP = LimitLine(165f, "TKP")
        limitLineTKP.lineWidth = 2f
        limitLineTKP.lineColor = Color.GRAY
        limitLineTKP.enableDashedLine(10f, 10f, 0f)
        limitLineTKP.labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
        limitLineTKP.textSize = 10f
        limitLineTKP.textColor = Color.BLACK

        val yAxis = chart.axisLeft
        yAxis.addLimitLine(limitLineTWK)
        yAxis.addLimitLine(limitLineTIU)
        yAxis.addLimitLine(limitLineTKP)
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = maxYValue + topPadding
        yAxis.setDrawGridLines(false)

        // Customize the look
        chart.axisLeft.setDrawGridLines(false)
        chart.xAxis.setDrawGridLines(false)
        chart.legend.isEnabled = false
        chart.setDrawValueAboveBar(true)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)

        chart.invalidate()
    }

    private fun showChartTryout(finishedTryoutResponse: FinishedTryoutResponse) {
        val chart: BarChart = binding.chartTryout

        val finishedTryouts = finishedTryoutResponse.data

        // Sort finishedTryouts by createdAt and take the last 5 entries
        val limitedTryouts = finishedTryouts.sortedByDescending { it.createdAt }.takeLast(5)

        val entries = mutableListOf<BarEntry>()
        val colors = mutableListOf<Int>()

        val primaryColor = ContextCompat.getColor(this, R.color.primaryColor)
        val redColor = ContextCompat.getColor(this, R.color.red)

        // Add initial entry
        entries.add(BarEntry(0f, 0f))
        colors.add(primaryColor) // Initial color

        // Add up to 5 entries from limitedTryouts
        limitedTryouts.forEachIndexed { index, finishedTryout ->
            entries.add(BarEntry((index + 1).toFloat(), finishedTryout.tryoutScore?.toFloat() ?: 0f))
            // Add color based on tryoutStatus
            val color = if (finishedTryout.tryoutPassed == "Passed") primaryColor else redColor
            colors.add(color)
        }

        // Add final entry if necessary
        entries.add(BarEntry((limitedTryouts.size + 1).toFloat(), 0f))
        colors.add(primaryColor) // Final color

        val labels = mutableListOf<String>()
        labels.add("")
        entries.forEachIndexed { index, _ ->
            val label = if (index < (entries.size - 2)) "Tryout ${index + 1}" else ""
            labels.add(label)
        }

        // Create a BarDataSet
        val dataSet = BarDataSet(entries, "Tryout History")
        val formatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value == 0f) "" else value.toString()
            }
        }
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueFormatter = formatter

        // Create BarData
        val barData = BarData(dataSet)
        barData.barWidth = 0.9f

        // Set BarData to the chart
        chart.data = barData

        // Customize the chart
        chart.setFitBars(true)
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.xAxis.granularity = 1f
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels[value.toInt()]
            }
        }
        chart.axisLeft.granularity = 1f
        chart.axisLeft.axisMinimum = 0f

        val topPadding = 30f
        val maxYValue = entries.maxByOrNull { it.y }?.y ?: 0f
        chart.axisLeft.axisMaximum = maxYValue + topPadding

        // Customize the look
        chart.axisLeft.setDrawGridLines(false)
        chart.xAxis.setDrawGridLines(false)
        chart.legend.isEnabled = false
        chart.setDrawValueAboveBar(true)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)

        chart.invalidate() // Refresh the chart
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, ResultHistoryActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}