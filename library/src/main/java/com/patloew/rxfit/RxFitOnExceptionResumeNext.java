package com.patloew.rxfit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * -------------------------------
 *
 * Transformer that behaves like onExceptionResumeNext(Observable o), but propagates
 * a GoogleAPIConnectionException, which was caused by an unsuccessful resolution.
 * This can be helpful if you want to resume with another RxFit Observable when
 * an Exception occurs, but don't want to show the resolution dialog multiple times.
 *
 * An example use case: Fetch fitness data with server queries enabled, but provide
 * a timeout. When an exception occurs (e.g. timeout), switch to cached fitness data.
 * Using this Transformer prevents showing the authorization dialog twice, if the user
 * denys access for the first read. See MainActivity in sample project.
 */
public class RxFitOnExceptionResumeNext {

    private RxFitOnExceptionResumeNext() { }

    public static <T, R extends T> ObservableTransformer<T, T> with(final Observable<R> other) {
        return source -> source.onErrorResumeNext(getThrowableMapper(other));
    }

    public static <T, R extends T> SingleTransformer<T, T> with(final Single<R> other) {
        return source -> source.onErrorResumeNext(getThrowableMapper(other));
    }

    private static <R> Function<Throwable, R> getThrowableMapper(R other) {
        return throwable -> {
            if(shouldPropagateThrowable(throwable)) { throw Exceptions.propagate(throwable); }
            return other;
        };
    }

    private static boolean shouldPropagateThrowable(Throwable throwable) {
        return !(throwable instanceof Exception) ||
                (throwable instanceof GoogleAPIConnectionException && ((GoogleAPIConnectionException) throwable).wasResolutionUnsuccessful());
    }
}
