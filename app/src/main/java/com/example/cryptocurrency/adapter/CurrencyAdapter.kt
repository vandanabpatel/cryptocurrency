package com.example.cryptocurrency.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.data.CryptoCurrencyModel
import com.example.cryptocurrency.databinding.ItemCurrencyBinding

class CurrencyAdapter(private val context: Activity, private val listener: CurrencyListener) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private lateinit var list: List<CryptoCurrencyModel>

    fun doRefresh(list: List<CryptoCurrencyModel>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: CryptoCurrencyModel = list[position]
        holder.bind(item)
    }

    interface CurrencyListener {
        fun onItemClick(item: CryptoCurrencyModel)
    }

    inner class ViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CryptoCurrencyModel) {
            binding.tvName.text = item.name
            binding.tvUsd.text = "$" + String.format("%.2f", item.quote.usd.price)
            binding.tvQty.text = String.format("%.2f", item.qty)
            binding.tvSymbol.text = item.symbol
            binding.tvAssetValue.text = "$" + String.format("%.2f", item.assetValue)

            Glide.with(context)
                .load(item.logo)
                .into(binding.ivLogo)

            binding.llRoot.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }
}