# Changelog

## Version 1.5.0

* Updated Play Services (9.8.0) and RxJava (1.2.1).
* Added support for Goals API.

## Version 1.4.0

* Updated Play Services (9.6.1) and RxJava (1.2.0).
* BREAKING CHANGE: RxFit no longer provides static methods. Create an instance once and share it, e.g. via dependency injection or by providing the instance via your Application class.

## Version 1.3.0

* Updated to Play Services 9.2.0.
* Updated RxJava.
* Added support for History API `un/registerDataUpdateListener()`.

## Version 1.2.1

* BREAKING CHANGE: RxFit.OnExceptionResumeNext now exposes a static `.with()` method, which returns a Single or Observable Transformer.
* Updated dependencies.

## Version 1.2.0

* BREAKING CHANGE: The lib now uses Singles instead of Observables if only one item is emitted.
* BREAKING CHANGE: Observables, which previously emitted a single List, now emit the items of the list.
* BREAKING CHANGE: `RxFit.Ble.startScan(...)` and `stopScan(...)` was removed and replaced by `RxFit.Ble.scan(...)`.
* Added RxFit.OnExceptionResumeNext.Single Transformer.

## Version 1.1.1

* Fix for multiple subscribers on RxFit Observables.
* Fixed bug which did not handle Exceptions after successful resolution.
* Added RxFit.OnExceptionResumeNext Transformer.

## Version 1.1.0

* BREAKING CHANGE: Removed PermissionRequiredException in favor of SecurityException
* Added `RxFit.checkConnection()` Completable.
* Timeouts can now be provided when creating an Observable. Also, a global default timeout for all Fit API requests made through the lib can be set.