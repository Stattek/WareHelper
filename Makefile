.PHONY: build

build:
	javac -d build src/*.java src/database/*.java src/database/items/*.java src/database/reports/*.java src/database/importers/*.java
