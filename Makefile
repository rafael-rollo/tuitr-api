compile:
	@ echo ::::: compiling project 🔥
	@ ./mvnw compile
	
test: compile
	@ echo ::::: testing project 🧪
	@ ./mvnw test

package: compile
	@ echo ::::: packaging project 📦
	@ ./mvnw package -DskipTests
	
run: package
	@ echo ::::: running 🚀
	@ ./mvnw spring-boot:run
