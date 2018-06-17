package cmdb;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {


    private static JSONParser parser = new JSONParser();
    private static final Logger LOGGER = Logger.getLogger( Data.class.getName() );

    public JSONArray getAllResults() {
        return allResults;
    }

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    public ArrayList<VM>getSelectedVMs(String subscription){
        return vmMamp.get(subscription);
    }

    private JSONArray allResults = new JSONArray();
    private ArrayList<String> subscriptions = new ArrayList<String>();
    private ArrayList<VM> vmList = new ArrayList<VM>();

    private HashMap<String,ArrayList<VM>> vmMamp = new HashMap<String, ArrayList<VM>>();

    /*
        Hierarchy should be:
            -   Subscription ID + Name
                -   ResourceGroup
                    -   Vnet
                        -   Subnet
                            -   VM + Interface
     */
    private void init() throws FileNotFoundException, ParseException, IOException {



        String path = "scripts/results/";
        File file = new File(path);
        String[] files = file.list();
        for(String filex : files){
            String fileToParse = String.format("%s%s", path, filex);
            LOGGER.log(Level.INFO, "parsing file: {0}", fileToParse);
            String[] fileName = fileToParse.split("-###-");
            String subscriptionName = fileName[0].replace(path,"");
            String subscriptionId = fileName[1].replace(".json","");

            //  Create JsonObject per file


            JSONObject sub = new JSONObject();
            sub.put("subscription",String.format("%s%s%s", subscriptionName, "---", subscriptionId));
            JSONArray allMachines = new JSONArray();

            //  Parsing content
            String content = new String(Files.readAllBytes(Paths.get(fileToParse)));
            for (Object res : (JSONArray) JSONValue.parse(content)) {
                JSONObject obj = (JSONObject)res;
                JSONObject machine = new JSONObject();

                //  Group
                if (obj.get("group")!=null){
                    machine.put("group",obj.get("group"));
                }
                //  Machine
                if (obj.get("machine")!=null){
                    String tmpString = obj.get("machine").toString();
                    //JSONObject group = new JSONObject();
                    int lastSlash = tmpString.lastIndexOf("/")+1;
                    String newString = tmpString.substring(lastSlash,tmpString.length());
                    machine.put("machine",newString);
                }
                //  Network
                if (obj.get("network")!=null){
                    String tmpString = obj.get("network").toString();

                    int virtNetworks = tmpString.lastIndexOf("virtualNetworks")+17;
                    int subNetsBegin = tmpString.lastIndexOf("subnets")-2;
                    String vnet = tmpString.substring(virtNetworks,subNetsBegin);

                    int subNets = subNetsBegin+11;
                    String subnet = tmpString.substring(subNets,tmpString.length()-2);

                    machine.put("subnet",subnet);
                    machine.put("vnet",vnet);

                }
                if (obj.get("nic")!=null){
                    String tmpString = obj.get("nic").toString();
                    machine.put("nic",tmpString);
                }

                allMachines.add(machine);

            }

            sub.put("machines",allMachines);
            allResults.add(sub);

        }
    }

    JSONObject getSubscriptionObject(String subscriptionName){

        JSONObject result = null;
        for(int i=0; i<allResults.size(); i++){

            JSONObject tmp = (JSONObject) allResults.get(i);
            if(subscriptionName == tmp.get("subscription").toString()){
                result = tmp;
            }
        }
        return result;
    }


    private void makeSubscriptionList(){

        for(int i=0; i<allResults.size(); i++) {

            JSONObject jsonObject = (JSONObject) allResults.get(i);
            subscriptions.add(jsonObject.get("subscription").toString());
        }
        Collections.sort(subscriptions);
    }


    private void makeVMMap(){

        //  Iterate over all Subscriptions
        for(int i=0; i<allResults.size(); i++) {

            JSONObject jsonObject = (JSONObject) allResults.get(i);
            String subscription = jsonObject.get("subscription").toString();

            JSONArray vms = (JSONArray)jsonObject.get("machines");
            ArrayList<VM> tmpList = new ArrayList<VM>();
            for(int j=0; j<vms.size(); j++){
                VM vm = new VM();
                JSONObject vmJsonObject = (JSONObject)vms.get(j);
                vm.setResourcegroup(vmJsonObject.get("group")!=null?vmJsonObject.get("group").toString():"unknown");
                vm.setVnet(vmJsonObject.get("vnet")!=null?vmJsonObject.get("vnet").toString():"unknown");
                vm.setSubnet(vmJsonObject.get("subnet")!=null?vmJsonObject.get("subnet").toString():"unknown");
                vm.setVm(vmJsonObject.get("machine")!=null?vmJsonObject.get("machine").toString():"unknown");
                tmpList.add(vm);
            }
            vmMamp.put(subscription,tmpList);
        }

    }


    void collect(){

        try{
            init();
            makeSubscriptionList();
            makeVMMap();
        }catch(FileNotFoundException fnex){
            LOGGER.log( Level.SEVERE, "File not found exception: {0} ", fnex.getMessage() );
        }catch(ParseException pex){
            LOGGER.log( Level.SEVERE, "JSON Parsing File exception: {0} ", pex.getMessage() );
        }catch(Exception ex){
            LOGGER.log( Level.SEVERE, "General exception: {0} ", ex.getMessage() );
        }

    }



}
