package org.chromium.hat.utils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Floatingmuseum on 2017/3/1.
 */

public class RxUtil {

    private static ObservableTransformer schedulerTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * 子线程/主线程切换
     */
    public static <T> ObservableTransformer<T, T> threadSwitch() {
        return (ObservableTransformer<T, T>) schedulerTransformer;
    }

    /**
     * 子线程/主线程切换
     */
    public static <T> ObservableTransformer<T, T> threadSwitchs() {
        return (ObservableTransformer<T, T>) schedulerTransformer;
    }

    private static FlowableTransformer FlowableSchedulerTransformer = new FlowableTransformer() {
        @Override
        public Publisher apply(Flowable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * 子线程/主线程切换
     */
    public static <T> FlowableTransformer<T, T> flowableThreadSwitch() {
        return (FlowableTransformer<T, T>) FlowableSchedulerTransformer;
    }
}
