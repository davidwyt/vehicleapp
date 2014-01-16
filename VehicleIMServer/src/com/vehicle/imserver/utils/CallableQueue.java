package com.vehicle.imserver.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallableQueue {
	
	private int threadNum;
	
	private ThreadPoolExecutor executor;
	private BlockingQueue<CallableWrapper> queue;
	
	private static class InstanceHolder{
		public final static CallableQueue instance = new CallableQueue();
	}
	
	private CallableQueue()
	{
		init();
	}
	
	public static CallableQueue getInstance()
	{
		return InstanceHolder.instance;
	}
	
	private void init()
	{
		threadNum = Runtime.getRuntime().availableProcessors() * 2;
		queue = new LinkedBlockingQueue<CallableWrapper>();
		
		executor = new ThreadPoolExecutor(threadNum, threadNum, 10, TimeUnit.SECONDS, (BlockingQueue)queue);
		
		for(int i = 0; i < threadNum; i ++)
		{
			executor.execute(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public void destroy()
	{
		executor.shutdown();
	}
	
	public void Queue(Callable<?> call)
	{
		CallableWrapper callWrapper = new CallableWrapper(call);
		executor.execute(callWrapper);
	}
	
	class CallableWrapper implements Runnable{
		
		private Callable<?> call;
		public CallableWrapper(Callable<?> call)
		{
			this.call = call;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				call.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
