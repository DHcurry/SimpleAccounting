package io.github.skywalkerdarren.simpleaccounting.model.repository

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import io.github.skywalkerdarren.simpleaccounting.adapter.DateHeaderDivider
import io.github.skywalkerdarren.simpleaccounting.model.entity.*
import io.github.skywalkerdarren.simpleaccounting.model.service.BillService
import io.github.skywalkerdarren.simpleaccounting.model.service.JavaAccountService
import io.github.skywalkerdarren.simpleaccounting.model.service.JavaStatusService
import io.github.skywalkerdarren.simpleaccounting.model.service.TypeService
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors
import org.joda.time.DateTime
import java.math.BigDecimal
import java.util.*

class OnlineRepository private constructor(val executors: AppExecutors, context: Context) {
    var baseUrl = "http://114.116.237.243:8082";
//      var baseUrl = "http://10.0.2.2:8082";
    val accountService:JavaAccountService = JavaAccountService();
    val statusService:JavaStatusService = JavaStatusService();
    val billService: BillService = BillService();
    val typeService:TypeService = TypeService();

    private fun slowDown() {
        if (OnlineRepository.DEBUG) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }



    /**
     * 获取单个account
     */
    fun getAccount(uuid: UUID, callBack: (Account?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getAccount: in " + Thread.currentThread().name)
            val account = accountService.getAccount(baseUrl, uuid)
            executors.mainThread().execute { callBack(account) }
        }
    }

    /**
     * 由于是单机版，因此，我只要同步自己的数据，这个接口就不调用uuid获取
     */
    fun getAccounts(callBack: (List<Account>?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getAccounts: in " + Thread.currentThread().name)
            val accounts = accountService.getAccounts(baseUrl);
            executors.mainThread().execute { callBack(accounts) }
        }
    }



    fun getBill(id: UUID, callBack: (bill: Bill?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getBill: in " + Thread.currentThread().name)
            val bill: Bill? = billService.getBill(baseUrl, id)
            executors.mainThread().execute { callBack(bill) }
        }
    }

    /**
     * 获取类型
     */
    fun getType(uuid: UUID, callBack: (Type?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getType: in " + Thread.currentThread().name)
            val type = typeService.getType(baseUrl, uuid)
            executors.mainThread().execute { callBack(type) }
        }
    }

    /**
     * 对外提供的获取账单的方法
     */
    public fun getBillStats(start: DateTime, end: DateTime, callBack: (BillStats?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getBillStats: in " + Thread.currentThread().name)
            val stats = statusService.getBillsStats(baseUrl, start, end)
            Log.d(OnlineRepository.TAG, "getBillStats: stats " + stats)
            val billStats = BillStats()
            if (stats != null) {
                for ((balance, expense) in stats) {
                    if (expense) {
                        billStats.expense = balance
                    } else {
                        billStats.income = balance
                    }
                }
            }
            executors.mainThread().execute { callBack(billStats) }
        }
    }

    /**
     * 对内提供获取账户整体收入与支出
     */
    private fun getBillStats(start: DateTime, end: DateTime): BillStats {
        val stats = statusService.getBillsStats(baseUrl, start, end)
        val billStats = BillStats()
        if (stats != null) {
            for ((balance, expense) in stats) {
                if (expense) {
                    billStats.expense = balance
                } else {
                    billStats.income = balance
                }
            }
        }
        return billStats
    }

    /**
     * 更新account a 与 account b的id
     */
    fun changePosition(a: Account, b: Account) {
        execute {
            Log.d(OnlineRepository.TAG, "changePosition: in " + Thread.currentThread().name)
            val i = a.id
            val j = b.id
            a.id = j
            b.id = i
            accountService.updateAccountId(baseUrl, a.uuid, -1)
            accountService.updateAccountId(baseUrl, b.uuid, i)
            accountService.updateAccountId(baseUrl, a.uuid, j)
        }
    }

    /**
     * 获取account状态——余额以及收入和支出情况
     */
    fun getAccountStats(accountId: UUID, start: DateTime, end: DateTime, callBack: (AccountStats?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getAccountStats: in " + Thread.currentThread().name)
            val accountStats = AccountStats()
            statusService.getAccountStats(baseUrl, accountId, start, end)
                    ?.forEach { (balance, expense) ->
                        if (expense) {
                            accountStats.expense = balance
                        } else {
                            accountStats.income = balance
                        }
                    }
            executors.mainThread().execute { callBack(accountStats) }
        }
    }

    /**
     * 获取每个类型的余额
     */
    fun getTypeStats(start: DateTime, end: DateTime, typeId: UUID, callBack: (TypeStats?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getTypeStats: in " + Thread.currentThread().name)
            val typeStats = typeService.getTypeStats(baseUrl,start, end, typeId)
            executors.mainThread().execute { callBack(typeStats) }
        }
    }

    /**
     * 获取每个类型的平均值
     */
    fun getTypeAverage(start: DateTime, end: DateTime, typeId: UUID, callBack: (TypeStats?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getTypeAverage: in " + Thread.currentThread().name)
            val typeStats = typeService.getTypeAverageStats(baseUrl,start, end, typeId)
            executors.mainThread().execute { callBack(typeStats) }
        }
    }

    /**
     * 删除账单
     */
    fun delBill(id: UUID) {
        execute {
            Log.d(OnlineRepository.TAG, "delBill: in " + Thread.currentThread().name)
            val bill = billService.getBill(baseUrl, id) ?: return@execute
            // 更新account信息
            updateAccountBalanceByMinus(bill)
            billService.delBill(baseUrl, id)
        }
    }

    /**
     * 获取账单列表
     */
    fun getBillInfoList(year: Int, month: Int, callBack: (billsInfo: List<BillInfo>?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getBillInfoList: in " + Thread.currentThread().name)
            val start = DateTime(year, month, 1, 1, 0, 0)
            val end = start.plusMonths(1)

            val bills = billService.getsBillsByDate(baseUrl, start, end)
            val billInfoList: MutableList<BillInfo> = ArrayList()
            var date: DateTime? = null

            // 上一个账单的年月日
            bills?.indices?.forEach { i ->
                val bill = bills[i]
                val type = typeService.getType(baseUrl, bill.typeId)
                val dateTime = bill.date
                val y = dateTime.year
                val m = dateTime.monthOfYear
                val d = dateTime.dayOfMonth
                // 当前账单的年月日
                val currentDate = DateTime(y, m, d, 0, 0)
                // 如果当前帐单与上一张单年月日不同，则添加账单
                if (date == null || date != currentDate) {
                    date = currentDate
                    val income = getBillStats(currentDate, currentDate.plusDays(1)).income
                    val expense = getBillStats(currentDate, currentDate.plusDays(1)).expense
                    billInfoList.add(BillInfo(DateHeaderDivider(currentDate, income, expense)))
                }
                Log.d(OnlineRepository.TAG, "getBillInfoList: $bill")
                if (type != null) billInfoList.add(BillInfo(bill, type))
            }
            executors.mainThread().execute { callBack(billInfoList) }
        }
    }

    /**
     * 根据支出与消费获取类型
     */
    @Synchronized fun getTypes(isExpense: Boolean, callBack: (List<Type>?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getTypes: in " + Thread.currentThread().name)
            val types = typeService.getTypes(baseUrl, isExpense)
            executors.mainThread().execute { callBack(types) }
        }
    }

    /**
     * 添加账单
     */
    fun addBill(bill: Bill) {
        execute {
            Log.d(OnlineRepository.TAG, "addBill: in " + Thread.currentThread().name)
            billService.addBill(baseUrl, bill)
            updateAccountBalanceByAdd(bill)
        }
    }

    /**
     * 更新账单
     */
    fun updateBill(bill: Bill) {
        execute {
            Log.d(OnlineRepository.TAG, "updateBill: in " + Thread.currentThread().name + bill)
            val old = billService.getBill(baseUrl,bill.uuid) ?: return@execute
            updateAccountBalanceByMinus(old)
            updateAccountBalanceByAdd(bill)
            billService.updateBill(baseUrl, bill)
        }
    }

    fun getTypesStats(start: DateTime, end: DateTime, isExpense: Boolean, callBack: (List<TypeStats>?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getTypesStats: in " + Thread.currentThread().name)
            val typesStats = statusService.getTypesStats(baseUrl, start, end, isExpense)
            executors.mainThread().execute { callBack(typesStats) }
        }
    }

    fun getBillsCount(callBack: (count: Int) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getBillsCount: in " + Thread.currentThread().name)
            val count = billService.getbillsCount(baseUrl);
            executors.mainThread().execute { callBack(count) }
        }
    }

    fun getBillsCount(year: Int, month: Int, callBack: (count: Int) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getBillsCount: in " + Thread.currentThread().name)
            val start = DateTime(year, month, 1, 0, 0)
            val end = start.plusMonths(1)
            val count = billService.getBillsCount(baseUrl, start, end)
            executors.mainThread().execute { callBack(count) }
        }
    }

    fun getBillsAnnualStats(year: Int, callBack: (List<BillStats>?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG, "getBillsAnnualStats: in " + Thread.currentThread().name)
            var start = DateTime(year, 1, 1, 0, 0, 0)
            var end = start.plusMonths(1)
            val billStatsList: MutableList<BillStats> = ArrayList(12)
            for (i in 1..12) {
                val stats = statusService.getBillsStats(baseUrl, start, end)
                val billStats = BillStats()
                if (stats != null) {
                    for ((balance, expense) in stats) {
                        if (expense) {
                            billStats.expense = balance
                        } else {
                            billStats.income = balance
                        }
                    }
                }
                billStatsList.add(billStats)
                start = start.plusMonths(1)
                end = end.plusMonths(1)
            }
            executors.mainThread().execute { callBack(billStatsList) }
        }
    }

    fun addAccount(name: String, hint: String, balance: String,callBack: (res: String) -> Unit){
        execute {
            Log.d(OnlineRepository.TAG, "getBillsAnnualStats: in " + Thread.currentThread().name)
            var res = accountService.addAccount(baseUrl, name, hint, balance);
            executors.mainThread().execute { callBack(res) }
        }
    }

    /**
     * 更新账户余额
     */
    fun updateAccountBalance(uuid: UUID, balance: String) {
        execute {
            Log.d(OnlineRepository.TAG,"updateAccountBalance in"+Thread.currentThread().name)
            var suc = accountService.updateAccountBalance(baseUrl,uuid, BigDecimal(balance));
        }
    }

    /**
     * 删除账户
     */
    fun delAccount(uuid: UUID) {
        execute {
            Log.d(OnlineRepository.TAG,"delAccount in"+Thread.currentThread().name)
            var suc = accountService.delAccount(baseUrl,uuid);
        }
    }

    /**
     * 获取该账户下最近50条数据
     */
    fun getBillsByAccount(accountId: UUID,callBack: (bills: List<BillInfo>?) -> Unit) {
        execute {
            Log.d(OnlineRepository.TAG,"getBillsByAccount in"+Thread.currentThread().name)
            var billList = billService.getBillByAccount(baseUrl,accountId);
            Log.d(OnlineRepository.TAG,"billInfos = "+billList);
            executors.mainThread().execute{ callBack(billList)}
        }
    }

    /**
     * 获取该品类下的账单
     */
    fun getTypeBills(typeAndStats: TypeAndStats, start: DateTime, end: DateTime, callBack: (bills:List<BillInfo>) -> Unit) {
        execute {
            var billList = billService.getTypeBills(baseUrl,typeAndStats,start,end)
            Log.d(OnlineRepository.TAG,"typeBills = "+billList);
            executors.mainThread().execute { callBack(billList)}
        }

    }

    private fun updateAccountBalanceByAdd(bill: Bill) {
        val type = typeService.getType(baseUrl, bill.typeId) ?: return
        val account = accountService.getAccount(baseUrl, bill.accountId)
        val balance = account!!.balance.add(if (type.isExpense) bill.balance?.negate() else bill.balance)
        accountService.updateAccountBalance(baseUrl, account.uuid, balance)
    }

    private fun updateAccountBalanceByMinus(bill: Bill) {
        val account = accountService.getAccount(baseUrl, bill.accountId)
        var type = typeService.getType(baseUrl, bill.typeId);
        val balance = account!!.balance.add((if (type?.isExpense!!) bill.balance else bill.balance?.negate())!!);
        accountService.updateAccountBalance(baseUrl, account.uuid, balance)
    }

    // 线程执行框架
    private fun execute(loadData: () -> Unit) {
        val runnable = Runnable {
            slowDown()
            loadData()
        }
        executors.diskIO().execute(runnable)
    }




    // 定义的伴随类
    companion object {
        private const val TAG = "OnlineRepository"
        private const val DEBUG = false

        @Volatile
        private var INSTANCE: OnlineRepository? = null

        @JvmStatic
        fun getInstance(executors: AppExecutors, context: Context): OnlineRepository =
                INSTANCE
                        ?: synchronized(this) {
                            INSTANCE
                                    ?: OnlineRepository(executors,context).also { INSTANCE = it }
                        }


        @JvmStatic
        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }


}