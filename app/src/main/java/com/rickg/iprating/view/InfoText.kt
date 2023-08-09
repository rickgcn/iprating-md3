package com.rickg.iprating.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rickg.iprating.R
import com.rickg.iprating.api.IpInfoState
import io.ipinfo.api.model.IPResponse

@Composable
fun InfoText(label: String, value: IpInfoState?, content: (IPResponse) -> String?) {
    Text(text = label, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = when (value) {
            is IpInfoState.Loading -> stringResource(id = R.string.loading)
            is IpInfoState.Success -> when (content(value.ipInfo)) {
                "true" -> stringResource(id = R.string.true_text)
                "false" -> stringResource(id = R.string.false_text)
                null -> "N/A"
                else -> content(value.ipInfo)!!
            }
            is IpInfoState.Error -> value.message
            null -> stringResource(id = R.string.no_data)
        }, style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(16.dp))
}