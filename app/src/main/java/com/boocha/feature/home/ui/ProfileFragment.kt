package com.boocha.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.boocha.R
import com.boocha.base.BaseFragment
import com.boocha.data.remote.util.Status
import com.boocha.feature.home.viewmodel.ProfileFragmentViewModel
import com.boocha.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_profile2.*

class ProfileFragment : BaseFragment() {

    lateinit var viewModel: ProfileFragmentViewModel

    val id: String by lazy { arguments?.getString(BUNDLE_ID) as String }

    companion object {

        const val TAG = "profile_fragment"

        private const val BUNDLE_ID = "id"

        fun newInstance(id: String? = ""): ProfileFragment {
            val profileFragment = ProfileFragment()
            val bundle = Bundle()

            bundle.putString(BUNDLE_ID, id)
            profileFragment.arguments = bundle

            return profileFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel::class.java)

        initUserLiveData()

        viewModel.getUser(id)
    }

    private fun initUserLiveData() {
        viewModel.userLiveData.observe(this, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    dismissLoadingDialog()
                    updateUiWithUser(resource.data)
                }
                Status.ERROR -> {
                    dismissLoadingDialog()
                }
                Status.LOADING -> {
                    showLoadingDialog()
                }
            }
        })
    }

    private fun updateUiWithUser(user: User?) {
        tvUsername.text = "${user?.name} ${user?.surname}"

        Glide.with(this)
                .load(user?.profilePhoto)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(ivProfilePhoto)
    }
}