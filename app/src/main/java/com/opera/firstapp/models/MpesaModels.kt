package com.opera.firstapp.models

import com.google.gson.annotations.SerializedName

data class STKPushRequest(
    @SerializedName("BusinessShortCode") val businessShortCode: String,
    @SerializedName("Password") val password: String,
    @SerializedName("Timestamp") val timestamp: String,
    @SerializedName("TransactionType") val transactionType: String = "CustomerPayBillOnline",
    @SerializedName("Amount") val amount: String,
    @SerializedName("PartyA") val partyA: String,
    @SerializedName("PartyB") val partyB: String,
    @SerializedName("PhoneNumber") val phoneNumber: String,
    @SerializedName("CallBackURL") val callBackUrl: String,
    @SerializedName("AccountReference") val accountReference: String,
    @SerializedName("TransactionDesc") val transactionDesc: String
)

data class STKPushResponse(
    @SerializedName("MerchantRequestID") val merchantRequestId: String? = null,
    @SerializedName("CheckoutRequestID") val checkoutRequestId: String? = null,
    @SerializedName("ResponseCode") val responseCode: String? = null,
    @SerializedName("ResponseDescription") val responseDescription: String? = null,
    @SerializedName("CustomerMessage") val customerMessage: String? = null,
    @SerializedName("errorMessage") val errorMessage: String? = null
)

data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: String
)