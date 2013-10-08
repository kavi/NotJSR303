NotJSR303
=========

# Description
A subset implementation of javax.validation aka JSR303 because the full specification may not always be suitable.

# Notes on Constraints

## Groups
Groups are not supported.

## Past and Future
Currently only java.util.Date is supported. Support for Calendar may be implemented in future versions.

## Digits
Digits supports String rather than CharSequence.
It's also expected not to work correctly with BigDecimals that uses scaling.

## Null, AssertFalse, AssertTrue
These Constraints are currently unsupported.

## NotNull, Size, Pattern
These annotations should work according to specification.

## DecimalMin, Min and DecimalMax, Max
These work similarly, which seems according to spec. The difference being that "inclusive" is 
explicit on the Decimal versions and implicitly true on Min/Max.

# Build instructions
Make sure you have ant installed.
Run:
ant dist

This should put a jar file in ant/dist/notjsr303.jar

Other targets:
ant clean      # Cleans build folders.
ant compile    # Compiles java files.
ant test       # Runs unit tests and builds coverage report.
ant dist-src   # Builds zip file containing source.
ant publish    # Currently disabled. Publishes to ivy repository.
