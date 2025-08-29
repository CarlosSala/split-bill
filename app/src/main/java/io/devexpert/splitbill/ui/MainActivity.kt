package io.devexpert.splitbill.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.devexpert.splitbill.BuildConfig
import io.devexpert.splitbill.data.datasource.local.DataStoreScanCounterDataSource
import io.devexpert.splitbill.data.datasource.remote.MLTicketDataSource
import io.devexpert.splitbill.data.datasource.mock.MockTicketDataSource
import io.devexpert.splitbill.data.repository.ScanCounterRepository
import io.devexpert.splitbill.data.repository.TicketRepository
import io.devexpert.splitbill.ui.screens.receipt.ReceiptScreen
import io.devexpert.splitbill.ui.screens.home.HomeScreen
import io.devexpert.splitbill.ui.screens.home.HomeViewModel
import io.devexpert.splitbill.ui.theme.SplitBillTheme
import io.devexpert.splitbill.usecases.DecrementScanCounterUseCase
import io.devexpert.splitbill.usecases.GetScansRemainingUseCase
import io.devexpert.splitbill.usecases.InitializeScanCounterUseCase
import io.devexpert.splitbill.usecases.ProcessTicketUseCase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val ticketDataSource = if (BuildConfig.DEBUG) MockTicketDataSource() else MLTicketDataSource()
        val ticketRepository = TicketRepository(ticketDataSource)

        val scanCounterDataSource = DataStoreScanCounterDataSource(this)
        val scanCounterRepository = ScanCounterRepository(scanCounterDataSource)


        setContent {
            SplitBillTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {

                        val homeViewModel: HomeViewModel = viewModel {
                            HomeViewModel(
                                InitializeScanCounterUseCase(scanCounterRepository),
                                GetScansRemainingUseCase(scanCounterRepository),
                                DecrementScanCounterUseCase(scanCounterRepository),
                                ProcessTicketUseCase(ticketRepository)
                            )
                        }
                        HomeScreen(
                            viewModel = homeViewModel,
                            onTicketProcessed = {
                                navController.navigate("receipt")
                            }
                        )
                    }

                    composable("receipt") {
                        ReceiptScreen(
                            ticketRepository = ticketRepository,
                            onBackPressed = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}