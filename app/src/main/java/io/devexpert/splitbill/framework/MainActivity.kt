package io.devexpert.splitbill.framework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.devexpert.splitbill.BuildConfig
import io.devexpert.splitbill.data.datasource.local.DataStoreScanCounterDataSource
import io.devexpert.splitbill.data.datasource.remote.MLTicketDataSource
import io.devexpert.splitbill.data.datasource.mock.MockTicketDataSource
import io.devexpert.splitbill.data.repository.ScanCounterRepository
import io.devexpert.splitbill.data.repository.TicketRepository
import io.devexpert.splitbill.framework.detail.ReceiptScreen
import io.devexpert.splitbill.framework.home.HomeScreen
import io.devexpert.splitbill.framework.theme.SplitBillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataSource = if (BuildConfig.DEBUG) MockTicketDataSource() else MLTicketDataSource()
        val ticketRepository = TicketRepository(dataSource)

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
                        HomeScreen(
                            ticketRepository = ticketRepository,
                            scanCounterRepository = scanCounterRepository
                        ) {

                            navController.navigate("receipt")
                        }
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