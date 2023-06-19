package com.example.my_shopping_list.domain

interface ShopListRepository {

    fun addItem(item: ShopItem)

    fun deleteItem(item: ShopItem)

    fun editItem(item: ShopItem)

    fun getItemById(id: Int): ShopItem

    fun getShoppingList(): List<ShopItem>
}