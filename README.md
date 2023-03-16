# android-fibo-app-kotlin

## Brief description

This Android app allows a user to request arbitrary numbers in the Fibonacci sequence https://en.wikipedia.org/wiki/Fibonacci_sequence. It saves the user's previous requests (number, value, date of request) in a local database so they can be reviewed later in other sessions. List of requests is displayed in main screen and automatically updated using **Flow** when there are changes on the database. Also to be more efficient, the application saves all fibonacci values that were needed to calculate a certain requested number, so they are not calculated over and over again for other requests, but ofcourse those numbers are not displayed until the user requests one of them.
