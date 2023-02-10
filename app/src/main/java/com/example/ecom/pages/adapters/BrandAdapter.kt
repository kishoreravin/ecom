package com.example.ecom.pages.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecom.databinding.AdapterBrandItemBinding
import com.example.ecom.models.BrandModel

class BrandAdapter(private val brands: List<BrandModel>) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val mBrandBinding = AdapterBrandItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(mBrandBinding)
    }

    override fun getItemCount(): Int = brands.size

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class BrandViewHolder(private val mBrandBinding: AdapterBrandItemBinding) : RecyclerView.ViewHolder(mBrandBinding.root) {
        fun bindData(position: Int) {
            val brand = brands[position]
            Glide.with(mBrandBinding.imgBrandImage.context)
                .load(brand.brandUrl)
                .into(mBrandBinding.imgBrandImage)
            val shapeAppearanceModel = mBrandBinding.imgBrandImage.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(90f)
                .build()
            mBrandBinding.imgBrandImage.shapeAppearanceModel = shapeAppearanceModel
        }
    }
}