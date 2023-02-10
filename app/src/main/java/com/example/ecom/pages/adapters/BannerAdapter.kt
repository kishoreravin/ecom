package com.example.ecom.pages.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecom.databinding.AdapterBannerItemBinding
import com.example.ecom.models.BannerModel

class BannerAdapter(private val bannerList: List<BannerModel>, private val onBannerClick: (bannerName: String) -> Unit) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val mBrandBinding = AdapterBannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(mBrandBinding)
    }

    override fun getItemCount(): Int = bannerList.size

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class BannerViewHolder(private val brandBinding: AdapterBannerItemBinding) : RecyclerView.ViewHolder(brandBinding.root) {
        fun bindData(position: Int) {
            val banner = bannerList[position]
            Glide.with(brandBinding.imgBannerImage.context)
                .load(banner.bannerUrl)
                .into(brandBinding.imgBannerImage)
            brandBinding.imgBannerImage.setOnClickListener {
                banner.bannerName?.let {
                    onBannerClick(it)
                }
            }

        }
    }
}