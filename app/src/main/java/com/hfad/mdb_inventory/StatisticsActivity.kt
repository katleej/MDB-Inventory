package com.hfad.mdb_inventory

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Fetches, calculatues, and displays basic statistics about the data set
 */
class StatisticsActivity : AppCompatActivity() {
    lateinit var purchaseCountText:TextView
    lateinit var purchaseSumText:TextView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        purchaseCountText = findViewById(R.id.purchaseCount)
        purchaseSumText = findViewById(R.id.purchaseSum)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE
        CloudDatabase().getPurchases({ model ->
            progressBar.visibility = View.INVISIBLE

            purchaseCountText.text = "Total purchases: ${model.size}"

            var totalValue = 0.0
            for (m in model) {
                m.price.toFloatOrNull()?.let {
                    totalValue += it
                }
            }
            purchaseSumText.text = "Total spent: \$%.2f".format(totalValue)
        }, { exception ->
            progressBar.visibility = View.INVISIBLE
            exception.printStackTrace()
        })
    }
}
