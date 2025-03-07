package com.example.cryptocurrency.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.cryptocurrency.BaseAppCompatActivity
import com.example.cryptocurrency.R
import com.example.cryptocurrency.data.CryptoCurrencyModel
import com.example.cryptocurrency.databinding.ActivityDetailBinding
import com.example.cryptocurrency.utilities.Utility
import com.example.cryptocurrency.utilities.Utility.intentSerializable

/**
 * This class show details of particular crypto currency
 *
 */
class DetailsActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private val context: Activity = this@DetailsActivity
    private val TAG = javaClass.simpleName

    lateinit var currencyModel: CryptoCurrencyModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        currencyModel =
            intent.intentSerializable("cryptoCurrency", CryptoCurrencyModel::class.java)!!

        initToolbar()
        initListener()
        displayData()
    }

    override fun onClick(p0: View?) {
        Utility.hideSoftKeyboard(context)
    }

    private fun initToolbar() {
        binding.toolbar.title = currencyModel.name

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initListener() {

    }

    private fun displayData() {
        binding.tvUsd.text = "$" + String.format("%.2f", currencyModel.quote.usd.price)

        val value1 = currencyModel.quote.usd.percentChange24h
        binding.tvValue1.text =
            String.format("%.2f", value1) + "%"
        binding.tvValue1.setTextColor(
            if (value1 >= 0) ContextCompat.getColor(
                context,
                R.color.green
            ) else ContextCompat.getColor(context, R.color.red)
        )

        binding.tvValue2.text = "$" + String.format("%.2f", currencyModel.amountChange)
        binding.tvValue2.setTextColor(
            if (currencyModel.amountChange >= 0) ContextCompat.getColor(
                context,
                R.color.green
            ) else ContextCompat.getColor(context, R.color.red)
        )

        Glide.with(context)
            .load(currencyModel.logo)
            .into(binding.ivLogo)

        binding.tvQty.text = String.format("%.2f", currencyModel.qty)
        binding.tvSymbol.text = currencyModel.symbol
        binding.tvAssetValue.text = "$" + String.format("%.2f", currencyModel.assetValue)
    }
}
