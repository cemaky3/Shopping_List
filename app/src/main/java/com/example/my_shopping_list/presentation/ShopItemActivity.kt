package com.example.my_shopping_list.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private lateinit var binding: ActivityShopItemBinding
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private var screenMode = UNDEFINED_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        initViews()
        parseIntent()
        addTextChangeListeners()
        setLaunchMode()
        viewModel.errorInput.observe(this) {
            if(it) {
                tilName.error = getString(R.string.error_input)+"\n"+getString(R.string.name_error_hint)
                tilCount.error = getString(R.string.error_input)+ "\n" +getString(R.string.count_error_hint)
            } else {
                null
            }
        }
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }
    private fun setLaunchMode() {
        when(screenMode) {
            ADD_MODE -> launchAddMode()
            EDIT_MODE -> launchEditMode()
        }
    }
    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInput()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInput()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editItem(etName.text.toString(), etCount.text.toString())
        }
    }
    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addItem(etName.text.toString(), etCount.text.toString())
        }
    }

    private fun initViews() {
        tilName = binding.name
        tilCount = binding.count
        etName = binding.etName
        etCount = binding.etCount
        buttonSave = binding.saveBtn
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