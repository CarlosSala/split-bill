package domain.usecases

import data.ScanCounterRepository
import kotlinx.coroutines.flow.Flow

class GetScansRemainingUseCase(private val scanCounterRepository: ScanCounterRepository) {

    operator fun invoke(): Flow<Int> = scanCounterRepository.scansRemaining
}