package com.boocha.model

import com.boocha.util.BOOK_STATUS_GOOD
import com.boocha.util.SWAP_STATUS_ACTIVE

data class Swap(
        var id: String? = "",
        var date: String? = "",
        var book: Book? = null,
        var bookStatus: Int? = BOOK_STATUS_GOOD,
        var owner: User? = null,
        var ownerDescription: String? = "",
        var imageUri: String? = "",
        var swapStatus: Int = SWAP_STATUS_ACTIVE
)