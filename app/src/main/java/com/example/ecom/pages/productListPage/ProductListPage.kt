package com.example.ecom.pages.productListPage

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecom.R
import com.example.ecom.base.BaseViewModelFragment
import com.example.ecom.databinding.FragmentProductListPageBinding
import com.example.ecom.models.BannerModel
import com.example.ecom.models.BrandModel
import com.example.ecom.models.ProductModel
import com.example.ecom.models.ViewTypes
import com.example.ecom.pages.adapters.BrandAdapter
import com.example.ecom.pages.adapters.ProductListPageAdapter
import com.example.ecom.pages.productDetailsPage.ProductDetailsPage
import com.example.ecom.pages.productListPage.viewmodel.ProductListViewModel
import com.example.ecom.utils.setVisibility
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListPage : BaseViewModelFragment() {

    private lateinit var mBinding: FragmentProductListPageBinding
    private val mViewModel: ProductListViewModel by viewModel()
    private val adapter: ProductListPageAdapter by lazy {
        ProductListPageAdapter(mutableListOf(), ::onSectionsClicked)
    }
    var doResetList = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentProductListPageBinding.inflate(inflater, container, false)
        mViewModel.productListLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                onProductsAvailable(it)
            }
        }
        mViewModel.bannerListLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                onBannersAvailable(it)
            }
        }
        mViewModel.brandListLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                onBrandsAvailable(it)
            }
        }
        mViewModel.searchResult.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                onSearchResultAvailable(it)
            }
        }
        setAdapters()
        return mBinding.root
    }

    private fun onSearchResultAvailable(productModels: List<ProductModel>) {
        productModels.forEach { it.type = ViewTypes.Product }
        adapter.addProducts(productModels, true)
    }

    override fun onInitViewModel() {
        setApiStateObservers(
            viewModel = mViewModel,
            mBinding.progressBar,
            view = mBinding.root
        )
        doResetList = true
        mViewModel.getProductList()
        mBinding.svSearch.setOnQueryTextListener(mViewModel.onQueryTextListener())
    }

    private fun setAdapters() {
        val layoutManager = GridLayoutManager(activity, 2)
        mBinding.plpRecyclerView.layoutManager = layoutManager
        mBinding.plpRecyclerView.adapter = adapter
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter.getItemViewType(position) == 1) {
                    return 2
                }
                return 1
            }
        }
        mBinding.plpRecyclerView.isNestedScrollingEnabled = false
        mBinding.plpRecyclerView.addOnScrollListener(mViewModel.onPageScroll(layoutManager))
    }


    private fun onBannersAvailable(bannerModels: List<BannerModel>) {
        adapter.addModels(ProductModel().apply {
            type = ViewTypes.Banner
            banners = bannerModels
        }, 6)
    }

    private fun onBrandsAvailable(brandModels: List<BrandModel>?) {
        if (!brandModels.isNullOrEmpty()) {
            mBinding.clBrand.setVisibility(true)
            mBinding.rvBrand.layoutManager = LinearLayoutManager(mBinding.rvBrand.context, LinearLayoutManager.HORIZONTAL, false)
            mBinding.rvBrand.adapter = BrandAdapter(brandModels)
        } else {
            mBinding.clBrand.setVisibility(false)
        }
    }

    private fun onProductsAvailable(productModels: List<ProductModel>) {
        productModels.forEach { it.type = ViewTypes.Product }
        adapter.addProducts(productModels, doResetList)
        doResetList = false
    }

    private fun onSectionsClicked(viewTypes: ViewTypes, data: String) {
        if (viewTypes == ViewTypes.Product) {
            startActivity(Intent(activity, ProductDetailsPage::class.java).apply { putExtra("ProductCode", data) })
        } else if (viewTypes == ViewTypes.Banner && data == "membership") {
            showSnackBar(getString(R.string.text_premium_message))
        }
    }

    override fun showNoNetworkView(isError: Boolean) {
        mBinding.plpParent.setVisibility(!isError)
        mBinding.plpNetworkErrorView.setVisibility(isError)
    }

    override fun showWebApiErrorView(isError: Boolean) {
        mBinding.plpParent.setVisibility(!isError)
        mBinding.plpApiErrorView.setVisibility(isError)
    }

    private fun showSnackBar(message: String) {
        if (!TextUtils.isEmpty(message)) {
            val snackbar = Snackbar.make(mBinding.parentView, message, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(mBinding.parentView.context, R.color.colorAccent))
            snackbar.setActionTextColor(ContextCompat.getColor(mBinding.parentView.context, R.color.colorPrimary))
            val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.maxLines = 2
            snackbar.show()
        }
    }
}