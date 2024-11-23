package com.pdm.esas.di.module

import com.pdm.esas.di.qualifier.ScopeIO
import com.pdm.esas.utils.coroutine.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ScopeIO
    @ServiceScoped
    fun provideIOCoroutineScope(dispatcher: Dispatcher) =
        CoroutineScope(dispatcher.io() + SupervisorJob())

}