package com.example.android.hilt.di

import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.data.LoggerInMemoryDataSource
import com.example.android.hilt.data.LoggerLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DatabaseLogger

@Qualifier
annotation class InMemoryLogger

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggingDatabaseModule {
	
	@Binds
	@Singleton
	@DatabaseLogger
	abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource
}

@Module
@InstallIn(ActivityComponent::class)
abstract class LoggingInMemoryModule {

	@Binds
	@ActivityScoped
	@InMemoryLogger
	abstract fun bindImMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}