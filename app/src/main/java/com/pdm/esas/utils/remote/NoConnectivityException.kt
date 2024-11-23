package com.pdm.esas.utils.remote

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String = "No internet connection"
}