package com.pluscubed.velociraptor.api.osm

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText

class OsmHttpResult(
    val status: Int,
    val body: String
)

class OsmApiEndpoint(
    private val client: HttpClient,
    val baseUrl: String
) : Comparable<OsmApiEndpoint> {
    var timeTaken: Int = 0

    suspend fun getOsm(data: String): OsmHttpResult {
        val response = client.post("interpreter") {
            setBody(data)
        }
        return OsmHttpResult(response.status.value, response.bodyAsText())
    }

    override fun toString(): String {
        val time = when (timeTaken) {
            Int.MAX_VALUE -> "error"
            0 -> "pending"
            else -> timeTaken.toString() + "ms"
        }
        return this.baseUrl + " - " + time
    }

    override fun compareTo(other: OsmApiEndpoint): Int {
        return timeTaken.compareTo(other.timeTaken)
    }
}
