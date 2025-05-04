.PHONY: build

build:
	$(test -d build/ && rm -r build/)
	javac -cp "lib/gson-2.13.0.jar" -d build src/*.java src/user/*.java src/database/*.java src/database/items/*.java src/database/reports/*.java src/database/importers/*.java
	cp -r lib/ build/

run: build
	cd build/ && java -cp .:lib/gson-2.13.0.jar:lib/ojdbc17.jar:lib/mysql-connector-j-9.2.0.jar Driver
