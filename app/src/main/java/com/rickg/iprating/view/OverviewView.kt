package com.rickg.iprating.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rickg.iprating.R
import com.rickg.iprating.api.IpInfoState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewView(viewModel: OverviewViewModel = viewModel()) {
    val ipInfoState by viewModel.ipInfo.observeAsState()
    val cfTrace by viewModel.cfTrace.observeAsState()
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            launch {
                viewModel.fetchIpInfo()
            }
            launch {
                viewModel.fetchCfTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.overview)) },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            launch {
                                viewModel.fetchIpInfo()
                            }
                            launch {
                                viewModel.fetchCfTrace()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                // Card of Device IP
                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = when (ipInfoState) {
                            is IpInfoState.Loading, is IpInfoState.Success -> CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                            is IpInfoState.Error -> CardDefaults.cardColors(MaterialTheme.colorScheme.error)
                            null -> CardDefaults.cardColors(MaterialTheme.colorScheme.error)
                        },
                        onClick = {}
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (ipInfoState) {
                                    is IpInfoState.Loading, is IpInfoState.Success -> Icons.Default.CheckCircle
                                    is IpInfoState.Error, null -> Icons.Filled.Warning
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .size(24.dp)
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    text = stringResource(id = R.string.device_ip),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = when (ipInfoState) {
                                        is IpInfoState.Loading -> stringResource(id = R.string.loading)
                                        is IpInfoState.Error -> stringResource(id = R.string.get_ip_error)
                                        is IpInfoState.Success -> (ipInfoState as IpInfoState.Success).ipInfo.ip
                                        null -> stringResource(id = R.string.get_ip_error)
                                    },
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
                // Card of WARP Status
                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = if (cfTrace?.warp == "off" || cfTrace?.warp == null) {
                            CardDefaults.cardColors(MaterialTheme.colorScheme.error)
                        } else {
                            CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                        },
                        onClick = { openDialog.value = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (cfTrace?.warp == "off" || cfTrace?.warp == null) {
                                    Icons.Filled.Warning
                                } else {
                                    Icons.Default.CheckCircle
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .size(24.dp)
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    text = stringResource(id = R.string.warp_status),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = when (cfTrace?.warp) {
                                        null -> stringResource(id = R.string.get_warp_error)
                                        "loading" -> stringResource(id = R.string.loading)
                                        "off" -> stringResource(id = R.string.warp_off)
                                        "on" -> stringResource(id = R.string.warp_basic)
                                        "plus" -> stringResource(id = R.string.warp_plus)
                                        else -> "N/A"
                                    },
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
                item {
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            InfoText(
                                label = stringResource(id = R.string.ip),
                                value = ipInfoState
                            ) { it.ip }
                            InfoText(
                                label = stringResource(id = R.string.hostname),
                                value = ipInfoState
                            ) { it.hostname }
                            InfoText(
                                label = stringResource(id = R.string.is_anycast),
                                value = ipInfoState
                            ) { it.anycast.toString() }
                            InfoText(
                                label = stringResource(id = R.string.bogon),
                                value = ipInfoState
                            ) { it.bogon.toString() }
                            InfoText(
                                label = stringResource(id = R.string.city),
                                value = ipInfoState
                            ) { it.city }
                            InfoText(
                                label = stringResource(id = R.string.region),
                                value = ipInfoState
                            ) { it.region }
                            InfoText(
                                label = stringResource(id = R.string.country),
                                value = ipInfoState
                            ) { it.countryCode }
                            InfoText(
                                label = stringResource(id = R.string.location),
                                value = ipInfoState
                            ) { it.location }
                            InfoText(
                                label = stringResource(id = R.string.organization),
                                value = ipInfoState
                            ) { it.org }
                            InfoText(
                                label = stringResource(id = R.string.postal),
                                value = ipInfoState
                            ) { it.postal }
                            InfoText(
                                label = stringResource(id = R.string.timezone),
                                value = ipInfoState
                            ) { it.timezone }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(onClick = {
                                    scope.launch {
                                        launch {
                                            viewModel.fetchIpInfo()
                                        }
                                        launch {
                                            viewModel.fetchCfTrace()
                                        }
                                    }
                                }) {
                                    Text(text = stringResource(id = R.string.refresh))
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    // Alert Dialog of WARP Information
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            title = {
                Text(stringResource(id = R.string.information))
            },
            text = {
                Column {
                    Text(
                        text = stringResource(id = R.string.warp_status),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // WARP Status
                    Text(
                        text = when (cfTrace?.warp) {
                            "off" -> stringResource(id = R.string.warp_off)
                            null -> stringResource(id = R.string.get_warp_error)
                            "loading" -> stringResource(id = R.string.loading)
                            else -> if (cfTrace?.warp == "on") stringResource(id = R.string.warp_basic) else stringResource(
                                id = R.string.warp_plus
                            )
                        }, style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.warp_colo_center),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // WARP Colo Center
                    Text(
                        text = when {
                            cfTrace?.warp == "off" -> stringResource(id = R.string.warp_off)
                            cfTrace?.colo == null -> stringResource(id = R.string.get_warp_error)
                            cfTrace?.colo == "loading" -> stringResource(id = R.string.loading)
                            else -> cfTrace?.colo!!
                        }, style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            }
        )
    }
}

@Preview
@Composable
fun OverviewViewPreview() {
    OverviewView()
}