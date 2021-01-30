package io.github.skywalkerdarren.simpleaccounting.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo
import io.github.skywalkerdarren.simpleaccounting.model.repository.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.repository.OnlineRepository
import io.github.skywalkerdarren.simpleaccounting.ui.activity.StatsActivity
import io.github.skywalkerdarren.simpleaccounting.util.view.FormatUtil
import org.joda.time.DateTime
import java.util.*

/**
 * 账单列表vm
 *
 * @author darren
 * @date 2018/4/5
 */
class BillListViewModel(private val onlineRepo: OnlineRepository) : ViewModel() {

    val income = MutableLiveData<String>()
    val expense = MutableLiveData<String>()
    val month = MutableLiveData<String>()
    val budget = MutableLiveData("TODO")
    val budgetText = MutableLiveData("TODO")
    private val mDateTime = MutableLiveData<DateTime>()
    private val billInfoList = MutableLiveData<List<BillInfo>>()
    val date: LiveData<DateTime>
        get() = mDateTime

    /**
     * 设置日期
     */
    fun setDate(date: DateTime) {
        mDateTime.value = date
        val month = DateTime(date.year, date.monthOfYear,
                1, 0, 0)
        onlineRepo.getBillStats(month, month.plusMonths(1)) {
            Log.d("BillListViewModel","getBillStats income" + it?.income);
            income.value = FormatUtil.getNumeric(it?.income)
        }
        onlineRepo.getBillStats(month, month.plusMonths(1)) {
            Log.d("BillListViewModel","getBillStats expense" + it?.expense);
            expense.value = FormatUtil.getNumeric(it?.expense)
        }
        onlineRepo.getBillInfoList(date.year, date.monthOfYear) { billInfoList.value = it }
        this.month.value = month.monthOfYear.toString()
    }

    /**
     * 跳转到统计页
     */
    fun toStats(context: Context) {
        val intent = StatsActivity.newIntent(context)
        context.startActivity(intent)
    }

    fun getBillInfoList(): LiveData<List<BillInfo>> {
        Log.d("BillListViewModel","getBillInfoList"+billInfoList.value)
        return billInfoList
    }

    fun getBill(billId: UUID, callBack: (bill: Bill?) -> Unit) {
        onlineRepo.getBill(billId, callBack)
    }

}