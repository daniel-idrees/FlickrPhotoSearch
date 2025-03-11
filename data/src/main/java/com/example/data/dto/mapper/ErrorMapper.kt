package com.example.data.dto.mapper

/**
 * https://www.flickr.com/services/api/flickr.photos.search.html
 */
internal fun translateErrorCodeMessage(errorCode: Int?): String {
    return when (errorCode) {
        1 -> "Too many tags. Please specify fewer than 20 tags."
        2 -> "User not found. Please check the user ID and try again."
        3 -> "Invalid search. Use 'Get Recent Photos' instead."
        4 -> "You don’t have permission to view this content."
        5 -> "User not found. Please check the user ID and try again."
        10 -> "Search is temporarily unavailable. Please try again later."
        11 -> "Invalid search parameters. Please check your input and try again."
        12 -> "Too many machine tags. Please reduce the number and try again."
        17 -> "You can only search within your own contacts."
        18 -> "Invalid search parameters. Please check your input and try again."
        100 -> "Operation failed. Please contact support."
        105 -> "Service is temporarily unavailable. Please try again later."
        106 -> "Operation failed. Please try again later."
        111 -> "Operation failed. Please contact support."
        112 -> "Operation failed. Please contact support."
        114 -> "Operation failed. Please contact support."
        115 -> "Operation failed. Please contact support."
        116 -> "Operation failed. Please contact support."
        else -> "Something went wrong. Please try again later."
    }
}