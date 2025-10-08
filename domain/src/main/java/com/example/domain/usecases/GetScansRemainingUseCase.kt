package com.example.domain.usecases

import com.example.domain.repository.ScanCounterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetScansRemainingUseCase @Inject constructor(private val scanCounterRepository: ScanCounterRepository) {

    operator fun invoke(): Flow<Int> = scanCounterRepository.scansRemaining
}