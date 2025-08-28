package io.devexpert.splitbill.usecases

import io.devexpert.splitbill.data.repository.ScanCounterRepository
import kotlinx.coroutines.flow.Flow

class GetScansRemainingUseCase(private val scanCounterRepository: ScanCounterRepository) {

    operator fun invoke(): Flow<Int> = scanCounterRepository.scansRemaining
}