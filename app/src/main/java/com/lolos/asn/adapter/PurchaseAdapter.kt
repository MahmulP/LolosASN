package com.lolos.asn.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lolos.asn.R
import com.lolos.asn.data.response.TryoutBundleItem
import com.lolos.asn.databinding.ListBundleTryoutBinding
import com.lolos.asn.ui.dialog.ValidationPurchaseDialogFragment
import java.text.NumberFormat
import java.util.Locale

class PurchaseAdapter(private val context: Context) : ListAdapter<TryoutBundleItem, PurchaseAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListBundleTryoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bundle = getItem(position)
        holder.bind(bundle, context)

        holder.itemView.setOnClickListener {
            if (bundle.boolBought != true) {
                val bundleId = bundle.tryoutBundleId

                val dialog = ValidationPurchaseDialogFragment.newInstance(bundleId)
                val activity = context as? AppCompatActivity
                activity?.supportFragmentManager?.let { fragmentManager ->
                    dialog.show(fragmentManager, "ValidationPurchaseDialog")
                }
            } else {
                Toast.makeText(context, context.getString(R.string.owned), Toast.LENGTH_SHORT).show()
            }

        }
    }

    class MyViewHolder(val binding: ListBundleTryoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(bundle: TryoutBundleItem, context: Context) {
            val price = bundle.price?.toIntOrNull() ?: 0
            val basePrice = bundle.basePrice ?: 0
            val indonesianLocale = Locale("in", "ID")

            val formattedPrice = NumberFormat.getNumberInstance(indonesianLocale).format(price)
            val formattedBasePrice = NumberFormat.getNumberInstance(indonesianLocale).format(basePrice)
            val priceString = context.getString(R.string.price, formattedPrice)
            val basePriceString = context.getString(R.string.price, formattedBasePrice)

            if (bundle.boolBought == true) {
                binding.tvTotalPackage.text = bundle.userBought?.size.toString()
                binding.tvNormalPrice.visibility = View.GONE
                binding.tvPrice.text = context.getString(R.string.owned_bundle)
            } else {
                binding.tvTotalPackage.text = bundle.listTryoutId?.size.toString()
                binding.tvPrice.text = basePriceString
            }

            binding.tvTitle.text = bundle.tryoutBundleName

            if (basePrice == price) {
                binding.tvNormalPrice.visibility = View.GONE
            } else {
                binding.tvNormalPrice.text = priceString
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TryoutBundleItem>() {
            override fun areItemsTheSame(oldItem: TryoutBundleItem, newItem: TryoutBundleItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TryoutBundleItem, newItem: TryoutBundleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
