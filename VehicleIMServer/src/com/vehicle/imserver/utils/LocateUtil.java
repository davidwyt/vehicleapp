package com.vehicle.imserver.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vehicle.service.bean.Locate;
import com.vehicle.service.bean.LocateInfo;
import com.vehicle.service.bean.RangeInfo;

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
	
	public double getRangeY(int range){
		double m=0.0308*3600;
		return range/m;
	}
	
	public double getRangeX(int range){
		double m=0.03*3600;
		return range/m;
	}
	
	public List<RangeInfo> get(double centerX,double centerY,int range){
		double rangex=this.getRangeX(range);
		double rangey=this.getRangeY(range);
		List<LocateInfo> list=this.getByRange(centerX-rangex, centerY-rangey, centerX+rangex, centerY+rangey);
		List<RangeInfo> ret=new ArrayList<RangeInfo>();
		for(int i=0;i<list.size();i++){
			double distance=RangeUtil.LantitudeLongitudeDist(centerX, centerY, list.get(i).getLocateX(), list.get(i).getLocateY());
			long during=System.currentTimeMillis()-list.get(i).getTime();
			if(distance<=range&&during<=7200000){
				RangeInfo r=new RangeInfo();
				r.setDistance(distance);
				r.setDuring(during);
				r.setOwnerId(list.get(i).getOwnerId());
				ret.add(r);
			}
		}
		return ret;
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
				if(tempMap!=null){
					Set<String> set=tempMap.keySet();
					for(String s:set){
						list.add(tempMap.get(s));
					}
				}
			}
		}
		return list;
	}
	
	public synchronized void add(LocateInfo info){
		LocateInfo i=ownerMap.get(info.getOwnerId());
		Locate l=new Locate();
		if(i!=null){
			
			l.setX((int)(i.getLocateX()*100));
			l.setY((int)(i.getLocateY()*100));
			Map<String,LocateInfo> temp=rangeMap.get(l);
			if(temp!=null){
				if(temp.containsKey(info.getOwnerId())){
					temp.remove(info.getOwnerId());
				}
			}
		}
		ownerMap.put(info.getOwnerId(),info);
		l.setX((int)(info.getLocateX()*100));
		l.setY((int)(info.getLocateY()*100));
		Map<String,LocateInfo> temp=rangeMap.get(l);
		if(temp==null){
			temp=new HashMap<String,LocateInfo>();
			temp.put(info.getOwnerId(), info);
			rangeMap.put(l, temp);
		} else {
			temp.put(info.getOwnerId(), info);
		}
		
	}

}
