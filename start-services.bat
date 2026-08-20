@echo off
echo Starting Spring Boot services...

start "naming-server" cmd /k "cd /d %~dp0naming-server && mvn spring-boot:run"
timeout /t 20 /nobreak > nul

start "auth-service" cmd /k "cd /d %~dp0auth-service && mvn spring-boot:run"

start "user-service" cmd /k "cd /d %~dp0user-service && mvn spring-boot:run"

start "product-service" cmd /k "cd /d %~dp0product-service && mvn spring-boot:run"

start "card-service" cmd /k "cd /d %~dp0card-service && mvn spring-boot:run"

start "order-service" cmd /k "cd /d %~dp0order-service && mvn spring-boot:run"

start "payment-service" cmd /k "cd /d %~dp0payment-service && mvn spring-boot:run"
timeout /t 15 /nobreak > nul

start "api-gateway" cmd /k "cd /d %~dp0api-gateway && mvn spring-boot:run"

echo All services started.
pause