package com.anenigmatic.apogee19.di.shared

import com.anenigmatic.apogee19.di.events.EventsComponent
import com.anenigmatic.apogee19.di.login.LoginComponent
import com.anenigmatic.apogee19.di.profile.ProfileComponent
import com.anenigmatic.apogee19.di.tickets.TicketsComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun newEventsComponent(): EventsComponent

    fun newLoginComponent(): LoginComponent

    fun newProfileComponent(): ProfileComponent

    fun newTicketsComponent(): TicketsComponent
}