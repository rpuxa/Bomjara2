package ru.rpuxa.bomjara.refactor.v

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener

class Ad(val context: Context, val id: String) : RewardedVideoAdListener, LifecycleObserver {
    private val videoAd: RewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context).apply {
        rewardedVideoAdListener = this@Ad
    }

    private var watched = false
    private var listener: (() -> Unit)? = null

    init {
        load()
    }

    override fun onRewardedVideoAdClosed() {
        if (watched)
            listener!!()
        load()
        watched = false
        listener = null
    }

    override fun onRewardedVideoAdLeftApplication() {
        Log.d(TAG, "onRewardedVideoAdLeftApplication")
    }

    override fun onRewardedVideoAdLoaded() {
        Log.d(TAG, "onRewardedVideoAdLoaded")
    }

    override fun onRewardedVideoAdOpened() {
        Log.d(TAG, "onRewardedVideoAdOpened")
    }

    override fun onRewardedVideoCompleted() {
        Log.d(TAG, "onRewardedVideoCompleted")
    }

    override fun onRewarded(p0: RewardItem?) {
        watched = true
        Log.d(TAG, "onRewarded")
    }

    override fun onRewardedVideoStarted() {
        Log.d(TAG, "onRewardedVideoStarted")
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        Log.d(TAG, "onRewardedVideoAdFailedToLoad  $p0")
        load()
    }

    fun show(owner: Activity, listener: () -> Unit): Boolean {
        if (!videoAd.isLoaded)
            return false
        this.listener = listener
        (owner as LifecycleOwner).lifecycle.addObserver(this)
        videoAd.show()
        return true
    }

    private fun load() {
        videoAd.loadAd(id, AdRequest.Builder().build())
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun pause() {
        videoAd.pause(context)
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun resume() {
        videoAd.resume(context)
    }

    companion object {
        const val TAG = "adMobDebug"
    }
}
