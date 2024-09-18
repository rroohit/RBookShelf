package roh.book.shelf.domain.mapper

import roh.book.shelf.data.local.entities.UserEntity
import roh.book.shelf.domain.model.UserDetails

fun UserDetails.toUserEntity(): UserEntity {
    return UserEntity(
        name = name,
        email = email,
        password = password,
        country = country
    )
}

fun UserEntity.toUserDetails(): UserDetails {
    return UserDetails(
        name = name,
        email = email,
        password = password,
        country = country
    )
}