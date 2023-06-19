package com.example.my_shopping_list.domain

class GetItemByIdUseCase(private val repository: ShopListRepository) {
    fun getItemById(id: Int): ShopItem {
       return repository.getItemById(id)
    }
}