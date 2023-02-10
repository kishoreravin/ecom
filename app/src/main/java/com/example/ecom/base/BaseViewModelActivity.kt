package com.example.ecom.base

import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ecom.R


abstract class BaseViewModelActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    protected abstract fun onInitViewModel()

    override fun onResume() {
        super.onResume()
        onInitViewModel()
    }

    fun setApiStateObservers(viewModel: BaseViewModel, progressBar: ProgressBar) {
        onRetry(viewModel)
        this.progressBar = progressBar
        viewModel.loaderLiveData.observe(this, ::showHorizontalProgressBar)
        viewModel.showNetworkError.observe(this, ::showNoNetworkView)
        viewModel.showWebApiError.observe(this, ::showWebApiErrorView)
    }

    private fun onRetry(viewModel: BaseViewModel) {
        findViewById<View>(R.id.btn_api_retry).setOnClickListener {
            viewModel.showWebApiError.value = false
            viewModel.onRetryClickListener()
        }
        findViewById<View>(R.id.btn_network_reload).setOnClickListener {
            viewModel.showNetworkError.value = false
            viewModel.onRetryClickListener()
        }
    }

    private fun showHorizontalProgressBar(isLoading: Boolean) {
        if (this::progressBar.isInitialized) {
            if (isLoading) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                progressBar.visibility = View.VISIBLE
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                progressBar.visibility = View.GONE
            }
        }
    }
    // error handlers

    abstract fun showNoNetworkView(isError: Boolean)
    abstract fun showWebApiErrorView(isError: Boolean)


    protected fun toolBarSetUp(toolbar: Toolbar?, title: String = "") {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = title
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_new)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}