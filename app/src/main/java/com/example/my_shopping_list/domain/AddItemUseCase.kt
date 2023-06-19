package com.example.my_shopping_list.domain

class AddItemUseCase(private val repository: ShopListRepository) {
    fun addItem(item: ShopItem) {
        repository.addItem(item)
    }
}