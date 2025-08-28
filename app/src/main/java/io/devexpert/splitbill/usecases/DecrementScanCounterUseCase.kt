package io.devexpert.splitbill.usecases

import io.devexpert.splitbill.data.repository.ScanCounterRepository


class DecrementScanCounterUseCase(private val scanCounterRepository: ScanCounterRepository) {

    suspend operator fun invoke() {
        scanCounterRepository.decrementScan()
    }

}