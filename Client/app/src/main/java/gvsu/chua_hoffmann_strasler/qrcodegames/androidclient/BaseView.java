package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

/**
 * Basic interface for all views.
 *
 * @param <T>
 */
public interface BaseView<T> {
    /**
     * Sets basic presenter.
     *
     * @param presenter presenter to be set
     */
    void setPresenter(T presenter);
}
