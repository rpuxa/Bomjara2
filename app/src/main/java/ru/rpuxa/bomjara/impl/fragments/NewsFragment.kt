/*
package ru.rpuxa.bomjara.impl.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import kotlinx.android.synthetic.main.news_fragment.view.*
import kotlinx.android.synthetic.main.news_item.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.server.Server
import ru.rpuxa.bomjara.CurrentData.server
import ru.rpuxa.bomjserver.News
import kotlin.math.min


class NewsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.news_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fun View.setAdapter() {
            news_list.layoutManager = LinearLayoutManager(context)
            news_list.adapter = NewsAdapter()
        }
        view.apply {
            setAdapter()
            news_refresh.setOnRefreshListener {
                setAdapter()
                news_refresh.isRefreshing = false
            }
        }
    }

    inner class NewsAdapter : RecyclerView.Adapter<NewsAdapter.Holder>() {
        private var newsCount = 1
        private val news = HashMap<Int, NewsItem>()

        init {
            server.send(Server.NEWS_COUNT)
                    .onCommand {
                        newsCount = it as Int
                    }
                    .onError {
                        activity?.runOnUiThread {
                            toast("Сервер недоступен")
                        }
                    }
        }

        inner class Holder(val view: ViewSwitcher) : RecyclerView.ViewHolder(view) {
            val text = view.news_text!!
            val date = view.news_date!!
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
            val view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false) as ViewSwitcher
            return Holder(view)
        }

        override fun getItemCount() = min(news.size + 1, newsCount)

        override fun onBindViewHolder(holder: Holder, position: Int) {
            if (position < news.size) holder.apply {
                val new = news[position]!!
                text.text = new.text
                val s = "${new.day} ${MONTHS[new.month - 1]} ${new.year}"
                date.text = s
                if (holder.view.nextView === holder.view.getChildAt(1)) {
                    view.showNext()
                }
            }
            if (position == news.size)
                loadNews(position)
        }

        private fun loadNews(position: Int) {
            if (news[position] != null)
                return
            server.send(Server.GET_NEWS, position)
                    .onCommand {
                        val n = it as News
                        val date = n.date
                        news[position] = NewsItem(n.text, (date shr 21) and 0b1111111, (date shr 14) and 0b1111111, date and 0b1111111_1111111)
                        activity?.runOnUiThread {
                            notifyItemChanged(position)
                        }
                    }
                    .onError {
                        activity?.runOnUiThread {
                            toast("Сервер недоступен")
                        }
                    }
        }
    }

    private class NewsItem(val text: String, val day: Int, val month: Int, val year: Int)
}*/
