package com.vehicle.imserver.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vehicle.service.bean.Locate;
import com.vehicle.service.bean.LocateInfo;

public class LocateUtil {
	
	private static LocateUtil instance=null;
	
	private Map<String,LocateInfo> ownerMap=null;
	
	private Map<Locate,Map<String,LocateInfo>> rangeMap=null; 
	
	private LocateUtil(){
		ownerMap=new HashMap<String , LocateInfo>();
		rangeMap=new HashMap<Locate,Map<String,LocateInfo>>();
	}
	
	public static LocateUtil getInstance(){
		if(instance==null){
			instance=new LocateUtil();
		}
		return instance;
	}
	
	public LocateInfo getByOwner(String id){
		if(ownerMap.containsKey(id)){
			return ownerMap.get(id);
		}
		return null;
	}
	
	public List<LocateInfo> getByRange(Double startX,Double startY,Double endX,Double endY){
		Integer sx=(int) (startX*100);
		Integer sy=(int) (startY*100);
		Integer ex=(int) (endX*100)+1;
		Integer ey=(int) (endY*100)+1;
		List<LocateInfo> list=new ArrayList<LocateInfo>();
		for(int i=sx;i<=ex;i++){
			for(int j=sy;j<ey;j++){
				Locate l=new Locate();
				l.setX(i);
				l.setY(j);
				Map<String,LocateInfo> tempMap=rangeMap.get(l);
				Set<String> set=tempMap.keySet();
				for(String s:set){
					list.add(tempMap.get(s));
				}
			}
		}
		return list;
	}
	
	public synchronized void add(LocateInfo info){
		LocateInfo i=ownerMap.get(info.getOwnerId());
		Locate l=new Locate();
		l.setX((int)(i.getLocateX()*100));
		l.setY((int)(i.getLocateY()*100));
		Map<String,LocateInfo> temp=rangeMap.get(l);
		if(temp!=null){
			if(temp.containsKey(info.getOwnerId())){
				temp.remove(info.getOwnerId());
			}
		}
		ownerMap.put(info.getOwnerId(),info);
		l.setX((int)(info.getLocateX()*100));
		l.setY((int)(info.getLocateY()*100));
		temp=rangeMap.get(l);
		if(temp==null){
			temp=new HashMap<String,LocateInfo>();
			temp.put(info.getOwnerId(), info);
			rangeMap.put(l, temp);
		} else {
			temp.put(info.getOwnerId(), info);
		}
		
	}

}
