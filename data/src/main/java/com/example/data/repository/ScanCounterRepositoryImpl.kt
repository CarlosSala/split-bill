package com.example.data.repository

import com.example.data.datasource.local.ScanCounterDataSource
import com.example.domain.repository.ScanCounterRepository
import kotlinx.coroutines.flow.Flow

class ScanCounterRepositoryImpl(
    private val scanCounterDataSource: ScanCounterDataSource
) :
    ScanCounterRepository {

    override val scansRemaining: Flow<Int> = scanCounterDataSource.scansRemaining

    override suspend fun initializeOrResetIfNeeded() {
        scanCounterDataSource.initializeOrResetIfNeeded()
    }

    override suspend fun decrementScan() {
        scanCounterDataSource.decrementScan()
    }
}