@echo off

for /d %%i in (*) do (
    echo Building %%i
    pushd %%i
    call mvn clean install
    popd
)

echo All builds completed.
pause