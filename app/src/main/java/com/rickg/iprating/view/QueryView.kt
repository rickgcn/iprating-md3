package com.rickg.iprating.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rickg.iprating.R

@Composable
fun QueryView(viewModel: QueryViewModel = viewModel()) {
    val ipInfoState by viewModel.ipInfo.observeAsState()
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
                    viewModel.fetchIpInfo(searchText.trim())
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
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