package com.opera.firstapp.viewModel

import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.opera.firstapp.models.STKPushRequest
import com.opera.firstapp.network.MpesaAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MpesaViewModel(private val context: Context) : ViewModel() {

    // Safaricom Daraja Sandbox Credentials
    private val consumerKey = "cEGA4yFGHf13eI6LpAGXyV8L3Gg3sTG2"
    private val consumerSecret = "pG8a0H9AAnxNGq3Z"
    private val shortCode = "174379"
    private val passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
    private val callbackUrl = "https://mydomain.com/path"

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://sandbox.safaricom.co.ke/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mpesaApi = retrofit.create(MpesaAPI::class.java)

    fun performSTKPush(
        phone: String,
        amount: Double,
        onLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit
    ) {
        val formattedPhone = formatPhoneNumber(phone)
        if (formattedPhone.length != 12) {
            Toast.makeText(context, "Please enter a valid phone number (e.g. 0712345678)", Toast.LENGTH_SHORT).show()
            return
        }

        if (amount <= 0) {
            Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
            return
        }

        onLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Generate Auth Token
                val authBytes = "$consumerKey:$consumerSecret".toByteArray()
                val encodedAuth = Base64.encodeToString(authBytes, Base64.NO_WRAP)
                val tokenResponse = mpesaApi.getAccessToken("Basic $encodedAuth")

                if (!tokenResponse.isSuccessful || tokenResponse.body() == null) {
                    withContext(Dispatchers.Main) {
                        onLoading(false)
                        Toast.makeText(
                            context,
                            "Auth failed: ${tokenResponse.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return@launch
                }

                val accessToken = tokenResponse.body()!!.accessToken

                // 2. Generate Timestamp and Password
                val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                val passwordBytes = "$shortCode$passkey$timestamp".toByteArray()
                val password = Base64.encodeToString(passwordBytes, Base64.NO_WRAP)

                val intAmount = amount.toInt().toString()

                // 3. Create STK Push Request Payload
                val stkRequest = STKPushRequest(
                    businessShortCode = shortCode,
                    password = password,
                    timestamp = timestamp,
                    transactionType = "CustomerPayBillOnline",
                    amount = intAmount,
                    partyA = formattedPhone,
                    partyB = shortCode,
                    phoneNumber = formattedPhone,
                    callBackUrl = callbackUrl,
                    accountReference = "FirstApp Order",
                    transactionDesc = "Payment for order"
                )

                // 4. Send STK Push Request
                val stkResponse = mpesaApi.sendSTKPush("Bearer $accessToken", stkRequest)

                withContext(Dispatchers.Main) {
                    onLoading(false)
                    if (stkResponse.isSuccessful && stkResponse.body()?.responseCode == "0") {
                        Toast.makeText(
                            context,
                            "STK Push Sent! Please check your phone to enter M-Pesa PIN.",
                            Toast.LENGTH_LONG
                        ).show()
                        onSuccess()
                    } else {
                        val errorMsg = stkResponse.body()?.customerMessage
                            ?: stkResponse.body()?.responseDescription
                            ?: "STK Push request failed"
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onLoading(false)
                    Toast.makeText(
                        context,
                        "Error sending STK Push: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun formatPhoneNumber(phone: String): String {
        var cleanPhone = phone.trim().replace("\\s+".toRegex(), "").replace("+", "")
        if (cleanPhone.startsWith("0")) {
            cleanPhone = "254" + cleanPhone.substring(1)
        }
        return cleanPhone
    }
}