package com.feeleuterio.filmo.view.detail;

import com.feeleuterio.filmo.api.model.Images;
import com.feeleuterio.filmo.api.model.Movie;

public interface DetailContract {

    interface View {

        void showLoading();

        void showContent(Movie movie);

        void showError();

        void onConfigurationSet(Images images);

    }

    interface Presenter {

        void start(int movieId);

        void finish();

    }

}
