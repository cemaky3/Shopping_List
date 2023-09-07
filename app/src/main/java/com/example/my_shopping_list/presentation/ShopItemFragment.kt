package com.example.my_shopping_list.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.my_shopping_list.R
import com.example.my_shopping_list.databinding.FragmentShopItemBinding
import com.example.my_shopping_list.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {
    private lateinit var viewModel: ShopItemViewModel

    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private var screenMode = UNDEFINED_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        addTextChangeListeners()
        setLaunchMode()
        observeViewModel()
    }
    private fun observeViewModel() {
        viewModel.errorInput.observe(viewLifecycleOwner) {
            if (it) {
                tilName.error = getString(R.string.error_input) + "\n" + getString(
                    R.string.name_error_hint
                )
                tilCount.error = getString(R.string.error_input) + "\n" + getString(
                    R.string.count_error_hint
                )
            } else {
                null
            }
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            requireActivity().finish()
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
        viewModel.shopItem.observe(viewLifecycleOwner) {
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
    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != EDIT_MODE && mode != ADD_MODE) {
            throw RuntimeException("Param screen mode is absent")
        }
        screenMode = mode
        if (screenMode == EDIT_MODE) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SCREEN_MODE = "Screen_mode"
        private const val SHOP_ITEM_ID = "Shop_item_id"
        private const val ADD_MODE = "add"
        private const val EDIT_MODE = "edit"
        private const val UNDEFINED_MODE = ""

        fun newInstanceAddMode(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, ADD_MODE)
                }
            }
        }
        fun newInstanceEditMode(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, EDIT_MODE)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}
