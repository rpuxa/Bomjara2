package ru.rpuxa.bomjara.refactor.v.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class CacheFragment : Fragment() {
    private var cacheView: View? = null
    abstract val layout: Int
    private var changed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cacheView = cacheView ?: inflater.inflate(layout, container, false)
        return cacheView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onPreChange()
        if (changed)
            return
        onChange(view)
        changed = true
    }

    protected open fun onPreChange() {
    }

    abstract fun onChange(view: View)
}