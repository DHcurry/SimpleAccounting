package io.github.skywalkerdarren.simpleaccounting.util

import android.content.Context
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase
import io.github.skywalkerdarren.simpleaccounting.model.repository.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo
import io.github.skywalkerdarren.simpleaccounting.model.repository.OnlineRepository

object InjectorUtils {
    fun getCurrencyRepo(context: Context) = CurrencyRepo.getInstance(
            AppDatabase.getInstance(context.applicationContext).currencyInfoDao(),
            AppDatabase.getInstance(context.applicationContext).currencyRateDao()
    )

    fun getAppRepo(context: Context) = AppRepository.getInstance(
            AppExecutors(), context.applicationContext
    )

    fun getOnlineRepo(context: Context) = OnlineRepository.getInstance(
            AppExecutors(),context.applicationContext
    )
}