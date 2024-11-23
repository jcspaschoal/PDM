package com.pdm.esas.init

import android.content.Context
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.pdm.esas.SocialEsasApplication
import com.pdm.esas.data.repository.UserRepository
import com.pdm.esas.utils.log.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseInit @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
) : Initializer {
    override suspend fun init() {
        recordUser()
        syncFcmToken()
    }

    private suspend fun recordUser() {
        userRepository.getCurrentUser()?.run {
            Firebase.crashlytics.setUserId(id)
            Firebase.analytics.setUserId(id)
            email?.let { Firebase.analytics.setUserProperty("Email", it) }
            email?.let { Firebase.analytics.setUserProperty("Name", it) }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun syncFcmToken() {
        if (!userRepository.getFirebaseTokenSent() && userRepository.userExists()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Logger.e(
                        SocialEsasApplication.TAG,
                        "Falha ao buscar o token de registro do FCM",
                        task.exception.toString()
                    )
                    return@addOnCompleteListener
                }
                task.result?.let {
                    GlobalScope.launch {
                        userRepository.setFirebaseToken(it)
                        userRepository.sendFirebaseToken(it)
                            .catch { e ->
                                Logger.record(e)
                            }.collect {
                                userRepository.setFirebaseTokenSent()
                            }
                    }
                    if (true) Logger.d(SocialEsasApplication.TAG, it)
                }
            }
        }
    }

}
