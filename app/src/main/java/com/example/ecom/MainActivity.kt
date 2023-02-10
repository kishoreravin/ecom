package com.example.ecom

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ecom.databinding.ActivityMainBinding
import com.example.ecom.pages.productListPage.ProductListPage
import com.example.ecom.pages.userProfile.UserProfilePage
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private lateinit var navigationItemSelectedListener: NavigationBarView.OnItemSelectedListener
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setBottomNavClickListeners()
        loadFragment(ProductListPage())
    }

    private fun setBottomNavClickListeners() {
        onBackPressedDispatcher.addCallback(this) {
            if (ProductListPage::class.java.name == if (getActiveFragment() != null) getActiveFragment()!!.javaClass.name else Fragment()) {
                finish()
            } else {
                loadFragment(ProductListPage())
            }
        }
        mBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_bar_item_plp -> loadFragment(ProductListPage())
                R.id.nav_bar_item_profile -> loadFragment(UserProfilePage())
                else -> loadFragment(ProductListPage())
            }
            return@setOnItemSelectedListener true
        }
        mBinding.bottomNavigation.itemIconTintList = null
    }

    private fun loadFragment(fragment: Fragment) {
        if (!isSimilarFragment(fragment)) {
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            ft.replace(R.id.container, fragment, fragment.javaClass.name)
            ft.addToBackStack(fragment.javaClass.name)
            if (!isFinishing) {
                ft.commitAllowingStateLoss()
            }
        }
    }

    private fun isSimilarFragment(fragment: Fragment): Boolean {
        return getActiveFragment() != null && (fragment.javaClass.name == getActiveFragment()?.javaClass?.name)
    }

    private fun getActiveFragment(): Fragment? {
        if (supportFragmentManager.backStackEntryCount == 0) {
            return null
        }
        val tag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
        return supportFragmentManager.findFragmentByTag(tag)
    }
}