package com.devapps.pamwezi.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.devapps.pamwezi.PaMwezyApplication
import com.devapps.pamwezi.domain.database.BudgetDatabase
import com.devapps.pamwezi.domain.repository.BudgetRepository
import com.devapps.pamwezi.domain.repository.BudgetRepositoryImpl
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePaMwezyApplication(): PaMwezyApplication {
        return PaMwezyApplication()
    }


    @Provides
    @Singleton
fun provideBudgetDatabase(app: Application): BudgetDatabase {
    return Room.databaseBuilder(
        app,
        BudgetDatabase::class.java,
        "budget_database"
    ).build()
}

    @Provides
    @Singleton
    fun provideSignInClient(application: Application): SignInClient {
        return Identity.getSignInClient(application)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideBudgetRepository(db: BudgetDatabase) : BudgetRepository {
        return BudgetRepositoryImpl(db.budgetDao())
    }
}