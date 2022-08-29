package com.example.wikiclient.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikiclient.data.WikiRepository
import com.example.wikiclient.data.WikiRepositoryImpl
import com.example.wikiclient.data.model.GetArticlesResponse
import com.example.wikiclient.ui.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: WikiRepository = WikiRepositoryImpl()
) : ViewModel() {

    // 取得したデータを別クラスに通知するためのクラス
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private var job: Job? = null

    fun fetchArticles(keyword: String, limit: Int) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getArticles(keyword, limit)
            }.onSuccess { response ->
                // データ取得成功時
                if (response != null) postArticles(response)
            }.onFailure {
                // データ取得失敗時
                Log.e("Fetch Failure", it.toString())
            }
        }
    }


    // 取得したデータをMainActivityに通知する
    private fun postArticles(response: GetArticlesResponse) {
        _articles.value = response.query.pages.map {
            Article(
                id = it.pageid,
                title = it.title,
                thumbnailUrl = it.thumbnail?.source ?: "",
            )
        }
    }
}
