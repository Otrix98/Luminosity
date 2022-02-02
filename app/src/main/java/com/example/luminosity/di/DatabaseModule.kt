package com.example.luminosity.di

import android.app.Application
import androidx.room.Room
import com.example.luminosity.db.UnsplashDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

@Provides
@Singleton
fun providesDatabase(application: Application): UnsplashDatabase {
    return Room.databaseBuilder(
        application,
        UnsplashDatabase::class.java,
        UnsplashDatabase.DB_NAME
    )
        .build()
}
}