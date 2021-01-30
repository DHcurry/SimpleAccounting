package io.github.skywalkerdarren.simpleaccounting.view_model

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.OnlineRepository
import java.util.*

class AccountEditViewModel(private val onlineRepo:OnlineRepository) : ViewModel(){
    val billInfos = MutableLiveData<List<BillInfo>>()

    fun submitChange(uuid: UUID, balance: String){
        return onlineRepo.updateAccountBalance(uuid,balance)
    }

    fun delAccount(uuid: UUID) {
        return onlineRepo.delAccount(uuid)
    }

    // 点击的account
    lateinit var account: Account
    // 该账户的账单信息

    fun getBills(accountId:UUID){
        onlineRepo.getBillsByAccount(accountId){
            billInfos.value = it
        }
//        Log.d("AccountEditViewModel","getBillsByAccount"+billInfos.value)
    }

    fun getBillLiveData():LiveData<List<BillInfo>>{
        return billInfos;
    }

}