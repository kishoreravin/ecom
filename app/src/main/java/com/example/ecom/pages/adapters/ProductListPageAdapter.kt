package com.example.ecom.pages.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.ecom.databinding.AdapterBannerLayoutBinding
import com.example.ecom.databinding.AdapterProductItemBinding
import com.example.ecom.models.ProductModel
import com.example.ecom.models.ViewTypes
import com.example.ecom.utils.setVisibility
import com.example.ecom.utils.strikeThrough
import com.example.ecom.utils.toCurrency

class ProductListPageAdapter(
    private val products: MutableList<ProductModel>,
    private val onSectionsClicked: (type: ViewTypes, data: String) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val mBinding = AdapterProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProductListViewHolder(mBinding)
            }
            else -> {
                val mBinding = AdapterBannerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BannerViewHolder(mBinding)
            }
        }
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as ProductListViewHolder).bindData(position)
            else -> (holder as BannerViewHolder).bindData(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (products[position].type) {
            ViewTypes.Product -> 0
            ViewTypes.Banner -> 1
            ViewTypes.None -> 2
        }
    }

    fun addProducts(productModels: List<ProductModel>, doReset: Boolean = false) {
        val size = products.size
        if (doReset) {
            products.clear()
        }
        products.addAll(productModels)
        if (!doReset) {
            notifyItemRangeInserted(size, products.size)
        } else {
            notifyDataSetChanged()
        }
    }

    fun addModels(productModel: ProductModel, index: Int) {
        products.add(index, productModel)
        notifyItemInserted(index)
    }

    inner class ProductListViewHolder(private val mBinding: AdapterProductItemBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bindData(position: Int) {
            val product = products[position]
            with(mBinding) {
                product.brand?.let {
                    tvBrandName.text = it
                }
                product.name?.let {
                    tvProductName.text = it
                }
                if (!product.offerPrice.isNullOrEmpty() && !product.price.isNullOrEmpty()) {
                    val offer = (product.price.toDoubleOrNull() ?: 0.0) - (product.offerPrice.toDoubleOrNull() ?: 0.0)
                    tvDiscountPrice.setVisibility(offer > 0)
                    tvDiscount.setVisibility(offer > 0)
                    if (offer > 0) {
                        tvDiscountPrice.text = product.offerPrice.toCurrency()
                        tvMrp.apply {
                            text = product.price
                            strikeThrough()
                        }
                        val offerText = "${offer.toString().toCurrency()} OFF"
                        tvDiscount.text = offerText
                    } else {
                        tvDiscountPrice.text = product.price.toCurrency()
                    }
                } else {
                    tvDiscount.setVisibility(false)
                    tvMrp.setVisibility(false)
                    tvDiscountPrice.setVisibility(false)
                }
                product.offerPrice?.let {
                    tvDiscountPrice.text = it.toCurrency()
                }
                product.price?.let {
                    tvMrp
                }
                Glide.with(imgProductImage.context)
                    .load(product.productUrl).transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(imgProductImage)

                val shapeAppearanceModel = imgProductImage.shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(40f)
                    .build()
                imgProductImage.shapeAppearanceModel = shapeAppearanceModel

                productParent.setOnClickListener {
                    product.id?.let { it1 -> onSectionsClicked(product.type, it1) }
                }
            }
        }
    }


    inner class BannerViewHolder(private val mBinding: AdapterBannerLayoutBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bindData(position: Int) {
            val bannerList = products[position].banners
            mBinding.rvBanners.layoutManager = LinearLayoutManager(mBinding.rvBanners.context, LinearLayoutManager.HORIZONTAL, false)
            mBinding.rvBanners.adapter = BannerAdapter(bannerList) {
                onSectionsClicked(ViewTypes.Banner, it)
            }
        }
    }
}