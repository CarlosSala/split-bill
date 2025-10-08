package com.example.data

import android.content.Context
import com.example.data.datasource.TicketDataSource
import com.example.data.datasource.local.DataStoreScanCounterDataSource
import com.example.data.datasource.local.ScanCounterDataSource
import com.example.data.datasource.mock.MockTicketDataSource
import com.example.data.datasource.remote.MLKitTicketDataSource
import com.example.data.repository.ScanCounterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import com.example.data.BuildConfig
import com.example.data.repository.TicketRepositoryImpl
import com.example.domain.repository.ScanCounterRepository
import com.example.domain.repository.TicketRepository

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
        return TicketRepositoryImpl(ticketDataSource)
    }

    @Provides
    @Singleton
    fun provideScanCounterDataSource(@ApplicationContext context: Context): ScanCounterDataSource {
        return DataStoreScanCounterDataSource(context)
    }

    @Provides
    @Singleton
    fun provideScanCounterRepository(scanCounterDataSource: ScanCounterDataSource): ScanCounterRepository {
        return ScanCounterRepositoryImpl(scanCounterDataSource)
    }

}