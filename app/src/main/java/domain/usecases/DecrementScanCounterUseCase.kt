package domain.usecases

import data.ScanCounterRepository


class DecrementScanCounterUseCase(private val scanCounterRepository: ScanCounterRepository) {

    suspend operator fun invoke() {
        scanCounterRepository.decrementScan()
    }

}