package com.example.androidroom.entities

import androidx.room.*

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey @ColumnInfo(name = "idR") var idR: Int,
    @ColumnInfo(name = "comentary") var comentary: String,
    @ColumnInfo(name = "punctuation") var punctuation: Int,
    @ColumnInfo(name = "idReviewC") var idReviewC: Int,
    @ColumnInfo(name = "idReviewR") var idReviewR: Int,
)

class ClientsWithReviews(
    @Embedded val client: ClientEntity,
    @Relation(
        parentColumn = "idC",
        entityColumn = "idReviewC"
    )
    var reviews: List<ReviewEntity>
)

class RestaurantsWithReviews(
    @Embedded val restaurant: RestaurantEntity,
    @Relation(
        parentColumn = "idR",
        entityColumn = "idReviewR"
    )
    var reviews: List<ReviewEntity>
)


