package io.github.skywalkerdarren.simpleaccounting.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeAndStats
import io.github.skywalkerdarren.simpleaccounting.model.repository.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.repository.OnlineRepository
import org.joda.time.DateTime
import org.joda.time.Period

/**
 * 分类页vm
 *
 * @author darren
 * @date 2018/4/6
 */
class ClassifyViewModel(private val onlineRepo: OnlineRepository) : ViewModel() {

    lateinit var typeAndStats : TypeAndStats
    var billList =  MutableLiveData<List<BillInfo>>()

    val date = MutableLiveData<String>()
    val typeAndStatsList = MutableLiveData<List<TypeAndStats>>()
    var start: DateTime
        private set
    var end: DateTime
        private set
    private var mPeriod: Period
    var isExpense = true


    fun setDate(start: DateTime, end: DateTime) {

        this.end = end
        this.start = start
        mPeriod = Period(this.start, this.end)
        setStatsList(this.start, this.end, isExpense)
    }

    fun start() {
        setStatsList(start, end, isExpense)
    }

    private fun setStatsList(start: DateTime, end: DateTime, isExpense: Boolean) {
        Log.d("ClassifyViewModel","start="+start+" end="+end)
        val pattern = "yyyy年MM月dd日"
        date.value = start.toString(pattern) + " - " + end.toString(pattern)
        onlineRepo.getTypes(isExpense) { types ->
            types ?: return@getTypes
            onlineRepo.getTypesStats(start, end, isExpense) { typesStats ->
                typesStats ?: return@getTypesStats
                typeAndStatsList.value = typesStats.map { typeStats ->
                    TypeAndStats(types.find { it.uuid == typeStats.typeId }
                            ?: return@getTypesStats, typeStats)
                }
            }
        }
    }

    fun getTypeBill(typeAndStats: TypeAndStats){
        onlineRepo.getTypeBills(typeAndStats, start, end){
            billList.value = it
        }
    }

    /**
     * 后退日期
     */
    fun back() {
        end = end.minus(mPeriod)
        start = start.minus(mPeriod)
    }

    /**
     * 前进日期
     */
    fun more() {
        end = end.plus(mPeriod)
        start = start.plus(mPeriod)
    }

    init {
        val now = DateTime.now()
        start = DateTime(now.year, now.monthOfYear, 1, 0, 0)
        end = start.plusMonths(1)
        mPeriod = Period(start, end)
    }
}