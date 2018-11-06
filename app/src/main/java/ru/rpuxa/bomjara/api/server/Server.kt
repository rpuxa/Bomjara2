package ru.rpuxa.bomjara.api.server

interface Server {

    fun send(id: Int, data: Any? = null): ServerToken

    companion object {

        const val EMPTY_MESSAGE = -1
        const val STATISTIC = 0
        const val REVIEW = 1
        const val NEWS_COUNT = 2
        const val GET_NEWS = 3
        const val GET_CACHED_ACTIONS = 4

        const val ACTIONS = 0
        const val LOCATIONS = 1
        const val FRIENDS = 2
        const val TRANSPORTS = 3
        const val HOMES = 4
        const val COURSES = 5
        const val HASH = 6
    }
}