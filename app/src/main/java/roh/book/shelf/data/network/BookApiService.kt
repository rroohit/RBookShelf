package roh.book.shelf.data.network

import retrofit2.http.GET
import roh.book.shelf.domain.model.BookApiResponse
import roh.book.shelf.domain.model.CountryApiResponse

interface BookApiService {
    @GET("CNGI")
    suspend fun getBooks(): List<BookApiResponse>

    @GET("IU1K")
    suspend fun getCountries(): List<CountryApiResponse>
}