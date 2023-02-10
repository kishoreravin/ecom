package com.example.ecom.pages.userProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecom.R
import com.example.ecom.base.BaseViewModelFragment
import com.example.ecom.databinding.FragmentUserProfilePageBinding
import com.example.ecom.models.UserModel
import com.example.ecom.pages.userProfile.viewmodel.UserProfileViewModel
import com.example.ecom.utils.CommonUtils
import com.example.ecom.utils.setVisibility
import com.example.ecom.utils.toColorListState
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfilePage : BaseViewModelFragment() {
    private lateinit var mBinding: FragmentUserProfilePageBinding
    private val mViewModel: UserProfileViewModel by viewModel()
    private val commonUtils: CommonUtils by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentUserProfilePageBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onInitViewModel() {
        setApiStateObservers(
            viewModel = mViewModel,
            mBinding.progressBar,
            view = mBinding.root
        )
        mViewModel.profileDataLiveData.observe(this, ::onUserDataAvailable)
        mViewModel.getUserDetails()
    }

    private fun onUserDataAvailable(userModel: UserModel?) {
        if (userModel != null) {
            with(mBinding) {

                val backgroundShapeModel: ShapeAppearanceModel = ShapeAppearanceModel.builder()
                    .setAllCornerSizes(40F)
                    .build()
                hbdLayout.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                    fillColor = commonUtils.getColor(R.color.colorAccent).toColorListState()
                }
                flIcon.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                    fillColor = commonUtils.getColor(R.color.colorOrangeWhite).toColorListState()
                }
                flWalletIcon.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                    fillColor = commonUtils.getColor(R.color.colorOrangeWhite).toColorListState()
                }
                clPremium.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                    fillColor = commonUtils.getColor(R.color.colorPrimary).toColorListState()
                    strokeColor = commonUtils.getColor(R.color.colorAccent).toColorListState()
                    strokeWidth = 2f
                }
                clWallet.background = MaterialShapeDrawable(backgroundShapeModel).apply {
                    fillColor = commonUtils.getColor(R.color.colorPrimary).toColorListState()
                    strokeColor = commonUtils.getColor(R.color.colorAccent).toColorListState()
                    strokeWidth = 2f
                }
            }
            mBinding.viewModel = mViewModel
            mBinding.hbdLayout.setVisibility(true)
        }
    }


    override fun showNoNetworkView(isError: Boolean) {
        mBinding.profileParent.setVisibility(!isError)
        mBinding.profileNetworkErrorView.setVisibility(isError)
    }

    override fun showWebApiErrorView(isError: Boolean) {
        mBinding.profileParent.setVisibility(!isError)
        mBinding.profileApiErrorView.setVisibility(isError)
    }
}