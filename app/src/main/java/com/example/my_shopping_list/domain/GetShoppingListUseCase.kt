package com.example.my_shopping_list.domain

class GetShoppingListUseCase(private val repository: ShopListRepository) {
    fun getShoppingList(): List<ShopItem> {
        return repository.getShoppingList()
    }
}