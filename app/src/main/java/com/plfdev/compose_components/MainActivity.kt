package com.plfdev.compose_components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plfdev.compose_components.components.VicoCharts
import com.plfdev.compose_components.ui.theme.ComposecomponentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposecomponentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VicoCharts(
                        modifier = Modifier.padding(innerPadding).padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposecomponentsTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            VicoCharts(
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}