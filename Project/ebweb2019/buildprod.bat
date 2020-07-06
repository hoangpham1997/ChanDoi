mvnw clean && del package-lock.json && git pull && mvnw package -Pprod,tls -DskipTests
cmd /k
