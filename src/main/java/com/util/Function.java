package com.util;

import java.io.File;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobInputStream;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import domain.Settlement;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
	
	
	private static String STORAGE_CONN_STR = "ENTER STORAGE CONN STR";
	private static String CONTAINER_NAME = "settlements";
	
	private CloudStorageAccount storageAccount;
	private CloudBlobClient blobClient;
	private CloudBlobContainer container;
	private CloudBlockBlob blob;
	
	/**
	 * Function converts csv file to json
	 * listens to an HTTP endpoint
	 * @return json response of settlement data
	 */
	@FunctionName("CsvToJson")
	public HttpResponseMessage run(@HttpTrigger(name = "req", methods = { HttpMethod.GET,
			HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			final ExecutionContext context) {
		
		context.getLogger().info("CSVToJSON HTTP trigger processed a request.");
		
		String fileName = "";
		boolean isLocalEnv = false;
		File file = null;
		List<Settlement> settlements = new ArrayList<Settlement>();
	
		if(!isBlankStr(System.getenv("local")))
		{
			isLocalEnv = true;
			fileName = "";
			file = new File(fileName);
		}
		
		Map<String, String> paramMap = request.getQueryParameters();
		
		if(paramMap != null && paramMap.get("filename") != null)
		{
			fileName = paramMap.get("filename");
		}	
		
		if(isBlankStr(fileName))
		{
			fileName = "settlements.csv";
		}
		
		// read file from storage
		CloudBlockBlob blob = readFileFromStorage(fileName);	
		Scanner sc = null;
		
		try {

			if(!isLocalEnv)
			{
				sc = new Scanner(blob.openInputStream()).useDelimiter("\\n|\\|");
			}
			else {
			// parse tokens using pipe (|) and newLine delimiters 
			 sc = new Scanner(file).useDelimiter("\\n|\\|");
			}
			if (sc.hasNext()) {
				// skip first line that has headers
				sc.nextLine();
			}

			while (sc.hasNextLine()) {

				// Parse in input File format: SETTLEMENT DATE|CUSIP|SYMBOL|QUANTITY|DESCRIPTION|PRICE
				Settlement settlement = new Settlement();
				settlement.setSettlementDate(sc.next());
				settlement.setCusip(sc.next());
				settlement.setSymbol(sc.next());
				settlement.setQuantity(sc.nextInt());
				settlement.setDescription(sc.next());
				settlement.setPrice(sc.next().trim());

				// add to list
				settlements.add(settlement);
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
			String msg = "Error parsing csv: " + e.getMessage();
			return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body(msg).build();
		} finally {
			if(sc != null) 	sc.close();
		}

		return request.createResponseBuilder(HttpStatus.OK).body(settlements).build();
	}

	private CloudBlockBlob readFileFromStorage(String filePath) {
		
		String connectStr = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
		String containerName = System.getenv("SETTLEMENTS_CONTAINER");
		
		if(isBlankStr(containerName))
		{
			containerName = CONTAINER_NAME;
		}
		
		if(isBlankStr(connectStr))
		{
			connectStr = STORAGE_CONN_STR;
		}
		
		try {
			storageAccount = CloudStorageAccount.parse(connectStr);			
			blobClient = storageAccount.createCloudBlobClient();			
		    container = blobClient.getContainerReference(containerName);		
		    blob = container.getBlockBlobReference(filePath);	    
		} 
		
		catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidKeyException e) {
			
			e.printStackTrace();
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
		}
		
		
		return blob;
	}

	
	private boolean isBlankStr(String str)
	{
		
		if(str == null || "".equals(str.trim())){
			return true;
		}
		
		return false;
		
	}
	
}
