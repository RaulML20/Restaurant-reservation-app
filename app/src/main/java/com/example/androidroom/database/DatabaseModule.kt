package com.example.androidroom.database

import android.content.Context
import androidx.room.Room
import com.example.androidroom.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideClients(clientsDatabase: ReservationsDatabase): ClientDAO {
        return clientsDatabase.clientDao()
    }

    @Provides
    fun provideReservation(reservationsDatabase: ReservationsDatabase): ReservationDAO {
        return reservationsDatabase.reservationDao()
    }

    @Provides
    fun provideRestaurant(restaurantsDatabase: ReservationsDatabase): RestaurantDAO {
        return restaurantsDatabase.restaurantDao()
    }

    @Provides
    fun provideDiscount(discountDatabase: ReservationsDatabase): DiscountDAO {
        return discountDatabase.discountDao()
    }

    @Provides
    fun provideReview(reviewDatabase: ReservationsDatabase): ReviewDAO {
        return reviewDatabase.reviewDao()
    }

    @Provides
    fun provideLocation(locationDatabase: ReservationsDatabase): LocationDAO {
        return locationDatabase.locationDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ReservationsDatabase {
        return Room.databaseBuilder(appContext,
            ReservationsDatabase::class.java,
            "RssReader"
        ).fallbackToDestructiveMigration().build()
    }
}