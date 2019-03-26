package com.anenigmatic.apogee19.di.shared

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.anenigmatic.apogee19.screens.events.data.EventRepository
import com.anenigmatic.apogee19.screens.events.data.EventRepositoryImpl
import com.anenigmatic.apogee19.screens.events.data.retrofit.EventsApi
import com.anenigmatic.apogee19.screens.events.data.room.EventsDao
import com.anenigmatic.apogee19.screens.events.data.storage.FilterStorage
import com.anenigmatic.apogee19.screens.events.data.storage.FilterStorageImpl
import com.anenigmatic.apogee19.screens.shared.data.UserRepository
import com.anenigmatic.apogee19.screens.shared.data.UserRepositoryImpl
import com.anenigmatic.apogee19.screens.shared.data.firebase.UserWatcher
import com.anenigmatic.apogee19.screens.shared.data.firebase.UserWatcherImpl
import com.anenigmatic.apogee19.screens.shared.data.retrofit.BaseInterceptor
import com.anenigmatic.apogee19.screens.shared.data.retrofit.SigningTypeAdapter
import com.anenigmatic.apogee19.screens.shared.data.retrofit.UserApi
import com.anenigmatic.apogee19.screens.shared.data.room.AppDatabase
import com.anenigmatic.apogee19.screens.shared.data.storage.UserStorage
import com.anenigmatic.apogee19.screens.shared.data.storage.UserStorageImpl
import com.anenigmatic.apogee19.screens.shared.util.NetworkDetails
import com.anenigmatic.apogee19.screens.tickets.data.TicketRepository
import com.anenigmatic.apogee19.screens.tickets.data.TicketRepositoryImpl
import com.anenigmatic.apogee19.screens.tickets.data.retrofit.ComboTicketTypeAdapter
import com.anenigmatic.apogee19.screens.tickets.data.retrofit.TicketsApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun providesEventRepository(filterStorage: FilterStorage, eventsDao: EventsDao, eventsApi: EventsApi, networkDetails: NetworkDetails): EventRepository {
        return EventRepositoryImpl(filterStorage, eventsDao, eventsApi, networkDetails)
    }

    @Singleton
    @Provides
    fun providesTicketRepository(ticketsApi: TicketsApi, userRepository: UserRepository): TicketRepository {
        return TicketRepositoryImpl(ticketsApi, userRepository)
    }

    @Singleton
    @Provides
    fun providesUserRepository(userStorage: UserStorage, userApi: UserApi, userWatcher: UserWatcher): UserRepository {
        return UserRepositoryImpl(userStorage, userApi, userWatcher)
    }

    @Singleton
    @Provides
    fun providesUserWatcher(): UserWatcher {
        return UserWatcherImpl()
    }

    @Singleton
    @Provides
    fun providesEventsApi(retrofit: Retrofit): EventsApi {
        return retrofit.create(EventsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesTicketsApi(retrofit: Retrofit): TicketsApi {
        return retrofit.create(TicketsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesEventsDao(appDatabase: AppDatabase): EventsDao {
        return appDatabase.getEventsDao()
    }

    @Singleton
    @Provides
    fun providesFilterStorage(sharedPreferences: SharedPreferences): FilterStorage {
        return FilterStorageImpl(sharedPreferences)
    }

    @Singleton
    @Provides
    fun providesUserStorage(sharedPreferences: SharedPreferences): UserStorage {
        return UserStorageImpl(sharedPreferences)
    }

    @Singleton
    @Provides
    fun providesNetworkDetails(application: Application): NetworkDetails {
        return NetworkDetails(application)
    }

    @Singleton
    @Provides
    fun providesProfileWatcher(): UserWatcherImpl {
        return UserWatcherImpl()
    }

    @Singleton
    @Provides
    fun providesRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://test.bits-apogee.org/")
            .client(OkHttpClient().newBuilder().addInterceptor(BaseInterceptor()).build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(SigningTypeAdapter())
            .add(ComboTicketTypeAdapter())
            .build()
    }

    @Singleton
    @Provides
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "apogee.db").build()
    }

    @Singleton
    @Provides
    fun providesSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("apogee.sp", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providesApplication(): Application {
        return application
    }
}