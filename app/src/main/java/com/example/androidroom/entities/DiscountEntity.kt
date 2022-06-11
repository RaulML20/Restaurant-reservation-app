package com.example.androidroom.entities

import androidx.room.*

@Entity(tableName = "discounts")
data class DiscountEntity(
    @PrimaryKey @ColumnInfo(name = "idD") var idD: Int,
    @ColumnInfo(name = "porcentage") var porcentage: Double,
    @ColumnInfo(name = "idDiscountC") var idDiscountC: Int?,
    @ColumnInfo(name = "idDiscountR") var idDiscountR: Int,
)

class ClientsWithDiscounts(
    @Embedded val client: ClientEntity,
    @Relation(
        parentColumn = "idC",
        entityColumn = "idDiscountC"
    )
    var discounts: List<DiscountEntity>
)

class RestaurantsWithDiscounts(
    @Embedded val restaurant: RestaurantEntity,
    @Relation(
        parentColumn = "idR",
        entityColumn = "idDiscountR"
    )
    var discounts: List<DiscountEntity>
)


