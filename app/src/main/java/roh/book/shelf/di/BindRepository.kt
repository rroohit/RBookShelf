package roh.book.shelf.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import roh.book.shelf.data.repository.BooksRepositoryImpl
import roh.book.shelf.data.repository.UserAuthRepositoryImpl
import roh.book.shelf.domain.repository.BooksRepository
import roh.book.shelf.domain.repository.UserAuthRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class BindRepository {
    @Binds
    abstract fun provideUserAuthRepository(
        userAuthRepositoryImpl: UserAuthRepositoryImpl
    ): UserAuthRepository

    @Binds
    abstract fun provideBooksRepository(
        booksRepositoryImpl: BooksRepositoryImpl
    ): BooksRepository
}