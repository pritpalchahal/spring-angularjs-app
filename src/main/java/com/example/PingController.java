package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.util.MimeTypeUtils;
import org.springframework.http.HttpStatus;

@RestController
public class PingController {

	//a validator to validate ip address
	static InetAddressValidator validator = InetAddressValidator.getInstance();
	
	/**
	 * Method accepts a list of host names, ping them and
	 * returns host names and response time as key,value pairs
	 * @param hostList
	 * @return
	 */
	@RequestMapping(value = "/ping-host", 
	method = {RequestMethod.POST}, 
	consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,Integer> pingAll(@RequestBody String hostList) {
		
		Map<String,Integer> result = new HashMap<String,Integer>();
		String[] hosts = hostList.split(",");
		
//		 Arrays.asList(hosts)
//			 .stream()
//			 .forEach(host -> result.put(host,ping(host)));
		 
		 for(String host : hosts){
			 Integer time = ping(host);
			 result.put(host,time);
		 }
		 
		return result;
	}
	
	/**
	 * Verifies the host name to be added and returns a response.
	 * @param hostName
	 * @return
	 */
	@RequestMapping(value = "/add-host", 
	method = {RequestMethod.POST}, 
	consumes = MimeTypeUtils.ALL_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public boolean addHost(@RequestBody String hostName) {
		print("HostName: "+hostName);
		
		//in case of an ip address is supplied, check if it is valid
		if(isValidIpAddress(hostName)) return true;
		
		//if host name is not an ip address
		//validate to check if it a valid domain name with domain extension
		return isValidHostName(hostName);
	}
	
	/**
	 * Utility method which verifies the host name.
	 * Assumptions regarding host names:
	 * No-IP Free host names must be no longer than 19 characters in length. 
	 * They can contain the letters a-z, numbers 0-9 and special character – (dash).
	 * 
	 * Plus Managed DNS host names can be up to 49 characters in length. 
	 * They can contain the letters a-z, numbers 0-9 and special character – (dash).
	 * 
	 * Basic Host name Guidelines:
	 * You cannot have more than two dashes within a host name.
	 * A host name cannot have any spaces, nor can it start with a dash.
	 * @param hostName
	 * @return
	 */
	private boolean isValidHostName(String hostName){
		if(!hostName.contains(".")) return false;
		
		//check if the host name contains www
		String www_regex = "^www\\.";
		Matcher www_matcher = Pattern.compile(www_regex).matcher(hostName);
		if(www_matcher.find()){hostName = hostName.substring(4,hostName.length());}
		
		String[] vals = hostName.split("\\.",2);
		
		//if nothing after .
		if(!(vals.length > 1)) return false;
		
		String regex1 = "^[a-z0-9][a-z0-9]*[-]?[a-z0-9]*$";
		String regex2 = "^[a-z][a-z]*[.]?[a-z]*$";
		Matcher m1 = Pattern.compile(regex1).matcher(vals[0]);
		Matcher m2 = Pattern.compile(regex2).matcher(vals[1]);
		
		if(m1.matches() && m2.matches()){
			print("Valid.");
			return true;
		}
		else{
			print("Invalid!");
			return false;
		}
	}
	
	/**
	 * Utility method uses org.apache.commons.validator.routines.InetAddressValidator
	 * to confirm a valid ip address
	 * @param hostName
	 * @return
	 */
	private boolean isValidIpAddress(String hostName){
		return validator.isValid(hostName);
	}
	
	/**
	 * Method takes as parameter the hostname or ip address as string 
	 * and returns the ping response time.
	 * @param ip
	 * @return
	 */
	public Integer ping (String ip){
		String inputLine;
        Integer time = null;

        //The command to execute
		//command only sends one echo request
        String pingCmd = "ping -n 1 " + ip;

        //get the runtime to execute the command
        Runtime runtime = Runtime.getRuntime();
		
		//pattern for general response times
        Pattern pattern = Pattern.compile("time=(\\d+)ms");
        Matcher m1 = null;
		
		//pattern for less than 1ms response times
        Pattern pattern2 = Pattern.compile("time<(\\d+)ms");
        Matcher m2 = null;
        try {
            Process process = runtime.exec(pingCmd);

            //reader to read the output of the command
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            //reads the output
            while ((inputLine = in.readLine()) != null) {
                m1 = pattern.matcher(inputLine);
                m2 = pattern2.matcher(inputLine);
                if (m1.find()) {
					time = Integer.parseInt(m1.group(1));
					break;
                }
				else if(m2.find()){
					time = 0;
					break;
				}
            }   
        } catch (Exception ex) {
            print("Error: "+ex.getMessage());
            return null;
        }
    	return time;
	}
	
	static void print(String msg){
		System.out.println(msg);
	}
}
