.PHONY: build

build:
	javac -cp "lib/gson-2.13.0.jar" -d build src/*.java src/database/*.java src/database/items/*.java src/database/reports/*.java src/database/importers/*.java
