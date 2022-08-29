package jp.ac.it_college.std.s21010.hotpepperapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wikiclient.ui.ArticleListAdapter
import com.example.wikiclient.ui.MainViewModel
import com.example.wikiclient.ui.model.Article
import jp.ac.it_college.std.s21010.hotpepperapi.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val articleListAdapter = ArticleListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initArticleList()
        observeArticles()
        initButton()

        viewModel.fetchArticles("Android", 20)
    }

    private fun initButton() {
        binding.button.setOnClickListener {
            // 検索ボタンクリック時の処理
            val formText = binding.form.text.toString() // フォームに入力されている値を取得
            if (formText.isNotEmpty()) {
                viewModel.fetchArticles(formText, 20) // フォームの値を元にWeb APIから値を取得
            }
        }
    }

    // RecyclerViewの初期設定
    private fun initArticleList() {
        binding.articleList.apply {
            adapter = articleListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun updateArticleList() {
        val articles: List<Article> = (0..10).map { number ->
            Article(
                number,
                "記事$number",
                "https://s3-ap-northeast-1.amazonaws.com/qiita-image-store/0/42d9537951bfbf445bfff027956eb920b6079d73/x_large.png?1585895165"
            )
        }
        articleListAdapter.submitList(articles)

    }

    private fun observeArticles() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articles.collect { articles ->
                    // Web APIからのデータ取得に成功すると、ここに流れてくる
                    updateArticleList(articles)
                }
            }
        }
    }
    private fun updateArticleList(articles: List<Article>) {
        articleListAdapter.submitList(articles)
    }
}