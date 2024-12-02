package com.example.kpi_mobileappdev_lab6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kpi_mobileappdev_lab6.navigation.AppNavHost
import com.example.kpi_mobileappdev_lab6.ui.theme.KPIMobileAppDevLab6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KPIMobileAppDevLab6Theme {
                AppNavHost()
            }
        }
    }
}
