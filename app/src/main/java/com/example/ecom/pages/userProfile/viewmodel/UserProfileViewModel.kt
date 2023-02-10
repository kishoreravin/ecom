package com.example.ecom.pages.userProfile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ecom.base.BaseViewModel
import com.example.ecom.models.UserModel
import com.example.ecom.models.base.ApiState
import com.example.ecom.network.ApiManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UserProfileViewModel(private val apiManager: ApiManager) : BaseViewModel() {

    val profileDataLiveData = MutableLiveData<UserModel?>()
    lateinit var profileData: UserModel

    fun getUserDetails() {
        viewModelScope.launch {
            apiManager.getUserProfile()
                .onStart { showLoader() }
                .onCompletion { dismissLoader() }
                .collectLatest {
                    when (it) {
                        is ApiState.Error -> showApiError()
                        is ApiState.Failed -> showApiError()
                        ApiState.NoNetwork -> showNetworkError()
                        is ApiState.Success -> {
                            profileDataLiveData.value = it.result.result
                            profileData = it.result.result
                        }
                    }
                }
        }
    }

    override fun onRetryClickListener() {
        getUserDetails()
    }

    fun userName() = if (this::profileData.isInitialized) "Hello, ${profileData.lastName}, ${profileData.firstname}" else "Hello, User"
    fun pointsEarned() = "Points Earned : ${if (this::profileData.isInitialized) profileData.pointsEarned else 0}"
    fun walletBalance() = "Balance : ${if (this::profileData.isInitialized) profileData.walletBalance else 0}"
    fun address() = if (this::profileData.isInitialized) profileData.address else ""
}