package com.example.wikiclient.data

import com.example.wikiclient.data.model.GetArticlesResponse
import com.example.wikiclient.data.remote.WikiApiClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface WikiRepository {
    suspend fun getArticles(keyword: String, limit: Int): GetArticlesResponse?
}

class WikiRepositoryImpl : WikiRepository {

    // JSONからKotlinのクラスに変換するためのライブラリの設定
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // REST APIを利用するためのライブラリの設定
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ja.wikipedia.org/") // 今回利用するWeb APIのURL
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // WikiApiClientに定義したメソッドを呼び出すための設定
    private val wikiService = retrofit.create(WikiApiClient::class.java)

    // Web APIからデータを取得するメソッド
    override suspend fun getArticles(keyword: String, limit: Int): GetArticlesResponse? {
        return wikiService.getArticleList(keyword, limit).body()
    }
}