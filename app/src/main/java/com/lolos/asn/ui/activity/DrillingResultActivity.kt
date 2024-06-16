package com.lolos.asn.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lolos.asn.R
import com.lolos.asn.databinding.ActivityDrillingResultBinding

class DrillingResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrillingResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrillingResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pieChart: PieChart = findViewById(R.id.chart_data)

        // Create entries for the PieChart
        val entries = listOf(
            PieEntry(40f, null as String?), // No label for this entry
            PieEntry(30f, null as String?),
            PieEntry(30f, null as String?)
        )

        // Create a DataSet and set its properties
        val dataSet = PieDataSet(entries, "Difficulty Levels")
        val greenColor = ContextCompat.getColor(this, R.color.green_400)
        val primaryColor = ContextCompat.getColor(this, R.color.primaryColor)
        val redColor = ContextCompat.getColor(this, R.color.red)

// Now use these colors in your PieChart configuration
        dataSet.colors = listOf(greenColor, primaryColor, redColor)
        dataSet.setDrawValues(false)

        // Create PieData and set it to the PieChart
        val pieData = PieData(dataSet)
        pieChart.data = pieData

        // Configure additional properties for the PieChart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = false
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.legend.isEnabled = false

        // Refresh the chart
        pieChart.invalidate()
    }
}