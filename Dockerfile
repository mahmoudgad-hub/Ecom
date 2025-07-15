# استخدم صورة جافا الرسمية
#FROM eclipse-temurin:17-jdk

# اضبط مكان العمل
#WORKDIR /app

# انسخ المشروع
#COPY . .

# نظّف وابني المشروع
#RUN ./mvnw clean package -DskipTests

# شغّل الـ jar الناتج
#CMD ["java", "-jar", "target/emp-0.0.1-SNAPSHOT.jar"]

#FROM eclipse-temurin:17-jre
#WORKDIR /app
#COPY target/emp-0.0.1-SNAPSHOT.jar app.jar
#CMD ["java", "-jar", "app.jar"]

# مرحلة البناء
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# مرحلة التشغيل
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/emp-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]