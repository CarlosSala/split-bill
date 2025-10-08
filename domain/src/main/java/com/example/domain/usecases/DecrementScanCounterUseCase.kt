package com.example.domain.usecases

import com.example.domain.repository.ScanCounterRepository
import javax.inject.Inject


class DecrementScanCounterUseCase @Inject constructor(private val scanCounterRepository: ScanCounterRepository) {

    suspend operator fun invoke() {
        scanCounterRepository.decrementScan()
    }

}