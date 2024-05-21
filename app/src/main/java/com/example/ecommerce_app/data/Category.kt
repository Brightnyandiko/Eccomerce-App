package com.example.ecommerce_app.data

sealed class Category(val category: String) {

    object Chair: Category("Chair")
    object Cupboard: Category("Cupboard")
    object Table: Category("Table")
    object Accessories: Category("Accessories")
    object Furniture: Category("Furniture")
}