package com.example.android.hilt.di

import android.content.Context
import androidx.room.Room
import com.example.android.hilt.data.AppDatabase
import com.example.android.hilt.data.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
	
	@Provides
	fun provideLogDao(dataBase: AppDatabase): LogDao = dataBase.logDao()
	
	@Provides
	@Singleton
	fun provideDataBase(@ApplicationContext appContext: Context): AppDatabase =
		Room.databaseBuilder(
			appContext,
			AppDatabase::class.java,
			"logging.db"
		).build()
}