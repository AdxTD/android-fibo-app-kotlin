# android-fibo-app-kotlin

## Brief description

This Android app allows a user to request arbitrary numbers in the Fibonacci sequence https://en.wikipedia.org/wiki/Fibonacci_sequence. It saves the user's previous requests (number, value, date of request) in a local database so they can be reviewed later in other sessions. List of requests is displayed in main screen and automatically updated using **Flow** when there are changes on the database. Also to be more efficient, the application saves all fibonacci numbers that were needed to calculate a certain requested number, so they are not calculated over and over again for other requests, but ofcourse those numbers are not displayed until the user requests one of them.

## App models and database entities 

* **FiboNumber: id (fiboNumber: Int), value (fiboValue: Long)** - where id is primary key represents the index of fibonacci number in the sequence i.e. the nth number, value is actual fibonacci number in the sequence i.e. f(n), it is long to hold as higher fibonacci number as possible
* **FiboRequest: id: Int (primary key auto generated), fibo_number (fiboNumber: Int), date (requestDate: String)** - where fibo_number is a foriegn key represents the relation between the two tables, as this table holds the requests dates for calculated fibonacci numbers that were saved in the first table
*  **JoinedFiboRequestsNumbers: fiboNumber: Int, fiboValue : Long, requestDate: String** - it is a model to hold results from inner joining the db two tables, and used for displaying those data in the list of the main screen

_Limitation note:_ long type can save up to the 92th fibonacci number of the sequence, so currently numbers larger than 92 are not allowed to be requested, until some optimisation might be done here

## App UI/UX

* A request is displayed in material design card, showing the request fibonacci number plus its index in the sequence (e.g. f(1) = 1, f(6) = 8) - and below that a full request date is diplayed
* Material design text input element is used, accepting only positive number, empty requests are not allowed
* A message informing the user that requested number is beyond limits is displayed when they request fibo n > 92
* A message informing the user that they requested this number before is displayed when they repeat a previous requested number (but ofcourse we can allow multiple requests to the same number saving multiple requests dates records without any need to change the database schema)
* UI theme supports dark (night) mode - even with the custom colors used in the project

_Possible improvements: implementing details screen which can be used to show multiple requests with multiple dates for same fibo number_ 

## Android project structure and implementation notes

* **MVVM** design is used in the structure of the project
* **SQLite** is used to store data, **Room ORM** framework is used for the implementatio. _note: as our database design tends to be structured and relational then SQLite is preffered and more efficient than other means of local storage and data persistence (e.g. vs DataStore which would have been better option in case of just storing key-value type of information)_
* **Unit tests** are implemented in the androidTest folder to verify the main and most important database operations in the DAO class
* **Repository pattern** is used in the project - despite the absence of other data sources but it is still useful to decouple / seperate the data layer form viewModels, e.g. in case we want to change the type of database used or in case we added a new datasource to the project ... etc.
* Minified is enabled for both debug and release version, obfuscation won't effect the app logic nor the database - and we get smaller size apk
* **Important - Gradle vars:** please check the variables values available in the project build.gradle file, values like gradle plugin version, target SDK and compile SDK version can be changed here to be suitable to your enviroment before the build    
   
   
   
 ## Improvements tasks:

1- notify the recycler view (either when a previous request happen - or - when a new item added) - DONE.    
2- move current main screen into a new fragment (main activity should only hold navigation functionality) - DONE    
3- create navigation plus a new empty fragment to navigate to when item on clicked. (Use base fragment for shared functionalities between fragments)  - DONE               
4- in the new fragment: use same adapter but publishing data from different db method, this one should contain all requests within chosen date  - DONE    
5- add a switch into main fragment to enable / disable multi requests on same number  - DONE      
6- add some beautiful splash screen to welcome the user!   - DONE       
7- provide more unit tests to cover all data access functionalities.     
8- provide turn off log mechanism.    
9- Update the readme file with all improvements
