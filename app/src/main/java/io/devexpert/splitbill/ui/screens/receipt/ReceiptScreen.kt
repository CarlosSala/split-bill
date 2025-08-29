package io.devexpert.splitbill.ui.screens.receipt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.devexpert.splitbill.R
import io.devexpert.splitbill.data.repository.TicketRepository
import io.devexpert.splitbill.data.model.TicketItem
import io.devexpert.splitbill.usecases.GetTicketDataUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    ticketRepository: TicketRepository,
    onBackPressed: () -> Unit
) {

    val getTicketDataUseCase = remember { GetTicketDataUseCase(ticketRepository) }

    val ticketData = remember { getTicketDataUseCase() }

    if (ticketData == null) {
        // if there is no ticket data, show error and back button
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_ticket_data),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(onClick = onBackPressed) {
                Text(stringResource(R.string.back))
            }
        }
        return
    }

    // states to manage selected quantities and paid items
    // associate each ticket item with its selected quantity
    var selectedQuantities by remember {
        mutableStateOf(ticketData.items.associateWith { item -> 0 })
    }
    var paidQuantities by remember {
        mutableStateOf(ticketData.items.associateWith { item -> 0 })
    }

    // Calculate total selected
    val selectedTotal = selectedQuantities.entries.sumOf { (item, quantity) ->
        item.price * quantity
    }

    // calculate available items (not paid)
    val availableItems = ticketData.items.map { item ->
        val paidQty = paidQuantities[item] ?: 0
        val availableQty = item.quantity - paidQty
        item to availableQty
    }.filter { it.second > 0 }

    // paid items
    val paidItems = ticketData.items.map { item ->
        val paidQty = paidQuantities[item] ?: 0
        item to paidQty
    }.filter { it.second > 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.receipt)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Items available
                items(availableItems) { (item, availableQty) ->
                    val selectedQty = selectedQuantities[item] ?: 0

                    SelectableTicketItemCard(
                        item = item,
                        availableQuantity = availableQty,
                        selectedQuantity = selectedQty,
                        onQuantityChange = { newQty ->
                            selectedQuantities = selectedQuantities.toMutableMap().apply {
                                this[item] = newQty
                            }
                        }
                    )
                }

                // paid items (ticked)
                items(paidItems) { (item, paidQty) ->
                    PaidTicketItemCard(
                        item = item,
                        paidQuantity = paidQty
                    )
                }
            }

            // total selected and pay button
            if (selectedTotal > 0) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.selected_total),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "€${String.format("%.2f", selectedTotal)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                // check each selected item and add to paidQuantities
                                paidQuantities = paidQuantities.toMutableMap().apply {
                                    selectedQuantities.forEach { (item, selectedQty) ->
                                        if (selectedQty > 0) {
                                            this[item] = (this[item] ?: 0) + selectedQty
                                        }
                                    }
                                }
                                // clear selection
                                selectedQuantities = selectedQuantities.mapValues { 0 }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50) // Verde
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.mark_as_paid),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableTicketItemCard(
    item: TicketItem,
    availableQuantity: Int,
    selectedQuantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // original quantity in circle
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${availableQuantity}x",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // information of the item
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "€${String.format("%.2f", item.price)} ${stringResource(R.string.each)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // selection controls
            if (availableQuantity == 1) {
                // checkbox for items with quantity 1
                Checkbox(
                    checked = selectedQuantity > 0,
                    onCheckedChange = { checked ->
                        onQuantityChange(if (checked) 1 else 0)
                    }
                )
            } else {
                // counter with +/- buttons for items with quantity > 1
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (selectedQuantity > 0) {
                                onQuantityChange(selectedQuantity - 1)
                            }
                        },
                        enabled = selectedQuantity > 0
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = stringResource(R.string.reduce_quantity)
                        )
                    }

                    Text(
                        text = selectedQuantity.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = {
                            if (selectedQuantity < availableQuantity) {
                                onQuantityChange(selectedQuantity + 1)
                            }
                        },
                        enabled = selectedQuantity < availableQuantity
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.increase_quantity)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaidTicketItemCard(
    item: TicketItem,
    paidQuantity: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // quantity in a circle with strikethrough
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${paidQuantity}x",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }

            // information of the item with strikethrough
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = "€${String.format("%.2f", item.price)} ${stringResource(R.string.each)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = TextDecoration.LineThrough
                )
            }

            // total price paid
            Text(
                text = "€${String.format("%.2f", item.price * paidQuantity)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
} 