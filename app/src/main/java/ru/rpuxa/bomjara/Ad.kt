package ru.rpuxa.bomjara

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener

class Ad(val context: Context, val id: String) : RewardedVideoAdListener {
    private val videoAd = MobileAds.getRewardedVideoAdInstance(context).apply {
        rewardedVideoAdListener = this@Ad
    }!!
    private lateinit var listener: () -> Unit

    init {
        load()
    }

    override fun onRewardedVideoAdClosed() {
        load()
    }

    override fun onRewardedVideoAdLeftApplication() {
    }

    override fun onRewardedVideoAdLoaded() {
    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoCompleted() {
    }

    override fun onRewarded(p0: RewardItem?) {
        listener()
    }

    override fun onRewardedVideoStarted() {
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
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

    fun destroy() {
        videoAd.destroy(context)
    }

    fun pause() {
        videoAd.pause(context)
    }

    fun resume() {
        videoAd.resume(context)
    }
}