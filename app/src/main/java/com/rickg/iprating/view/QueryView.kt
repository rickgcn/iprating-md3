package com.rickg.iprating.view

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rickg.iprating.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueryView(viewModel: QueryViewModel = viewModel()) {
    val ipInfo by viewModel.ipInfo.observeAsState()
    val scope = rememberCoroutineScope()
    var searchText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    modifier = Modifier.padding(end = 4.dp),
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text(stringResource(id = R.string.query_text)) },
                    singleLine = true
                )
                Button(onClick = {
                    scope.launch {
                        viewModel.fetchIpInfo(searchText.trim())
                    }
                }) {
                    Text(text = stringResource(id = R.string.query))
                }
            }

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
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
                            value = ipInfo?.ip.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.hostname),
                            value = ipInfo?.hostname.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.is_anycast),
                            value = ipInfo?.anycast.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.bogon),
                            value = ipInfo?.bogon.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.city),
                            value = ipInfo?.city.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.region),
                            value = ipInfo?.region.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.country),
                            value = ipInfo?.country.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.location),
                            value = ipInfo?.loc.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.organization),
                            value = ipInfo?.org.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.postal),
                            value = ipInfo?.postal.toString()
                        )
                        InfoText(
                            label = stringResource(id = R.string.timezone),
                            value = ipInfo?.timezone.toString()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun QueryViewPreview() {
    QueryView()
}