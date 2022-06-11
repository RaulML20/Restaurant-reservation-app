package com.example.androidroom.entities

import androidx.room.*

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey @ColumnInfo(name = "numberR") var numberR: Int,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "hour") var hour: Int,
    @ColumnInfo(name = "minute") var minute: Int,
    @ColumnInfo(name = "clientsN") var clientsN: Int,
    @ColumnInfo(name = "state") var state: String,
    @ColumnInfo(name = "idCreatorC") var idCreatorC: Int,
    @ColumnInfo(name = "idCreatorR") var idCreatorR: Int,
)

class ClientsWithReservations(
    @Embedded val client: ClientEntity,
    @Relation(
        parentColumn = "idC",
        entityColumn = "idCreatorC"
    )
    var reservations: List<ReservationEntity>
)

class RestaurantsWithReservations(
    @Embedded val restaurant: RestaurantEntity,
    @Relation(
        parentColumn = "idR",
        entityColumn = "idCreatorR"
    )
    var reservations: List<ReservationEntity>
)

