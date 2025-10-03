package io.devexpert.splitbill.domain.usecases

import io.devexpert.splitbill.data.repository.ScanCounterRepository
import javax.inject.Inject

class InitializeScanCounterUseCase @Inject constructor(private val scanCounterRepository: ScanCounterRepository) {

    suspend operator fun invoke() {
        scanCounterRepository.initializeOrResetIfNeeded()
    }
}