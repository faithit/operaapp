package com.opera.firstapp.screens.demo

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun IntentScreen(navController: NavHostController){
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("INTENTS",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color=Color.Blue
        )
        val context= LocalContext.current
        //OPEN CAMERA
        Button(onClick = {
            val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            context.startActivity(intent)
        },
            modifier=Modifier.fillMaxWidth()) {
            Text("Open  Camera")
        }
        //open browser
        Button(onClick={
            val intent= Intent(Intent.ACTION_VIEW,Uri.parse("https://www.emobilis.ac.ke"))
            context.startActivity(intent)
        },
            modifier=Modifier.fillMaxWidth()) {
            Text("Open browser")
        }
    //open google maps
        Button(
            onClick = {
                val intent= Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Nairobi"))
                context.startActivity(intent)
            },
            modifier=Modifier.fillMaxWidth()
        ) {
            Text("open  maps")
        }
        //open sms
        Button(onClick = {
            val smsintent= Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:0729618566"))
            smsintent.putExtra("sms_body","hello from my intent in my app")
            context.startActivity(smsintent)
        },
            modifier=Modifier.fillMaxWidth()) {

            Text("SEND SMS")
        }
        //open email
        Button(onClick = {
            val emailintent= Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "opera@gmail.com",null))
            emailintent.putExtra(Intent.EXTRA_SUBJECT,"HELLO GOODMORNING")
            context.startActivity(emailintent)
        }) {
            Text("send email")
        }
        //open phone dial
        //simtoolkit


    }

}
@Preview(showBackground = true)
@Composable
fun intentscreenpreview(){
    IntentScreen(rememberNavController())
}