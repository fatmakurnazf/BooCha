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
import com.boocha.feature.home.adapter.SwapListAdapter
import com.boocha.feature.home.viewmodel.HomeFragmentViewModel
import com.boocha.feature.search.ui.SearchActivity
import kotlinx.android.synthetic.main.home_fragment_2.*


class HomeFragment : BaseFragment() {

    lateinit var viewModel: HomeFragmentViewModel
    lateinit var swapListAdapter: SwapListAdapter

    companion object {

        const val TAG = "home_fragment"

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)

        initOnClickListener()
        initSwapListLiveData()
        prepareSwapList()

        viewModel.getSwapList()
    }

    private fun initOnClickListener() {
        tvSearch.setOnClickListener {
            context?.let {
                startActivity(SearchActivity.newIntent(it, SearchActivity.OPENED_FROM_HOME_FRAGMENT))
            }
        }
    }

    private fun initSwapListLiveData() {
        viewModel.swapListLiveData.observe(this, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    if (resource.data != null) {
                        swapListAdapter.swapList = resource.data
                        swapListAdapter.notifyDataSetChanged()
                        dismissLoadingDialog()
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

    private fun prepareSwapList() {
        swapListAdapter = SwapListAdapter()

        rvSwapList.apply {
            setHasFixedSize(true)
            adapter = swapListAdapter
        }
    }
}