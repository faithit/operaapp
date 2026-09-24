package com.opera.firstapp.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.opera.firstapp.R
import com.opera.firstapp.navigation.ROUTE_DASHBOARD
import com.opera.firstapp.navigation.ROUTE_REGISTER

@Composable
fun LoginScreen(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text="LOGIN",
            fontSize = 40.sp,
            color = Color.Magenta,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive
        )
        Spacer(modifier= Modifier.height(20.dp))
        Text(
            text="Already have an account?Login here",
            color=Color.Blue,
            fontSize = 20.sp

        )
        Spacer(modifier= Modifier.height(20.dp))
        Image(
            painter = painterResource(id= R.drawable.logob),
            contentDescription = "logo",
            modifier= Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        Spacer(modifier= Modifier.height(20.dp))
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        //Outlinetextfield for email
        OutlinedTextField(
            value = email,
            onValueChange = {email=it},
            label = {Text("Enter email Address")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "email icon"
                )
            }
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password=it},
            label={Text("Enter password")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "password icon"
                )
            }
        )
        Spacer(modifier= Modifier.height(20.dp))
        Button(
            onClick = {
//                TODO  LATER
                navController.navigate(ROUTE_DASHBOARD)
            },
            modifier = Modifier.fillMaxWidth(),
            colors= ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Blue
            )
        ) {
            Text("LOGIN", fontSize = 24.sp)

        }
        //text button
        TextButton(onClick = {navController.navigate(ROUTE_REGISTER)}) {
            Text("Dont have an account? register here")
        }
        //display the email
        Text(
            text="email entered is :$email"
        )
    }

}
@Preview(showBackground = true)
@Composable
fun loginscreenpreview(){
    LoginScreen(rememberNavController())
}