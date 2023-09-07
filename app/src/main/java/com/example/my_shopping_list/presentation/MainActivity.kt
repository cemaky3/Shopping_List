package com.example.my_shopping_list.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.my_shopping_list.R
import com.example.my_shopping_list.databinding.ActivityMainBinding
import com.example.my_shopping_list.domain.ShopItem


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopList: MutableList<ShopItem>
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        adapterSetup()
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it.toMutableList())
        }
        binding.buttonAddShopItem.setOnClickListener {
            val intent = ShopItemActivity.AddItemIntent(this)
            startActivity(intent)
        }
    }

    private fun adapterSetup() {
        val shopListRV = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        shopListRV.adapter = shopListAdapter
        shopListRV.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_DISABLED,
            ShopListAdapter.VIEW_POOL_MAX_SIZE
        )
        shopListRV.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_ENABLED,
            ShopListAdapter.VIEW_POOL_MAX_SIZE
        )
        setLongClickListener()
        setClickListener()
        setSwipeListener(shopListRV)
    }
    private fun setSwipeListener(shopListRV: RecyclerView?) {
        val callBack = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteItem(item)
            }
        }
        ItemTouchHelper(callBack).attachToRecyclerView(shopListRV)
    }

    private fun setClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("MainActivity", it.toString())
            val intent = ShopItemActivity.EditItemIntent(this, it.id)
            startActivity(intent)
        }
    }

    private fun setLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.editItem(it)
        }
    }
}
