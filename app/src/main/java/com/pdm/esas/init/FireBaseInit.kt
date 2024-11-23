package com.pdm.esas.init

import android.content.Context
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.pdm.esas.SocialEsasApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
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
                        "Fetching FCM registration token failed",
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
                    if (BuildConfig.DEBUG) Logger.d(WimmApplication.TAG, it)
                }
            }
        }
    }

}
