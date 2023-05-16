# STIB RIDE
Java/JavaFX app from scratch to search a way between 2 metro stations in Brussels

## Features
- Find your way from a source station and a destination's one
- Add shortcuts for you recurring trips and give them a name
- Load one of your shortcuts
- See the next metro arrival for a station in real time
- Change station's name language between FR-NL by going to the `Options` menu
- Wander on the stib network map

## Building and launch

To build the project, you'll have to run the makefile by using `make` at the root of the repository to compile.

You will need to have maven installed to build.

## Usage

Launch the app and go to the `Help` menu

## Remarks

* The data of the App is stored with a `SQL database` created via [STIB API v4](https://data.stib-mivb.brussels/explore/dataset/waiting-time-rt-production/table/) data
* Shortcuts are also stored in a database to keep them locally
* Searching stations bar is implemented with JavaFX Searchable ComboBox external lib. and the listener has some display bugs.
* Real-time arrivals using [STIB API v4](https://data.stib-mivb.brussels/explore/dataset/waiting-time-rt-production/table/)
* This app is not responsive

## Code 

* Using a MVC pattern
* Using Facade pattern
* Using Observable-Observer
* Using threads
* Requests API with Retrofit2
* ConfigManager and config.properties for app's information
* Process DB information via DAO, DTO and repositories
* JavaFX views with FXML
* Unit Tests with JUnit5 and Mockito
* Maven project

## Author

* Cameron Noupoue

## Credits

Project devised and created during my studies at the École Supérieure d'Informatique (ESI), Brussels.


