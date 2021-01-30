package io.github.skywalkerdarren.simpleaccounting.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bumptech.glide.util.Util
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account
import io.github.skywalkerdarren.simpleaccounting.model.repository.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.repository.OnlineRepository
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil
import org.joda.time.DateTime
import java.math.BigDecimal
import java.util.*

/**
 * 账户页vm
 *
 * @author darren
 * @date 2018/4/4
 */
class AccountViewModel(private val onlineRepo : OnlineRepository) : ViewModel() {
    var num = 0;
    /**
     * @return 净资产
     */
    val nav = MutableLiveData<String>()
    /**
     * @return 负债
     */
    val liability = MutableLiveData<String>()
    /**
     * @return 总资产
     */
    val totalAssets = MutableLiveData<String>()
    private val accounts = MutableLiveData<List<Account>>()
    /**
     * @return 账户数目
     */
    val accountSize = Transformations.map(accounts) { input: List<Account> -> input.size.toString() }

    /**
     * 账户插入是否成功
     */
    var res = "loading"

    fun start() {
        onlineRepo.getAccounts { accounts: List<Account>? ->
            this@AccountViewModel.accounts.value = accounts
            var res = BigDecimal.ZERO;
            if (accounts != null){
                for(a in accounts){
                    res = res.add(a.balance);
                    Log.d("AccountViewModel", "res: "+res+"balance="+a.balance)
                }
            }
            this@AccountViewModel.nav.value = res.toEngineeringString();
        }
        onlineRepo.getBillStats(DateTime(0), DateTime.now()) { billStats ->
//            nav.value = FormatUtil.getNumeric(billStats?.sum)
            liability.value = FormatUtil.getNumeric(billStats?.expense)
            totalAssets.value = FormatUtil.getNumeric(billStats?.income)
        }
    }

    fun getAccount(id: UUID, callBack:(account:Account?) -> Unit){
        onlineRepo.getAccount(id,callBack)
    }

    fun changePosition(a: Account, b: Account) {
        onlineRepo.changePosition(a, b)
    }

    fun getAccounts(): LiveData<List<Account>> {
        return accounts
    }

    fun addAccount(name: String, hint: String, balance: String) {
        onlineRepo.addAccount(name,hint,balance) { t -> this.res = t };
    }

}