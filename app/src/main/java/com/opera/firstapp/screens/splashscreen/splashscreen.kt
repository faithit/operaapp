package com.opera.firstapp.screens.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.opera.firstapp.ui.theme.myblue
import com.opera.firstapp.R
import com.opera.firstapp.navigation.ROUTE_LOGIN
import com.opera.firstapp.navigation.ROUTE_ONBOARDING
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController){
    LaunchedEffect(true) {
        delay(2000)//2 second delay
        navController.navigate(ROUTE_ONBOARDING)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffD714F1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //LOGO
        Image(
            painter = painterResource(id= R.drawable.logob),
            contentDescription = "logo",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
        //app name
        Text(
            text="Opera App",
            color= Color.Green,
            fontSize = 32.sp
        )

    }

}
@Preview(showBackground = true)
@Composable
fun slashscreenpreview(){
    SplashScreen(rememberNavController())
}