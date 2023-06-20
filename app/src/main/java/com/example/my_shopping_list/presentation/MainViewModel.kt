package com.example.my_shopping_list.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_shopping_list.data.ShopListRepositoryImpl
import com.example.my_shopping_list.data.ShopListRepositoryImpl.getShoppingList
import com.example.my_shopping_list.domain.AddItemUseCase
import com.example.my_shopping_list.domain.DeleteItemUseCase
import com.example.my_shopping_list.domain.EditItemUseCase
import com.example.my_shopping_list.domain.GetShoppingListUseCase
import com.example.my_shopping_list.domain.ShopItem

class MainViewModel : ViewModel()  {

    private val shopListRepository = ShopListRepositoryImpl

    private val addItemUC = AddItemUseCase(shopListRepository)
    private val deleteItemUC = DeleteItemUseCase(shopListRepository)
    private val editItemUC = EditItemUseCase(shopListRepository)
    private val getShoppingListUC = GetShoppingListUseCase(shopListRepository)

    val shopList = getShoppingListUC.getShoppingList()

    fun deleteItem(item: ShopItem) {
        deleteItemUC.deleteItem(item)
    }
    fun editItem(item: ShopItem) {
        val newItem = item.copy(enabled = !item.enabled)
        editItemUC.editItem(newItem)
    }
}