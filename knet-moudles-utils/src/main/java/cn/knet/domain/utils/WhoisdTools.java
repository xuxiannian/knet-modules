package cn.knet.domain.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

@Slf4j
public class WhoisdTools {
  private String ip;
  private int port;
  public WhoisdTools(String ip, int port){
	  this.ip=ip;
	  this.port=port;
  }
  
  
  public List<String> getDomainData(String domain){
	  List<String> list = new ArrayList<String>();
	  try{
			Socket client = new Socket(ip, port);
			client.setTcpNoDelay(true);
			PrintStream out = new PrintStream(client.getOutputStream());
			out.println(domain);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String readLine = "";
			while ((readLine = in.readLine()) != null) {
				if (readLine.length() > 0 && readLine.charAt(0) != '%') {
					  list.add(readLine);
					  log.info(readLine);
					//No match
				}
			}
			out.close();
			in.close();
			client.close();
			return list;
	  }catch(Exception ex){
		  log.error(ex.getMessage());
		  return null;
	  }
  }

  public Map<String,Object> getFilterDomainData(String domain){
	  Map<String,Object> map = new HashMap<String,Object>();
	  try{
		  List<String> list = getDomainData(domain);
			  if(list!=null&&list.size()>2){
				  if(!list.get(0).contains("The queried object does not exist: Common; No match.")){
					  String str1 = "Name Server";
					  String str2 ="Domain Status";
					  List<String> filter1 = filterDomainData(list,str1);
					  List<String> filter2 = filterDomainData(filter1,str2);
					  for (String str : filter2) {
						  if(str.trim().startsWith(">>>")){
							  String filterStr = str.trim();
							  str=filterStr.substring(3);
						  }
						  if(str.trim().endsWith("<<<")){
							  String filterStr = str.trim();
							  str=filterStr.substring(0,filterStr.length()-3);
						  }
						String[] arr = splitStr(str);
						if(arr!=null)
						{
						  map.put(arr[0], arr[1]);
						}
					  }
				  }
				  return map;
			  }else{
			  String[] arr = list.get(0).split(";");
			  map.put("DOMAIN_TYPE", arr[0].toUpperCase().trim());
			  map.put("NEXT_ACTION", arr[1].trim());
			  if(list.size()==2){
				  String[] arr2 = list.get(1).split(":");
				  map.put("DOMAIN_KEY", arr2[0].trim());
				  map.put("DOMAIN_VALUE", arr2[1].trim());
			  }
			  return map;
		  }
	  }catch(Exception ex){
		 ex.printStackTrace();
		  return null;
	  }
  }
  
  private List<String> filterDomainData(List<String> domain,String filterStr){
	  List<String> list = new ArrayList<String>();
	  String strAll="";
	  for (String str : domain) {
		if(str.trim().startsWith(filterStr)){
		  String[] arr =splitStr(str);
		  if(arr!=null){
			  strAll+=arr[1]+",";
		  }
		  
		}
		else{
			list.add(str);
		}
	  }
	  if(strAll.length()>1){
		  list.add(filterStr+": "+strAll.substring(0, strAll.length()-1));
	  }
	  return list;
  }
  
  private  String[] splitStr(String str){
	 try{
		 String[] arr = new String[2];
		 int n = str.indexOf(":");
		 if(n!=-1){
			 arr[0]=str.substring(0, n);
			 arr[1]=str.substring(n+1).trim();
		 }
		 else{
			 return null;
		 }
		 return arr;
	 } 
	 catch(Exception ex){
		 return null;
	 }
  }
  
  public static void main(String[] args)  {
//	  WhoisdTools tools = new WhoisdTools("whois.gtld.knet.cn",43);
	  WhoisdTools tools = new WhoisdTools("wis.knetreg.cn",43);
	  Map<String,Object> map =tools.getFilterDomainData("北龙中网.网址");
	  map.size();
	  
	  Set<String> key = map.keySet();
      Iterator<String> iter = key.iterator();
      while (iter.hasNext()) {
          String field = iter.next();
          System.err.println(field + "——>" + map.get(field));
      }
  }
}

