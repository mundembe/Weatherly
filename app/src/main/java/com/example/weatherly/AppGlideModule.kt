package com.example.weatherly

import android.content.Context
import android.os.Build
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import java.io.InputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // --- FIX: Create an OkHttpClient that is enabled for modern TLS versions. ---
        val client = OkHttpClient.Builder()
            .apply {
                // This is the crucial part. On older APIs (16-21), OkHttp might need help
                // to enable TLS 1.2. This ensures it's available.
                if (Build.VERSION.SDK_INT in 16..21) {
                    enableTls12(this)
                }
            }
            .build()

        // Replace the default HttpUrlFetcher with a new one using our custom OkHttp client.
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }

    // Helper function to enable TLS 1.2 on older Android versions.
    private fun enableTls12(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return try {
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                // Return original builder if we can't find the default trust manager.
                return builder
            }
            val trustManager = trustManagers[0] as X509TrustManager

            // Create a ConnectionSpec that forces TLS 1.2.
            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()

            builder
                .sslSocketFactory(Tls12SocketFactory(sslContext.socketFactory), trustManager)
                .connectionSpecs(listOf(spec, ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build()))


        } catch (e: Exception) {
            // If something goes wrong, log it and return the original builder.
            // In a production app, you'd want to log this error.
            builder
        }
    }
}
