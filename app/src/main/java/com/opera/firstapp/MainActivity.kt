package com.opera.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.opera.firstapp.navigation.AppNavHost
import com.opera.firstapp.screens.demo.BoxScreen
import com.opera.firstapp.screens.demo.FirstScreen
import com.opera.firstapp.screens.demo.RowScreen
import com.opera.firstapp.screens.login.LoginScreen
import com.opera.firstapp.screens.register.RegisterScreen
import com.opera.firstapp.ui.theme.FirstappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavHost()

        }
    }
}

@Composable
fun opera(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("welcome to jetpack compose",
            fontSize =28.sp,
            color= Color.Blue,
            fontStyle = FontStyle.Italic
        )
        Text("hello,this is my first app !",
            color=Color.Magenta,
            fontSize = 24.sp,
            fontFamily = FontFamily.Cursive )

    }
}

@Preview(showBackground = true)
@Composable
fun operapreview(){
    opera()
}


