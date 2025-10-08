package com.example.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface ScanCounterDataSource {
    val scansRemaining: Flow<Int>
    suspend fun initializeOrResetIfNeeded()
    suspend fun decrementScan()
}