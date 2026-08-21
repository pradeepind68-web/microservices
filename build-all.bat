@echo off

for /d %%i in (*) do (
    echo Building %%i
    pushd %%i
    call mvn clean package
    call mvn spring-boot:build-image
    popd
)

echo All builds completed.
pause