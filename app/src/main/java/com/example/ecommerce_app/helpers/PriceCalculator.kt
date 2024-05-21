package com.example.ecommerce_app.helpers

fun Float?.getProductPrice(price: Float): Float{
    // --> Percentage
    if (this == null)
        return price
    val remainingPricePercentage = 1f - this
    val priceAfterOffer = remainingPricePercentage * price

    return price
}