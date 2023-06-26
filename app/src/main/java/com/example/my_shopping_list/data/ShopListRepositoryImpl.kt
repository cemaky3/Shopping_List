package com.example.my_shopping_list.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.my_shopping_list.domain.ShopItem
import com.example.my_shopping_list.domain.ShopListRepository
import java.util.Random

object ShopListRepositoryImpl : ShopListRepository {
    private val shopList = mutableListOf<ShopItem>()
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private var autoIncrementId = 0

    init {
        for (i in 1..120) {
            val item = ShopItem("Name $i", i, kotlin.random.Random.nextBoolean())
            addItem(item)
        }
    }
    override fun addItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editItem(shopItem: ShopItem) {
        val oldElement = getItemById(shopItem.id)
        shopList.remove(oldElement)
        addItem(shopItem)
    }

    override fun getItemById(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    override fun getShoppingList(): LiveData<List<ShopItem>> {
        return shopListLD
    }
    private fun updateList() {
        shopListLD.value = shopList
    }
}