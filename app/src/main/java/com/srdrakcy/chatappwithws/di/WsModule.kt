package com.srdrakcy.chatappwithws.di

import com.srdrakcy.chatappwithws.data.WsClient
import com.srdrakcy.chatappwithws.ui.chatscreen.ChatViewModel
import com.srdrakcy.chatappwithws.util.Constant.WS_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WsModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideWebSocketService(okHttpClient: OkHttpClient, viewModel: ChatViewModel, request: Request): WsClient {
        return WsClient(viewModel, okHttpClient, request)
    }

    @Provides
    @Singleton
    fun provideWebSocketRequest(): Request {
        return Request.Builder()
            .url(WS_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providesViewModel(): ChatViewModel {
        return ChatViewModel()
    }
}