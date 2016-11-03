/**

Copyright 2014 ATOS SPAIN S.A.

Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Authors Contact:
Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
@email francisco.nieto@atos.net
**/

package net.atos.ari.vital.tassproxy;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.stereotype.Service;

/*import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;
*/
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import eu.betaas.taas.contextmanager.api.ThingsServiceManager;
@Service("TaaSCMClient")
public class TaaSCMClient 
{
	private static Logger logger = Logger.getLogger(TaaSCMClient.class);
	
	public TaaSCMClient ()
	{
		logger.info("Creationg TaaSCMClient");

	}
	
	
	public ArrayList<String> getThingServices ()
	{
		logger.debug ("Calling Get Thing Services to the CM for getting the full list");
		ArrayList<String> myList = null;
		try
		{
			// Retrieve the JasonArray object with the list
			String resList = null; /*EGO myClient.getContextThingServices();*/
			JsonElement jelement = new JsonParser().parse(resList);
			JsonObject parsedRes = jelement.getAsJsonObject();
			JsonArray listArray = parsedRes.getAsJsonArray("list");
			
			// Transform the JasonArray in an ArrayList
			myList = new ArrayList<String>();
			for (int i=0; i<listArray.size(); i++)
			{
				myList.add(listArray.get(i).getAsString());
			}			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return new ArrayList<String>();
		}
		
		logger.debug("Invocation done! Retrieved: " + myList.size());
		return myList;
	}
	
	public String retrieveThingIdentifier (String thingServiceId)
	{
		String data = null;;/* myClient.getContextualMeasurement(thingServiceId);*/
		JsonElement jelement = new JsonParser().parse(data);
		JsonObject parsedRes = jelement.getAsJsonObject();
		String thingId = parsedRes.get("ThingId").getAsString();
		
		return thingId;
	}
	
	public ArrayList<String> getEquivalentThingServices (String feature, ThingLocation location, String thingServiceId)
	{
		logger.debug("Looking for equivalents related to " + thingServiceId);
		logger.debug ("Calling Get Thing Services to the CM with feature " + feature + " in " + location.getLocationIdentifier());
		ArrayList<String> myList = new ArrayList<String>();
		try
		{
			// Retrieve the JasonArray object with the list			
			String resList = null;
			if (location.getEnvironment())
			{
				resList = null; 
			}
			else
			{
				resList = null; /*myClient.getContextThingServices(feature, location.getLocationIdentifier(), location.getLocationKeyword(), location.getFloor());*/
			}
			
			logger.debug("Data received: " + resList);
			
			JsonElement jelement = new JsonParser().parse(resList);
			JsonObject parsedRes = jelement.getAsJsonObject();
			JsonArray listArray = parsedRes.getAsJsonArray("list");
			JsonArray listEqArray = parsedRes.getAsJsonArray("eq_list");			
						
			// Transform the JasonArray in an ArrayList and look for the current thing service			
			int position=-1;
			for (int i=0; i<listArray.size(); i++)
			{
				if (listArray.get(i).getAsString().equalsIgnoreCase(thingServiceId))
				{
					// We found our thing service, so we take the position for the equivalents list
					position = i;
					break;
				}
				
			}	
			
			if (position==-1)
			{
				// If the thing service didn't appear, then provide empty list
				logger.error("The thing service wasn't retrieved. Unable to find equivalents!");
				return myList;
			}
			
			// Transform the Equivalent Services matrix in an ArrayList of ArrayList
			JsonArray currentList = listEqArray.get(position).getAsJsonArray();			
			for (int j=0; j<currentList.size(); j++)
			{
				String currentEquivalent = currentList.get(j).getAsString();
				if (!currentEquivalent.equalsIgnoreCase(thingServiceId))
				{
					myList.add(currentEquivalent);
				}				
			}		
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
		
		logger.debug("Invocation done! Equivalents found: " + myList.size());
			
		return myList;		
	}
	
}
