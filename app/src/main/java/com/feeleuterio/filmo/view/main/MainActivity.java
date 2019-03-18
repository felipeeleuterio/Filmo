package com.feeleuterio.filmo.view.main;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.feeleuterio.filmo.R;
import com.feeleuterio.filmo.api.model.Images;
import com.feeleuterio.filmo.api.model.Movie;
import com.feeleuterio.filmo.view.App;
import com.feeleuterio.filmo.view.detail.DetailActivity;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.feeleuterio.filmo.view.detail.DetailActivity.MOVIE_ID;
import static com.feeleuterio.filmo.view.detail.DetailActivity.MOVIE_TITLE;

public class MainActivity extends AppCompatActivity implements
        MainContract.View,
        SwipeRefreshLayout.OnRefreshListener, EndlessScrollListener.ScrollToBottomListener,
        MoviesAdapter.ItemClickListener {
    private static final String TAG = "Main";

    @Inject
    MainPresenter presenter;
    @BindView(R.id.mainToolbar)
    AppBarLayout toolbar;
    @BindView(R.id.findMoviesEditText)
    EditText editText;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView contentView;
    @BindView(R.id.textView)
    View errorView;
    @BindView(R.id.progressBar)
    View loadingView;

    private MoviesAdapter moviesAdapter;
    private EndlessScrollListener endlessScrollListener;
    private Images images;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupContentView();
        setupFindMovieView();

        DaggerMainComponent.builder()
                .appComponent(App.getAppComponent(getApplication()))
                .mainModule(new MainModule(this))
                .build()
                .inject(this);

    }

    private void setupContentView() {
        imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 100);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager, this);
        contentView.setLayoutManager(linearLayoutManager);
        contentView.addOnScrollListener(endlessScrollListener);
    }

    private void setupFindMovieView(){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editText.setCursorVisible(false);
                imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        endlessScrollListener.onRefresh();
        presenter.onPullToRefresh();
    }

    @Override
    public void onScrollToBottom() {
        presenter.onScrollToBottom();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void showLoading(boolean isRefresh) {
        if (isRefresh) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        } else {
            loadingView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showContent(List<Movie> movies, boolean isRefresh) {
        if (moviesAdapter == null) {
            moviesAdapter = new MoviesAdapter(movies, this, images, this);
            contentView.setAdapter(moviesAdapter);
        } else {
            if (isRefresh) {
                moviesAdapter.clear();
            }
            moviesAdapter.addAll(movies);
            moviesAdapter.notifyDataSetChanged();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);

        loadingView.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        swipeRefreshLayout.setRefreshing(false);
        toolbar.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationSet(Images images) {
        this.images = images;

        if (moviesAdapter != null) {
            moviesAdapter.setImages(images);
        }
    }

    @Override
    public void onItemClick(int movieId, String movieTitle) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(MOVIE_ID, movieId);
        i.putExtra(MOVIE_TITLE, movieTitle);
        startActivity(i);
    }

    @OnClick(R.id.textView)
    void onClickErrorView() {
        presenter.start();
    }

}
