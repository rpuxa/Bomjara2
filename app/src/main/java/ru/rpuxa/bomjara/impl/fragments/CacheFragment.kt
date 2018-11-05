package ru.rpuxa.bomjara.impl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class CacheFragment : Fragment() {
    private var cacheView: View? = null
    abstract val layout: Int
    protected var changed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cacheView = cacheView ?: inflater.inflate(layout, container, false)
        return cacheView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (changed)
            return
        onChange(view)
        changed = true
    }

    abstract fun onChange(view: View)
}