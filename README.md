HTTP Azure Function opens an Input Stream to read a .csv file from Azure Blob Storage, and converts it to JSON

**To build:** `mvn clean install -DskipTests`  <br />
**To run:** `mvn azure-functions:run` <br />
**To Debug Locally:** `mvn azure-functions:run -DenableDebug` and configuration debug configuration in eclipse on `localhost` (default port is `5005` <br />
**To deploy:** `mvn azure-functions:deploy` <br />
