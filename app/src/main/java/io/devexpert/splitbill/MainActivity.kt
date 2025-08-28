package io.devexpert.splitbill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import data.MLTicketDataSource
import data.MockTicketDataSource
import data.TicketRepository
import io.devexpert.splitbill.ui.theme.SplitBillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataSource = if (BuildConfig.DEBUG) MockTicketDataSource() else MLTicketDataSource()
        val ticketRepository = TicketRepository(dataSource)

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
                            onTicketProcessed = { ticketData ->

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