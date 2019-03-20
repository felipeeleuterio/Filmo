package com.feeleuterio.filmo.view;

import com.feeleuterio.filmo.api.ApiService;
import com.feeleuterio.filmo.api.model.Configuration;
import com.feeleuterio.filmo.api.model.Images;
import com.feeleuterio.filmo.api.model.Movie;
import com.feeleuterio.filmo.view.detail.DetailContract;
import com.feeleuterio.filmo.view.detail.DetailPresenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailPresenterTest {

    @Mock
    DetailContract.View view;

    @Mock
    ApiService apiService;

    private DetailPresenter presenter;
    private Movie movie;
    private Images images;
    private static int DUMMY_MOVIE_ID = 123;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new DetailPresenter(view, apiService);

        movie = JsonTestUtil.getJsonFromFile("movie.json", Movie.class);
        Configuration configuration = JsonTestUtil.getJsonFromFile("configuration.json", Configuration.class);
        images = configuration.images;

        when(apiService.getMovie(anyInt()))
                .thenReturn(RetrofitTestUtil.createCall(movie));
        when(apiService.getConfiguration())
                .thenReturn(RetrofitTestUtil.createCall(configuration));
    }

    private void makeGetMovieFail() {
        when(apiService.getMovie(anyInt()))
                .thenReturn(RetrofitTestUtil.createCall(500, new Movie()));
    }

    @Test
    public void onStartLoadsContent() {
        presenter.start(DUMMY_MOVIE_ID);

        verify(view).showLoading();
        verify(view).showContent(movie);
        verify(view).onConfigurationSet(images);
    }

    @Test
    public void onStartLoadsContentFail() {
        makeGetMovieFail();
        presenter.start(DUMMY_MOVIE_ID);

        verify(view).showLoading();
        verify(view).showError();
    }

}
