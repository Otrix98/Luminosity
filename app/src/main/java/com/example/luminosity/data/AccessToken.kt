package com.example.luminosity.data

import android.content.Context

object AccessToken {
   var value: String? = null
   val authorizationFailed: Boolean
      get() = (AccessToken.value == null)

   const val TOKEN_PREFERENCES = "token_preferences"
   const val TOKEN_STRING = "token string"

   fun saveToken(context: Context) {
      val editor = context.getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE).edit()
      editor.putString(TOKEN_STRING, AccessToken.value)
      editor.apply()
   }

   fun deleteToken(context: Context) {
      val editor = context.getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE).edit()
      editor.remove(TOKEN_STRING)
      editor.apply()
      AccessToken.value = null
   }
}