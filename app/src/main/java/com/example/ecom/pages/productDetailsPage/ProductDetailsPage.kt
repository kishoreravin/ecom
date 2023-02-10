package com.example.ecom.pages.productDetailsPage

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ecom.R
import com.example.ecom.base.BaseViewModelActivity
import com.example.ecom.databinding.ActivityProductDetailsBinding
import com.example.ecom.models.ProductModel
import com.example.ecom.models.ViewTypes
import com.example.ecom.pages.adapters.ProductListPageAdapter
import com.example.ecom.pages.adapters.VariantsAdapter
import com.example.ecom.pages.productDetailsPage.viewmodel.ProductDetailsViewModel
import com.example.ecom.utils.setVisibility
import com.example.ecom.utils.strikeThrough
import com.example.ecom.utils.toColorListState
import com.example.ecom.utils.toCurrency
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsPage : BaseViewModelActivity() {

    private lateinit var mBinding: ActivityProductDetailsBinding
    private val mViewModel: ProductDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        toolBarSetUp(mBinding.toolbar, "Product Details")
    }

    override fun onInitViewModel() {
        setApiStateObservers(viewModel = mViewModel, mBinding.progressBar)
        mViewModel.productDetailsLiveData.observe(this, ::onProductDetailsAvailable)
        mViewModel.similarProductsLiveData.observe(this, ::onSimilarProductsAvailable)
        mViewModel.variantLiveData.observe(this, ::onVariantsDataAvailable)
        checkIntent()
    }

    private fun onVariantsDataAvailable(productModels: List<ProductModel>?) {
        if (!productModels.isNullOrEmpty()) {
            mBinding.clVariants.setVisibility(true)
            mBinding.rvVariants.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            mBinding.rvVariants.adapter = VariantsAdapter(productModels.toMutableList(), mViewModel.productCode) { data: String ->
                mViewModel.getProductDetails(data)
            }
        } else {
            mBinding.clSimilarProducts.setVisibility(false)
        }
    }

    private fun onSimilarProductsAvailable(productModels: List<ProductModel>?) {
        if (!productModels.isNullOrEmpty()) {
            productModels.forEach { it.type = ViewTypes.Product }
            mBinding.clSimilarProducts.setVisibility(true)
            mBinding.rvSimilarProducts.layoutManager = GridLayoutManager(this, 2)
            mBinding.rvSimilarProducts.adapter = ProductListPageAdapter(productModels.toMutableList()) { _, data: String ->
                mViewModel.getProductDetails(data)
            }
        } else {
            mBinding.clSimilarProducts.setVisibility(false)
        }
    }

    private fun onProductDetailsAvailable(product: ProductModel) {
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
                        text = product.price.toCurrency()
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
                .load(product.productUrl)
                .into(imgProductImage)

            product.productDesc?.let {
                tvProductDetails.text = it
            }
            product.name?.let {
                tvProductNameForSimilar.text = it
            }
            val backgroundShapeModel: ShapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(40F)
                .build()
            addToCart.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                fillColor = getColorStateList(R.color.colorBlack)
            }
            remove.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                fillColor = getColorStateList(R.color.colorWhiteGrey)
            }
            goToBag.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                fillColor = getColorStateList(R.color.colorPrimary)
                strokeWidth = 2f
                strokeColor = getColorStateList(R.color.colorBlack)
            }

            val radius = 40f
            val shapeAppearanceModelLike = imgBtnLike.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(radius)
                .build()
            imgBtnLike.shapeAppearanceModel = shapeAppearanceModelLike
            val shapeAppearanceModelShare = imgBtnShare.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(radius)
                .build()
            imgBtnShare.shapeAppearanceModel = shapeAppearanceModelShare

            addToCart.setOnClickListener {
                addToCart.setVisibility(false)
                imgBtnLike.setVisibility(false)
                remove.setVisibility(true)
                goToBag.setVisibility(true)
            }

            remove.setOnClickListener {
                addToCart.setVisibility(true)
                imgBtnLike.setVisibility(true)
                remove.setVisibility(false)
                goToBag.setVisibility(false)
            }

            imgBtnShare.setOnClickListener { shareProduct(product) }
            imgBtnLike.setOnClickListener { }

        }
    }

    private fun shareProduct(productDetails: ProductModel) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, productDetails.name)
        intent.putExtra(
            Intent.EXTRA_TEXT, """
              ${productDetails.brand}
              ${productDetails.productDesc}
              ${productDetails.offerPrice}
              """.trimIndent()
        )
        startActivity(intent)
    }

    private fun checkIntent() {
        if (intent.hasExtra("ProductCode") && intent.getStringExtra("ProductCode") != null) {
            mViewModel.getProductDetails(intent.getStringExtra("ProductCode")!!)
        } else {
            showWebApiErrorView(true)
        }
    }


    override fun showNoNetworkView(isError: Boolean) {
        mBinding.pdpParent.setVisibility(!isError)
        mBinding.pdpNetworkErrorView.setVisibility(isError)
    }

    override fun showWebApiErrorView(isError: Boolean) {
        mBinding.pdpParent.setVisibility(!isError)
        mBinding.pdpApiErrorView.setVisibility(isError)
    }
}