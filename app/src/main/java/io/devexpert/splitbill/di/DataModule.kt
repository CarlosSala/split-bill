package io.devexpert.splitbill.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.devexpert.splitbill.BuildConfig
import io.devexpert.splitbill.data.datasource.TicketDataSource
import io.devexpert.splitbill.data.datasource.local.DataStoreScanCounterDataSource
import io.devexpert.splitbill.data.datasource.local.ScanCounterDataSource
import io.devexpert.splitbill.data.datasource.mock.MockTicketDataSource
import io.devexpert.splitbill.data.datasource.remote.MLKitTicketDataSource
import io.devexpert.splitbill.data.repository.ScanCounterRepository
import io.devexpert.splitbill.data.repository.TicketRepository
import javax.inject.Qualifier
import javax.inject.Singleton

// qualifier is used to distinguish different implementations of the same interface
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MLKitDataSource

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @MockDataSource
    fun provideMockTicketDataSource(): TicketDataSource {
        return MockTicketDataSource()
    }

    @Provides
    @MLKitDataSource
    fun provideMLKitTicketDataSource(): TicketDataSource {
        return MLKitTicketDataSource()
    }

    @Provides
    @Singleton
    fun provideTicketRepository(
        @MockDataSource mockDataSource: TicketDataSource,
        @MLKitDataSource mlKitDataSource: TicketDataSource
    ): TicketRepository {
        val ticketDataSource = if (BuildConfig.DEBUG) {
            mockDataSource
        } else {
            mlKitDataSource
        }
        return TicketRepository(ticketDataSource)
    }

    @Provides
    @Singleton
    fun provideScanCounterDataSource(@ApplicationContext context: Context): ScanCounterDataSource {
        return DataStoreScanCounterDataSource(context)
    }

    @Provides
    @Singleton
    fun provideScanCounterRepository(scanCounterDataSource: ScanCounterDataSource): ScanCounterRepository {
        return ScanCounterRepository(scanCounterDataSource)
    }

}