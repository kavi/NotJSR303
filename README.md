NotJSR303
=========

# Description
A subset implementation of javax.validation aka JSR303 because the full specification may not always be suitable.

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
