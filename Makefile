clean: 
	@ echo ::::: cleaning project 🔥
	@ ./mvnw clean

compile:
	@ echo ::::: compiling project 🏗️
	@ ./mvnw compile
	
test: compile
	@ echo ::::: testing project 🧪
	@ ./mvnw test

package: clean
	@ echo ::::: packaging project 📦
	@ ./mvnw package -DskipTests
	
run: package
	@ echo ::::: running 🚀
	@ ./mvnw spring-boot:run
