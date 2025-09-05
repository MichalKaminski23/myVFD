package com.vfd.client.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.vfd.client.data.remote.api.AssetApi
import com.vfd.client.data.remote.api.AssetTypeApi
import com.vfd.client.data.remote.api.AuthApi
import com.vfd.client.data.remote.api.EventApi
import com.vfd.client.data.remote.api.FiredepartmentApi
import com.vfd.client.data.remote.api.FirefighterActivityApi
import com.vfd.client.data.remote.api.FirefighterActivityTypeApi
import com.vfd.client.data.remote.api.FirefighterApi
import com.vfd.client.data.remote.api.InspectionApi
import com.vfd.client.data.remote.api.InspectionTypeApi
import com.vfd.client.data.remote.api.InvestmentProposalApi
import com.vfd.client.data.remote.api.OperationApi
import com.vfd.client.data.remote.api.OperationTypeApi
import com.vfd.client.data.remote.api.UserApi
import com.vfd.client.data.remote.api.VoteApi
import com.vfd.client.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://192.168.2.149:8080"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json, client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAssetApi(retrofit: Retrofit): AssetApi =
        retrofit.create(AssetApi::class.java)

    @Provides
    @Singleton
    fun provideAssetTypeApi(retrofit: Retrofit): AssetTypeApi =
        retrofit.create(AssetTypeApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideEventApi(retrofit: Retrofit): EventApi =
        retrofit.create(EventApi::class.java)

    @Provides
    @Singleton
    fun provideFiredepartmentApi(retrofit: Retrofit): FiredepartmentApi =
        retrofit.create(FiredepartmentApi::class.java)

    @Provides
    @Singleton
    fun provideFirefighterActivityApi(retrofit: Retrofit): FirefighterActivityApi =
        retrofit.create(FirefighterActivityApi::class.java)

    @Provides
    @Singleton
    fun provideFirefighterActivityTypeApi(retrofit: Retrofit): FirefighterActivityTypeApi =
        retrofit.create(FirefighterActivityTypeApi::class.java)

    @Provides
    @Singleton
    fun provideFirefighterApi(retrofit: Retrofit): FirefighterApi =
        retrofit.create(FirefighterApi::class.java)

    @Provides
    @Singleton
    fun provideInspectionApi(retrofit: Retrofit): InspectionApi =
        retrofit.create(InspectionApi::class.java)

    @Provides
    @Singleton
    fun provideInspectionTypeApi(retrofit: Retrofit): InspectionTypeApi =
        retrofit.create(InspectionTypeApi::class.java)

    @Provides
    @Singleton
    fun provideInvestmentProposalApi(retrofit: Retrofit): InvestmentProposalApi =
        retrofit.create(InvestmentProposalApi::class.java)

    @Provides
    @Singleton
    fun provideOperationApi(retrofit: Retrofit): OperationApi =
        retrofit.create(OperationApi::class.java)

    @Provides
    @Singleton
    fun provideOperationTypeApi(retrofit: Retrofit): OperationTypeApi =
        retrofit.create(OperationTypeApi::class.java)

    @Provides
    @Singleton
    fun provideVoteApi(retrofit: Retrofit): VoteApi =
        retrofit.create(VoteApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)
}