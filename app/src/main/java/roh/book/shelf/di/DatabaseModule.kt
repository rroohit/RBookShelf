package roh.book.shelf.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import roh.book.shelf.data.local.BookShelfDatabase
import roh.book.shelf.data.local.dao.BooksDao
import roh.book.shelf.data.local.dao.UserAuthDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): BookShelfDatabase {
        return Room.databaseBuilder(
            appContext,
            BookShelfDatabase::class.java,
            "BookShelfDatabase"
        ).build()
    }

    @Provides
    fun provideUserAuthDao(database: BookShelfDatabase): UserAuthDao {
        return database.userAuthDao()
    }

    @Provides
    fun provideBooksDao(database: BookShelfDatabase): BooksDao {
        return database.booksDao()
    }

}