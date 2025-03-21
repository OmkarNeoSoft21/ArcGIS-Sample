package com.bm.arcgis_sample.domain.di

import android.content.Context
import com.bm.arcgis_sample.data.local.AppDatabase
import com.bm.arcgis_sample.data.remote.ApiService
import com.bm.arcgis_sample.data.remote.NetworkClient
import com.bm.arcgis_sample.data.repository.feature_geocode.RemoteDataSourceGeocode
import com.bm.arcgis_sample.data.repository.feature_saved_address.LocalDataSourceSavedAddress
import com.bm.arcgis_sample.domain.utill.NetworkObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return androidx.room.Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "arcgis.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideNetworkObserver(@ApplicationContext context: Context): NetworkObserver {
        return NetworkObserver(context)
    }

    @Singleton
    @Provides
    fun provideRemoteNetworkClient(): ApiService {
        return NetworkClient.apiService
    }

    @Singleton
    @Provides
    fun provideTasksRemoteDataSourceGeoCode(remote: ApiService): RemoteDataSourceGeocode {
        return RemoteDataSourceGeocode(remote)
    }

    @Singleton
    @Provides
    fun provideTasksLocalDataSourceSavedAddress(appDatabase: AppDatabase): LocalDataSourceSavedAddress {
        return LocalDataSourceSavedAddress(appDatabase)
    }
}