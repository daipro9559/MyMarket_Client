package com.example.dainv.mymarket.ui.common


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.transition.AutoTransition
import android.support.transition.ChangeBounds
import android.support.transition.Fade
import android.support.transition.Slide
import android.support.transition.TransitionManager

import android.support.transition.TransitionSet
import android.transition.Transition
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

import com.example.dainv.mymarket.util.Util


import javax.inject.Inject

import timber.log.Timber

class AnimationImplement @Inject
constructor(private val context: Context) {

    fun animationScaleClick(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.9f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.9f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.interpolator = LinearInterpolator()
        scaleX.duration = 100
        scaleY.duration = 100
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.start()
    }
}
