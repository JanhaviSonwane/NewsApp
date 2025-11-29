package com.example.newsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * An annotation used to mark the Application class for Hilt dependency injection.
 * This annotation triggers Hilt's code generation for the application's component.
 */
@HiltAndroidApp  // Marks this Application class as the Hilt application entry point
class App : Application()  // Custom Application class that extends the default Android Application class