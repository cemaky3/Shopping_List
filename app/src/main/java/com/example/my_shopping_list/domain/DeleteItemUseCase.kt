package com.example.my_shopping_list.domain

class DeleteItemUseCase(private val repository: ShopListRepository) {
    fun deleteItem(item: ShopItem) {
        repository.deleteItem(item)
    }
}