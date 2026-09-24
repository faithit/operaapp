package com.opera.firstapp.screens.onboardingscreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.opera.firstapp.models.onboardingItems
import com.opera.firstapp.navigation.ROUTE_LOGIN
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavHostController){
    var pagerState= rememberPagerState(
        pageCount = { onboardingItems.size })
    val scope= rememberCoroutineScope ()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//horizontal pages
        HorizontalPager(
            state=pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val item = onboardingItems[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //image
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                //title
                Text(
                    text = item.title,
                    fontSize = 26.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(15.dp))
                //DESCRIpTION
                Text(
                    text = item.description,
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
                //page indicators
                Row(
                    modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(onboardingItems.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(
                                    if (isSelected) 12.dp else 8.dp
                                )
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Color.Green else Color.LightGray
                                )
                        )
                    }
                }
        Spacer(modifier = Modifier.height(20.dp))
        // Next / Get Started button
        Button(
            onClick = {
                if (pagerState.currentPage < onboardingItems.size - 1) {
                    scope.launch { pagerState.animateScrollToPage(
                            pagerState.currentPage + 1)
                    }
                } else {
                    navController.navigate(ROUTE_LOGIN)
                }
            },
                modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.Blue)
        ) {
            Text(
                text = if (
                    pagerState.currentPage == onboardingItems.size - 1
                ) { "Get Started" } else { "Next" }
            )
        }
        Spacer(modifier= Modifier.height(60.dp) )
            }
}
@Preview(showBackground = true)
@Composable
fun onboardingscreenpreview(){
    OnboardingScreen(rememberNavController())
}