package com.example.my_shopping_list.domain

class EditItemUseCase(private val repository: ShopListRepository) {
    fun editItem(item: ShopItem) {
        repository.editItem(item)
    }
}