package com.example.my_shopping_list.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.parseIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.service.controls.templates.TemperatureControlTemplate.MODE_UNKNOWN
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.my_shopping_list.R
import com.example.my_shopping_list.databinding.ActivityShopItemBinding
import com.example.my_shopping_list.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {



    private var screenMode = UNDEFINED_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        setLaunchMode()
    }
    private fun setLaunchMode() {
        val fragment = when(screenMode) {
            ADD_MODE -> ShopItemFragment.newInstanceAddMode()
            EDIT_MODE -> ShopItemFragment.newInstanceEditMode(shopItemId)
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(SCREEN_MODE)
        if (mode != EDIT_MODE && mode != ADD_MODE) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == EDIT_MODE) {
            if (!intent.hasExtra(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        private const val SCREEN_MODE = "Screen_mode"
        private const val SHOP_ITEM_ID = "Shop_item_id"
        private const val ADD_MODE = "add"
        private const val EDIT_MODE = "edit"
        private const val UNDEFINED_MODE = ""

        fun AddItemIntent(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(SCREEN_MODE, ADD_MODE)
            return intent
        }
        fun EditItemIntent(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(SCREEN_MODE, EDIT_MODE)
            intent.putExtra(SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}