package com.boocha.feature.search.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boocha.R
import com.boocha.base.BaseActivity
import com.boocha.feature.search.BookItemClickEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchActivity : BaseActivity() {

    private val openedFrom: String by lazy { intent.getStringExtra(EXTRA_OPENED_FROM) }

    companion object {

        const val OPENED_FROM_HOME_FRAGMENT = "opened_from_home_fragment"
        const val OPENED_FROM_ADD_BOOK_FOR_SWAP_FRAGMENT = "opened_from_add_book_for_swap_fragmnet"

        const val EXTRA_BOOK_ITEM = "extra_book_item"
        const val EXTRA_OPENED_FROM = "extra_opened_from"

        fun newIntent(context: Context, openedFrom: String): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(EXTRA_OPENED_FROM, openedFrom)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setContentView(R.layout.activity_search)
        navigateToSearchFragment()
    }

    override fun onStart() {
        super.onStart()
        registerToEvent()
    }

    override fun onStop() {
        super.onStop()
        unregisterToEvent()
    }

    private fun navigateToSearchFragment() {
        replaceFragment(R.id.fragmentHolder, SearchFragment.newInstance(), null, false)
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBookItemClickEvent(event: BookItemClickEvent) {
        if (openedFrom == OPENED_FROM_ADD_BOOK_FOR_SWAP_FRAGMENT) {
            val intent = Intent()
            intent.putExtra(EXTRA_BOOK_ITEM, event.item)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}