package com.adcolony

import android.app.Activity
import android.view.ViewGroup
import com.adcolony.sdk.*
import com.adcolony.sdk.AdColony
import com.advertising.IAdvertisingSponsor

open class AdColony (
        private val APP_ID: String,
        private val ZONE_ID: String,
        private val activity: Activity,
        private val iAdColonyRewardListener: IAdColonyRewardListener) : IAdvertisingSponsor {

    override val isVideoSponsor: Boolean = true
    override val isOfferwallSponsor: Boolean = false
    override val isInterstitialSponsor: Boolean = false
    override val isBannerSponsor: Boolean = false

    private var mAdColonyInterstitial: AdColonyInterstitial? = null
    private var mAdColonyInterstitialListener: AdColonyInterstitialListener? = null
    private var mAdColonyRewardListener: AdColonyRewardListener? = null


    override fun init() {
        initListeners()
        AdColony.configure(activity, APP_ID, ZONE_ID)
        AdColony.setRewardListener(mAdColonyRewardListener!!)
        AdColony.requestInterstitial(ZONE_ID, mAdColonyInterstitialListener!!)
    }



    override fun isVideoAvailable(): Boolean {
        return if (isVideoSponsor) {
            (mAdColonyInterstitial != null)
        } else false
    }


    override fun showVideoPreRoll() {
        mAdColonyInterstitial?.show()
        mAdColonyInterstitial = null
        AdColony.requestInterstitial(ZONE_ID, mAdColonyInterstitialListener!!)
    }



    private fun initListeners() {
        mAdColonyInterstitialListener = object : AdColonyInterstitialListener() {
            override fun onRequestNotFilled(zone: AdColonyZone?) {
                super.onRequestNotFilled(zone)
            }

            override fun onRequestFilled(adColonyInterstitial: AdColonyInterstitial) {
                mAdColonyInterstitial = adColonyInterstitial
            }
        }

        mAdColonyRewardListener = AdColonyRewardListener { reward ->
            iAdColonyRewardListener.onReward(reward.rewardAmount.toLong())
        }
    }

    override fun showOfferWall() {}
    override fun showInterstitial() {}
    override fun isOfferwallAvailable()= false
    override fun isInterstitialAvailable() = false
    override fun setBannerView(bunner: ViewGroup) {}
}