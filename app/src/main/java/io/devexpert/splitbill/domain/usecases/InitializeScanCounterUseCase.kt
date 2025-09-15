package io.devexpert.splitbill.domain.usecases

import io.devexpert.splitbill.data.repository.ScanCounterRepository

class InitializeScanCounterUseCase(private val scanCounterRepository: ScanCounterRepository) {

    suspend operator fun invoke() {
        scanCounterRepository.initializeOrResetIfNeeded()
    }
}