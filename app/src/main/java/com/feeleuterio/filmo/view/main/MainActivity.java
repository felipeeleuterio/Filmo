package com.feeleuterio.filmo.view.main;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import static com.feeleuterio.filmo.view.detail.DetailActivity.MOVIE_ID;
import static com.feeleuterio.filmo.view.detail.DetailActivity.MOVIE_TITLE;

public class MainActivity extends AppCompatActivity implements
        MainContract.View,
        SwipeRefreshLayout.OnRefreshListener, EndlessScrollListener.ScrollToBottomListener,
        MoviesAdapter.ItemClickListener {
    private static final String TAG = "Main";

    @Inject
    MainPresenter presenter;
    @BindView(R.id.mainActivity)
    CoordinatorLayout layout;
    @BindView(R.id.mainToolbar)
    AppBarLayout toolbar;
    @BindView(R.id.findMoviesEditText)
    EditText editText;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView contentView;
    @BindView(R.id.progressBar)
    View loadingView;

    private MoviesAdapter moviesAdapter;
    private EndlessScrollListener endlessScrollListener;
    private GridLayoutManager gridLayoutManager;
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
        swipeRefreshLayout.setProgressViewOffset(false, 0, 136);

        gridLayoutManager = new GridLayoutManager(this, 2);
        endlessScrollListener = new EndlessScrollListener(gridLayoutManager, this);
        contentView.setLayoutManager(gridLayoutManager);
        contentView.addOnScrollListener(endlessScrollListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupFindMovieView() {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_RIGHT = 2;

                editText.setCursorVisible(true);

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() <= (editText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        onTouchFindMovieView(true);
                        return true;
                    }
                    else if(motionEvent.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        onTouchFindMovieView(false);
                        return true;
                    }
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                onTouchFindMovieView(false);
                return false;
            }
        });
    }

    private void onTouchFindMovieView(boolean onGenericBackPressed) {
        editText.setCursorVisible(false);
        imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (!getQuery().isEmpty() && (!onGenericBackPressed)) {
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_back_black_48dp, 0, R.drawable.search_black_48dp, 0);
        }
        else {
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.camera_black_48dp, 0, R.drawable.search_black_48dp, 0);
            editText.getText().clear();
            toolbar.setExpanded(true);
        }
        presenter.start(getQuery());
        if((onGenericBackPressed) || (!getQuery().isEmpty()))
            gridLayoutManager.smoothScrollToPosition(contentView, null, 0);
    }

    private String getQuery() {
        return editText.getText().toString();
    }

    @Override
    public void onBackPressed() {
        if(!getQuery().isEmpty())
            onTouchFindMovieView(true);
        else
            super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        endlessScrollListener.onRefresh();
        presenter.onPullToRefresh(getQuery());
    }

    @Override
    public void onScrollToBottom() {
        presenter.onScrollToBottom(getQuery());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start(getQuery());
    }

    @Override
    public void showLoading(boolean isRefresh) {
        if (isRefresh) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
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
    }

    @Override
    public void showError() {
        swipeRefreshLayout.setRefreshing(false);
        loadingView.setVisibility(View.GONE);
        Snackbar.make(layout, getString(R.string.generic_error), Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

}
