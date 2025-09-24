package io.devexpert.splitbill.di

import android.content.Context
import io.devexpert.splitbill.BuildConfig
import io.devexpert.splitbill.data.datasource.local.DataStoreScanCounterDataSource
import io.devexpert.splitbill.data.datasource.mock.MockTicketDataSource
import io.devexpert.splitbill.data.datasource.remote.MLKitTicketDataSource
import io.devexpert.splitbill.data.repository.ScanCounterRepository
import io.devexpert.splitbill.data.repository.TicketRepository
import io.devexpert.splitbill.domain.usecases.DecrementScanCounterUseCase
import io.devexpert.splitbill.domain.usecases.GetScansRemainingUseCase
import io.devexpert.splitbill.domain.usecases.GetTicketDataUseCase
import io.devexpert.splitbill.domain.usecases.InitializeScanCounterUseCase
import io.devexpert.splitbill.domain.usecases.ProcessTicketUseCase
import io.devexpert.splitbill.ui.screens.home.HomeViewModel
import io.devexpert.splitbill.ui.screens.receipt.ReceiptViewModel

object AppModule {


    private var ticketRepository: TicketRepository? = null
    private var scanCounterRepository: ScanCounterRepository? = null

    fun provideTicketRepository(): TicketRepository {

        if (ticketRepository == null) {
            val ticketDataSource = if (BuildConfig.DEBUG) {
                MockTicketDataSource()
            } else {
                MLKitTicketDataSource()
            }
            ticketRepository = TicketRepository(ticketDataSource)
        }
        return ticketRepository!!
    }

    fun provideScanCounterRepository(context: Context): ScanCounterRepository {
        if (scanCounterRepository == null) {
            val scanCounterDataSource = DataStoreScanCounterDataSource(context)
            scanCounterRepository = ScanCounterRepository(scanCounterDataSource)
        }
        return scanCounterRepository!!
    }

    fun provideProcessTicketUseCase(context: Context): ProcessTicketUseCase {
        return ProcessTicketUseCase(provideTicketRepository())
    }

    fun provideInitializeScanCounterUseCase(context: Context): InitializeScanCounterUseCase {
        return InitializeScanCounterUseCase(provideScanCounterRepository(context))
    }

    fun provideGetScansRemainingUseCase(context: Context): GetScansRemainingUseCase {
        return GetScansRemainingUseCase(provideScanCounterRepository(context))
    }

    fun provideDecrementScanCounterUseCase(context: Context): DecrementScanCounterUseCase {
        return DecrementScanCounterUseCase(provideScanCounterRepository(context))
    }

    fun provideGetTicketUseCase(context: Context): GetTicketDataUseCase {
        return GetTicketDataUseCase(provideTicketRepository())
    }

    fun createHomeViewModel(context: Context): HomeViewModel {
        return HomeViewModel(
            processTicketUseCase = provideProcessTicketUseCase(context),
            initializeScanCounterUseCase = provideInitializeScanCounterUseCase(context),
            getScansRemainingUseCase = provideGetScansRemainingUseCase(context),
            decrementScanCounterUseCase = provideDecrementScanCounterUseCase(context)
        )
    }

    fun createReceiptViewModel(context: Context): ReceiptViewModel {
        return ReceiptViewModel(
            getTicketDataUseCase = provideGetTicketUseCase(context)
        )
    }
}