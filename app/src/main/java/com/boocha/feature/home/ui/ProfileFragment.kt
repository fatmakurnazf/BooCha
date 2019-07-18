package com.boocha.feature.home.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.boocha.R
import com.boocha.base.BaseFragment
import com.boocha.data.remote.util.Status
import com.boocha.feature.home.adapter.ProfileFragmentAdapter
import com.boocha.feature.home.viewmodel.ProfileFragmentViewModel
import com.boocha.feature.login.ui.LoginActivity
import com.boocha.model.Swap
import com.boocha.model.User
import com.boocha.util.SWAP_STATUS_ACTIVE
import com.boocha.util.SWAP_STATUS_SWAPPED
import com.boocha.util.WriteObjectFile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_profile2.*

class ProfileFragment : BaseFragment() {

    lateinit var viewModel: ProfileFragmentViewModel
    lateinit var swapsAdapter: ProfileFragmentAdapter
    lateinit var writeObjectFile: WriteObjectFile


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
        context?.let {
            writeObjectFile = WriteObjectFile(it)
        }

        initOnClickListener()
        initRecyclerView()
        initUserLiveData()
        initSwapsLiveDate()

        viewModel.getUser(id)
        viewModel.getUserSwaps(id)
    }

    private fun initOnClickListener() {
        btnSignOut.setOnClickListener {
            context?.let { context ->
                AlertDialog.Builder(context)
                        .setTitle(getString(R.string.sign_out))
                        .setMessage(getString(R.string.are_you_sure_you_want_to_sign_out))
                        .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                            dialog.dismiss()
                            viewModel.signOut()
                            startActivity(LoginActivity.newIntent(context))
                            activity?.finish()
                        }
                        .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
                        .show()

            }
            viewModel.signOut()
        }
    }

    private fun initRecyclerView() {
        swapsAdapter = ProfileFragmentAdapter()

        rvSwapList.apply {
            setHasFixedSize(true)
            adapter = swapsAdapter
        }
    }

    private fun initUserLiveData() {
        viewModel.userLiveData.observe(this, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    dismissLoadingDialog()
                    updateUiWithUser(resource.data)

                    resource.data?.let { user ->
                        writeObjectFile.deleteObject(WriteObjectFile.FILE_USER)
                    }
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

    private fun initSwapsLiveDate() {
        viewModel.swapsLiveData.observe(this, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let {
                        swapsAdapter.swapList = it
                        updateUiWithSwaps(it)
                        swapsAdapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {
                    showErrorDialog(resource.message)
                }
                Status.LOADING -> {
                }
            }
        })

    }

    private fun updateUiWithUser(user: User?) {
        tvUsername.text = "${user?.name} ${user?.surname}"

        if (user?.profilePhoto.isNullOrEmpty().not()) {
            Glide.with(this)
                    .load(user?.profilePhoto)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(ivProfilePhoto)
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_add_photo)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(ivProfilePhoto)
        }
    }

    private fun updateUiWithSwaps(swaps: MutableList<Swap>) {
        tvSwappableCount.text = (swaps.filter { it.swapStatus == SWAP_STATUS_ACTIVE }.size).toString()
        tvSwappedCount.text = (swaps.filter { it.swapStatus == SWAP_STATUS_SWAPPED }.size).toString()
    }
}