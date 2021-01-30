package io.github.skywalkerdarren.simpleaccounting.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.diff.BillInfoDiff
import io.github.skywalkerdarren.simpleaccounting.base.BaseDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.base.BaseMultiItemDataBindingAdapter
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListAccountBillBinding
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo

class AccountBillsAdapter:
        BaseDataBindingAdapter<BillInfo, ItemListAccountBillBinding>(R.layout.item_list_account_bill,null) {

    fun setNewList(data: List<BillInfo>?) {
//        Log.d("AccountBillAdapter", "set new list"+data)
        setNewDiffData(BillInfoDiff(data))
    }

    override fun convert(binding: ItemListAccountBillBinding, item: BillInfo) {
//        Log.d("AccountBillAdapter","convert item"+item)
        binding.billInfo = item
    }


}