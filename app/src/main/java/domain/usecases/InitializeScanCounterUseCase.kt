package domain.usecases

import data.ScanCounterRepository

class InitializeScanCounterUseCase(private val scanCounterRepository: ScanCounterRepository) {

    suspend operator fun invoke() {
        scanCounterRepository.initializeOrResetIfNeeded()
    }
}