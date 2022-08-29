package com.example.wikiclient.data.model

import android.app.DownloadManager
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer


data class GetArticlesResponse(
    val query: Query,
) {
    data class Query(
        val pages: List<Page>,
    ) {
        data class Page(
            val index: Int,
            val ns: Int,
            val pageid: Int,
            val terms: Terms?,
            val thumbnail: Thumbnail?,
            val title: String,
        ) {
            data class Terms(
                val description: List<String>,
            )

            data class Thumbnail(
                val height: Int,
                val source: String,
                val width: Int,
            )
        }
    }
}
