package com.example.my_shopping_list.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addItem(item: ShopItem)

    fun deleteItem(item: ShopItem)

    fun editItem(item: ShopItem)

    fun getItemById(id: Int): ShopItem

    fun getShoppingList(): LiveData <List<ShopItem>>
}