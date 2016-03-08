# Changelog

## Version 1.1.1

* Fix for multiple subscribers on RxFit Observables.
* Fixed bug which did not handle Exceptions after successful resolution.
* Added RxFit.OnExceptionResumeNext Transformer.

## Version 1.1.0

* BREAKING CHANGE: Removed PermissionRequiredException in favor of SecurityException
* Added `RxFit.checkConnection()` Completable.
* Timeouts can now be provided when creating an Observable. Also, a global default timeout for all Fit API requests made through the lib can be set.