FROM openjdk
WORKDIR shortcut
ADD bin/url_shortcut-1.0.jar app.jar
ENTRYPOINT java -jar app.jar





