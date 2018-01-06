package com.tufei.architecturedemo.utils;

import com.tufei.architecturedemo.net.HttpResult;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author tufei
 * @date 2017/7/8
 */

public class RxUtil {

    public static <T> ObservableTransformer<T, T> io_main() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> all_io() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    /**
     * 1)实现了线程切换io->main
     * 2)用于预处理后台返回的json
     * 注意：
     * 如果对{@link HttpResult#data}不关心，即使data为空也无所谓的时候，
     * 只在乎网络请求的结果{@link HttpResult#success}，
     * 那么请用{@link #io_main_handleNoData()},因为RxJava不允许发送null
     *
     * 使用的时候，
     * 如果{@link HttpResult#data}是一串json，这么写：Observable<HttpResult<Bean>>
     * 如果{@link HttpResult#data}是一组json，这么写：Observable<HttpResult<List<Bean>>>
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<HttpResult<T>, T> io_main_handleHttpResult() {
        return httpResultObservable ->
                httpResultObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .flatMap(httpResult->{
                            if (httpResult.isSuccess()) {
                                return Observable.just(httpResult.getData());
                            } else {
                                return Observable.error(new Exception(httpResult.getErrmsg()));
                            }
                        });
    }

    /**
     * 使用的时候，这么写：Observable<HttpResult>
     * @return
     */
    public static ObservableTransformer<HttpResult, HttpResult> io_main_handleNoData() {
        return httpResultObservable ->
                httpResultObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                        flatMap(httpResult -> {
                            if (httpResult.isSuccess()) {
                                return Observable.just(httpResult);
                            } else {
                                return Observable.error(new Exception(httpResult.getErrmsg()));
                            }
                        });
    }

}
