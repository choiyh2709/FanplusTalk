package com.fanplus.fanplustalk

import androidx.multidex.MultiDexApplication
import io.talkplus.TalkPlus

class MultiDexApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        TalkPlus.init(applicationContext, "84b54dae-b376-4408-872e-85c1eee91d46")
    }
}