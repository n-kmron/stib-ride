build:
	@mvn clean install -DskipTests
	@arch -x86_64 java --module-path /Users/cameron/Library/myJava/javafx sdk19/lib --add-modules javafx.controls,javafx.fxml \ -jar target/StibRide-1.0-jar-with-dependencies.jar
# no need for installing maven as it is supposed to be installed
