package com.vfd.client.network

import android.content.Context
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

class LanguageInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val lang = prefs.getString("lang_pref", null) ?: Locale.getDefault().language
        val request = chain.request().newBuilder()
            .header("Accept-Language", lang)
            .build()
        return chain.proceed(request)
    }
}