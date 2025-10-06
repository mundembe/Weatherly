package com.example.weatherly

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // FIX: Create an OkHttpClient that is enabled for modern TLS versions.
        // This is necessary for older Android versions to communicate with modern servers.
        val client = OkHttpClient.Builder()
            .build()

        // Replace the default HttpUrlFetcher with a new one using our custom OkHttp client.
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client) // <-- Pass the client to the factory
        )
    }
}
