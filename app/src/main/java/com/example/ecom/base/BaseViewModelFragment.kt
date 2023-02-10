package com.example.ecom.base

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.ecom.R
import com.example.ecom.utils.setVisibility

abstract class BaseViewModelFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    protected abstract fun onInitViewModel()

    override fun onResume() {
        super.onResume()
        onInitViewModel()
    }

    fun setApiStateObservers(
        viewModel: BaseViewModel, progressBar: ProgressBar,
        view: View
    ) {
        onRetry(viewModel, view)
        this.progressBar = progressBar
        viewModel.loaderLiveData.observe(this, ::showHorizontalProgressBar)
        viewModel.showNetworkError.observe(this, ::showNoNetworkView)
        viewModel.showWebApiError.observe(this, ::showWebApiErrorView)
    }

    private fun onRetry(viewModel: BaseViewModel, view: View) {
        view.findViewById<View>(R.id.btn_api_retry).setOnClickListener {
            viewModel.showWebApiError.value = false
            viewModel.onRetryClickListener()
        }
        view.findViewById<View>(R.id.btn_network_reload).setOnClickListener {
            viewModel.onRetryClickListener()
            viewModel.showNetworkError.value = false
        }
    }

    private fun showHorizontalProgressBar(isLoading: Boolean) {
        if (this::progressBar.isInitialized) {
            progressBar.setVisibility(isLoading)
        }
    }

    // error handlers
    abstract fun showNoNetworkView(isError: Boolean)
    abstract fun showWebApiErrorView(isError: Boolean)

}