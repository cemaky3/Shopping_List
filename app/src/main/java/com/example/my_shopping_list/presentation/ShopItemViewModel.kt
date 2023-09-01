package com.example.my_shopping_list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_shopping_list.data.ShopListRepositoryImpl
import com.example.my_shopping_list.domain.AddItemUseCase
import com.example.my_shopping_list.domain.EditItemUseCase
import com.example.my_shopping_list.domain.GetItemByIdUseCase
import com.example.my_shopping_list.domain.ShopItem

class ShopItemViewModel : ViewModel() {
    private val shopListRepository = ShopListRepositoryImpl

    private val addItemUC = AddItemUseCase(shopListRepository)
    private val editItemUC = EditItemUseCase(shopListRepository)
    private val getItemByIdUC = GetItemByIdUseCase(shopListRepository)

    private val _errorInput = MutableLiveData<Boolean>()
    val errorInput: LiveData<Boolean>
        get() = _errorInput

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(id: Int) {
        _shopItem.value = getItemByIdUC.getItemById(id)
    }

    fun addItem(newName: String?, newCount: String?) {
        val name = parseName(newName)
        val count = parseCount(newCount)
        val isValid = isValidInput(name, count)
        if (isValid) {
            val item = ShopItem(name, count, enabled = true)
            addItemUC.addItem(item)
            finishWork()
        }
    }

    fun editItem(newName: String?, newCount: String?) {
        val name = parseName(newName)
        val count = parseCount(newCount)
        val isValid = isValidInput(name, count)
        if (isValid) {
            val item = _shopItem
            item.value?.let {
                val newItem = it.copy(name = name, count = count)
                editItemUC.editItem(newItem)
                finishWork()
            }
        }
    }

    fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    fun parseCount(count: String?): Int {
        return count?.trim()?.toIntOrNull() ?: 0
    }

    private fun isValidInput(name: String, count: Int): Boolean {
        // TODO: show error message
        if (name.isNotBlank() && count > 0) {
            _errorInput.value = false
            return true
        } else {
            _errorInput.value = true
            return false
        }
    }

    fun resetErrorInput() {
        _errorInput.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}
