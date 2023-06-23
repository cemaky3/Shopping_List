package com.example.my_shopping_list.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.my_shopping_list.R
import com.example.my_shopping_list.domain.ShopItem

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopList: MutableList<ShopItem>
    private lateinit var shopListAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        adapterSetup()
        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it.toMutableList()
        }
    }

    private fun adapterSetup() {
        val shopListRV = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        shopListRV.adapter = shopListAdapter
    }
}