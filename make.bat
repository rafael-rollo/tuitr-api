@echo off
if "%1" == "clean" goto clean
if "%1" == "compile" goto compile
if "%1" == "test" goto test
if "%1" == "package" goto package
if "%1" == "run" goto run

:clean
echo ::::: cleaning project 🔥
call mvnw.cmd clean
goto end

:compile
echo ::::: compiling project 🏗️
call mvnw.cmd compile
goto end

:test
echo ::::: compiling project 🏗️
call mvnw.cmd compile
echo ::::: testing project 🧪
call mvnw.cmd test
goto end

:package
echo ::::: cleaning project 🔥
call mvnw.cmd clean
echo ::::: packaging project 📦
call mvnw.cmd package -DskipTests
goto end

:run
echo ::::: cleaning project 🔥
call mvnw.cmd clean
echo ::::: packaging project 📦
call mvnw.cmd package -DskipTests
echo ::::: running 🚀
call mvnw.cmd spring-boot:run
goto end

:end
