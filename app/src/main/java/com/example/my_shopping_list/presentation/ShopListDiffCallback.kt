package com.example.my_shopping_list.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.my_shopping_list.domain.ShopItem

class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}