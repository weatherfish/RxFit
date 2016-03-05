# Reactive Fit API Library for Android

This library wraps the Fit API in [RxJava](https://github.com/ReactiveX/RxJava) Observables. No more managing GoogleApiClients! Also, the authorization process for using fitness data is handled by the lib.

# Usage

Initialize RxFit once, preferably in your Application `onCreate()` via `RxFit.init(...)`. Make sure to include all the APIs and Scopes that you need for your app. The RxFit class is very similar to the Fitness class provided by the Fit API. Instead of `Fitness.HistoryApi.readData(apiClient, dataReadRequest)` you can use `RxFit.History.read(dataReadRequest)`. Make sure to have the Location and Body Sensors permission from Marshmallow on, if they are needed by your Fit API requests.

Example:

```java
RxFit.init(
        context,
        new Api[] { Fitness.HISTORY_API },
        new Scope[] { new Scope(Scopes.FITNESS_ACTIVITY_READ) }
);

DataReadRequest dataReadRequest = new DataReadRequest.Builder()
	    .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
	    .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
	    .bucketBySession(1, TimeUnit.MINUTES)
	    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
	    .build();

RxFit.History.read(dataReadRequest)
        .flatMap(dataReadResult -> Observable.from(dataReadResult.getBuckets()))
        .subscribe(bucket -> {
        	/* do something */
        });
```

You can also obtain an `Observable<GoogleApiClient>`, which connects on subscribe and disconnects on unsubscribe via `GoogleAPIClientObservable.create(...)`.

# Sample

A basic sample app is available in the `sample` project. You need to create an OAuth 2.0 Client ID for the sample app, see the [guide in the Fit API docs](https://developers.google.com/fit/android/get-api-key).

# Setup

Add the following to your `build.gradle`:

	repositories {
	    maven { url 'https://dl.bintray.com/patloew/maven' }
	}

	dependencies {
	    compile 'com.patloew.rxfit:rxfit:1.0.0'
	}

# Credits

The code for managing the GoogleApiClient is taken from the [Android-ReactiveLocation](https://github.com/mcharmas/Android-ReactiveLocation) library by Michał Charmas, which is licensed under the Apache License, Version 2.0.

# License

	Copyright 2016 Patrick Löwenstein

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.