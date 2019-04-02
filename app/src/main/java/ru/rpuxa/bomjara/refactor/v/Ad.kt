package ru.rpuxa.bomjara.refactor.v

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener

class Ad(val context: Context, val id: String) : RewardedVideoAdListener {
    private val videoAd = MobileAds.getRewardedVideoAdInstance(context).apply {
        rewardedVideoAdListener = this@Ad
    }!!

    private var watched = false
    lateinit var listener: () -> Unit

    init {
        load()
    }

    override fun onRewardedVideoAdClosed() {
        if (watched)
            listener()
        load()
        watched = false
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

    fun show(listener: () -> Unit): Boolean {
        this.listener = listener
        if (!videoAd.isLoaded)
            return false
        videoAd.show()
        return true
    }

    private fun load() {
        videoAd.loadAd(id, AdRequest.Builder().build())
    }

    companion object {
        const val TAG = "adMobDebug"
    }
}
