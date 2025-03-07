package com.example.cryptocurrency.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.BaseAppCompatActivity
import com.example.cryptocurrency.adapter.CurrencyAdapter
import com.example.cryptocurrency.api.ApiClient
import com.example.cryptocurrency.data.CryptoCurrencyModel
import com.example.cryptocurrency.databinding.ActivityMainBinding
import com.example.cryptocurrency.utilities.Utility
import com.example.cryptocurrency.viewmodel.MainRepository
import com.example.cryptocurrency.viewmodel.MainViewModel
import com.example.cryptocurrency.viewmodel.MyViewModelFactory

/**
 * This class fetch top 20 list of crypto currency fetch it from CoinMarketCap
 */
class MainActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val context: Activity = this@MainActivity
    private val TAG = javaClass.simpleName

    lateinit var viewModel: MainViewModel

    private lateinit var adapterCurrency: CurrencyAdapter
    private lateinit var listCurrency: List<CryptoCurrencyModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        initListener()
        fetchCurrency()
    }

    override fun onClick(p0: View?) {
        Utility.hideSoftKeyboard(context)
    }

    private fun initListener() {
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(MainRepository(ApiClient.apiInterface(context)))
        ).get(MainViewModel::class.java)
    }

    private fun fillAdapter() {
        if (!::adapterCurrency.isInitialized) {
            adapterCurrency = CurrencyAdapter(context, object : CurrencyAdapter.CurrencyListener {
                override fun onItemClick(item: CryptoCurrencyModel) {
                    navigateToDetailScreen(item)
                }
            })
        }

        adapterCurrency.doRefresh(listCurrency)

        if (binding.rvData.adapter == null) {
            binding.rvData.layoutManager = LinearLayoutManager(context)
            binding.rvData.adapter = adapterCurrency
        }
    }

    private fun fetchCurrency() {
        if (!isNetworkAvailable(context, true)) {
            return
        }

        viewModel.totalValue.observe(this, {
            binding.tvUsd.text = "$" + String.format("%.2f", it)
        })

        viewModel.currencyList.observe(this, {
            listCurrency = it
            fillAdapter()
        })

        viewModel.errorMessage.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loading.observe(this) {
            if (it) {
                showProgress(context)
            } else {
                hideProgress()
            }
        }

        // api call
        viewModel.getCurrencyList()
    }

    private fun navigateToDetailScreen(item: CryptoCurrencyModel) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("cryptoCurrency", item)
        startActivity(intent)
    }
}
