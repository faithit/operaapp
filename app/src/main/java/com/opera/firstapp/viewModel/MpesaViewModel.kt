package com.opera.firstapp.viewModel

import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.opera.firstapp.models.STKPushRequest
import com.opera.firstapp.network.MpesaAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MpesaViewModel(private val context: Context) : ViewModel() {

    // Safaricom Daraja Credentials
    private val consumerKey = "hAMFwbRNtEO8iK19tjDb35lL2HmAaUaekv1OKiOJzXRCN5My"
    private val consumerSecret = "OxBKrhGfGDc48lvbZLqjjq6rPu7bUC8JRGG3Cn5mUIkK6R26FWu1fu1NAdo9lhhD"
    private val shortCode = "174379"
    private val passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
    private val callbackUrl = "https://operashop.onrender.com/callback/"

    // Set to true for Sandbox, false for Production
    var isSandbox = true

    private fun createMpesaApi(sandbox: Boolean): MpesaAPI {
        val baseUrl = if (sandbox) "https://sandbox.safaricom.co.ke/" else "https://api.safaricom.co.ke/"
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MpesaAPI::class.java)
    }

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
                val cleanKey = consumerKey.trim()
                val cleanSecret = consumerSecret.trim()
                val authBytes = "$cleanKey:$cleanSecret".toByteArray(Charsets.UTF_8)
                val encodedAuth = Base64.encodeToString(authBytes, Base64.NO_WRAP).trim()

                Log.d("MpesaViewModel", "Requesting token with Basic $encodedAuth")

                var api = createMpesaApi(isSandbox)
                var tokenResponse = api.getAccessToken("Basic $encodedAuth")

                // If Sandbox returns error (404/401), automatically fallback to Production endpoint in case Production credentials are being used
                if (!tokenResponse.isSuccessful && isSandbox) {
                    Log.w("MpesaViewModel", "Sandbox auth failed (${tokenResponse.code()}), trying Production endpoint...")
                    val prodApi = createMpesaApi(sandbox = false)
                    val prodResponse = prodApi.getAccessToken("Basic $encodedAuth")
                    if (prodResponse.isSuccessful && prodResponse.body() != null) {
                        api = prodApi
                        tokenResponse = prodResponse
                        isSandbox = false
                        Log.d("MpesaViewModel", "Switched to Production endpoint successfully")
                    }
                }

                if (!tokenResponse.isSuccessful || tokenResponse.body() == null) {
                    val errorString = tokenResponse.errorBody()?.string() ?: tokenResponse.message()
                    Log.e("MpesaViewModel", "Token error (${tokenResponse.code()}): $errorString")
                    withContext(Dispatchers.Main) {
                        onLoading(false)
                        Toast.makeText(
                            context,
                            "Auth failed (${tokenResponse.code()}): $errorString",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return@launch
                }

                val accessToken = tokenResponse.body()!!.accessToken
                Log.d("MpesaViewModel", "Access token acquired successfully")

                // 2. Generate Timestamp and Password
                val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                val passwordBytes = "$shortCode$passkey$timestamp".toByteArray(Charsets.UTF_8)
                val password = Base64.encodeToString(passwordBytes, Base64.NO_WRAP).trim()

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
                val stkResponse = api.sendSTKPush("Bearer $accessToken", stkRequest)

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
                            ?: stkResponse.errorBody()?.string()
                            ?: "STK Push request failed"
                        Log.e("MpesaViewModel", "STK Error: $errorMsg")
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MpesaViewModel", "Exception during STK Push", e)
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