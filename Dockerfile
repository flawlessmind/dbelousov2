# Use official Tomcat image
FROM tomcat:10.1.14-jdk17

# Remove the default Tomcat ROOT application
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy the WAR file from the Gradle build directory
COPY build/libs/pkmn.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 for web traffic
EXPOSE 8080

CMD ["catalina.sh", "run"]

