package ru.rpuxa.bomjara.fragments

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
import ru.rpuxa.bomjara.MONTHS
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.server.Server
import ru.rpuxa.bomjara.toast
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
            }
        }
    }

    inner class NewsAdapter : RecyclerView.Adapter<NewsAdapter.Holder>() {
        private var newsCount = 1
        private val news = ArrayList<CNews>()

        init {
            Server.send(Server.NEWS_COUNT) {
                activity.runOnUiThread {
                    if (it == null) {
                        toast("Сервер недоступен")
                    } else {
                        newsCount = it.data as Int
                    }
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
            if (position < news.size) {
                holder.apply {
                    val new = news[position]
                    view.showNext()
                    text.text = new.text
                    val s = "${new.day} ${MONTHS[new.month - 1]} ${new.year}"
                    date.text = s
                }
            }
            if (position == news.size)
                loadNews(news.size)
        }

        fun add(text: String, day: Int, month: Int, year: Int) {
            news.add(CNews(text, day, month, year))
            notifyItemChanged(news.size - 1)
        }

        private fun loadNews(position: Int) = Thread {
            Server.send(Server.GET_NEWS, position) {
                activity.runOnUiThread {
                    if (it == null) {
                        toast("Сервер недоступен")
                        return@runOnUiThread
                    }
                    val n = it.data as News
                    val date = n.date
                    news.add(position, CNews(n.text, (date shr 21) and 0b1111111, (date shr 14) and 0b1111111, date and 0b11111111111111))
                    notifyItemChanged(position)
                }
            }
        }.start()
    }

    private class CNews(val text: String, val day: Int, val month: Int, val year: Int)

}