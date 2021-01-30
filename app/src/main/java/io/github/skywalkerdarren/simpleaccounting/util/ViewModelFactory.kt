package io.github.skywalkerdarren.simpleaccounting.util

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.lifecycle.ViewModelProviders
import io.github.skywalkerdarren.simpleaccounting.model.repository.AppRepository
import io.github.skywalkerdarren.simpleaccounting.model.repository.CurrencyRepo
import io.github.skywalkerdarren.simpleaccounting.model.repository.OnlineRepository
import io.github.skywalkerdarren.simpleaccounting.view_model.*

class ViewModelFactory private constructor(private val onlineRepository: OnlineRepository, private val mCurrencyRepo: CurrencyRepo) : NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                return AccountViewModel(onlineRepository) as T
            }
            modelClass.isAssignableFrom(BillDetailViewModel::class.java) -> {
                return BillDetailViewModel(onlineRepository) as T
            }
            modelClass.isAssignableFrom(BillEditViewModel::class.java) -> {
                return BillEditViewModel(onlineRepository) as T
            }
            modelClass.isAssignableFrom(BillListViewModel::class.java) -> {
                return BillListViewModel(onlineRepository) as T
            }
            modelClass.isAssignableFrom(ClassifyViewModel::class.java) -> {
                return ClassifyViewModel(onlineRepository) as T
            }
            modelClass.isAssignableFrom(JournalViewModel::class.java) -> {
                return JournalViewModel(onlineRepository) as T
            }
            modelClass.isAssignableFrom(ChartViewModel::class.java) -> {
                return ChartViewModel(onlineRepository) as T
            }
            modelClass.isAssignableFrom(DiscoveryViewModel::class.java) -> {
                return DiscoveryViewModel(onlineRepository, mCurrencyRepo) as T
            }
            modelClass.isAssignableFrom(EmptyListViewModel::class.java) -> {
                return EmptyListViewModel() as T
            }
            modelClass.isAssignableFrom(FeedBackViewModel::class.java) -> {
                return FeedBackViewModel() as T
            }
            modelClass.isAssignableFrom(CurrencyFavViewModel::class.java) -> {
                return CurrencyFavViewModel(mCurrencyRepo) as T
            }
            modelClass.isAssignableFrom(CurrencySelectViewModel::class.java) -> {
                return CurrencySelectViewModel(mCurrencyRepo) as T
            }
            modelClass.isAssignableFrom(AccountEditViewModel::class.java)->{
                return AccountEditViewModel(onlineRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)->{
                return LoginViewModel(onlineRepository) as T
            }
            else -> throw IllegalArgumentException("no this ViewModel")
        }
    }

    fun <T : ViewModel> obtainViewModel(fragment: Fragment, modelClass: Class<T>) =
            ViewModelProviders.of(fragment, this).get(modelClass)

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory = INSTANCE
                ?: synchronized(this) {
                    INSTANCE ?: ViewModelFactory(
                            InjectorUtils.getOnlineRepo(application),
                            InjectorUtils.getCurrencyRepo(application)
                    ).also { INSTANCE = it }
                }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}