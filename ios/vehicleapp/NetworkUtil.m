//
//  NetworkUtil.m
//  vehicleapp
//
//  Created by will on 14-1-15.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "NetworkUtil.h"
#import "JSONUtil.h"
#import <netinet/in.h>
#import <SystemConfiguration/SCNetworkReachability.h>


@implementation NetworkUtil

-(BOOL)isConnect{
    
    struct sockaddr_in initAddress;
    initAddress.sin_len = sizeof(initAddress);
    initAddress.sin_family = AF_INET;
    
    SCNetworkReachabilityRef     readRouteReachability = SCNetworkReachabilityCreateWithAddress(NULL, (struct sockaddr *)&initAddress);     //创建测试连接的引用：
    SCNetworkReachabilityFlags flags;
    
    BOOL getRetrieveFlags = SCNetworkReachabilityGetFlags(readRouteReachability, &flags);
    CFRelease(readRouteReachability);
    
    if (!getRetrieveFlags) {
        return NO;
    }
    
    BOOL flagsReachable = ((flags & kSCNetworkFlagsReachable) != 0);
    BOOL connectionRequired = ((flags & kSCNetworkFlagsConnectionRequired) != 0);
    return (flagsReachable && !connectionRequired) ? YES : NO;
}

+(NSDictionary*)post:(NSString *)urlString :(NSDictionary *)dic{
    NSMutableString* urls=[[NSMutableString alloc] initWithString:@"http://mobiles.ac0086.com"];
    [urls appendString:urlString];
    NSURL *url=[NSURL URLWithString:urls];
    NSMutableURLRequest *request=[NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:10];
    [request setHTTPMethod:@"POST"];
    if(dic!=nil){
    NSData *data=[JSONUtil toJSON:dic];
    [request setHTTPBody:data];
    }
    //[NSURLConnection connectionWithRequest:request delegate:self];
    NSData *received=[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    return [JSONUtil fromJSON:received];
}

+(NSDictionary*)imPost:(NSString *)urlString :(NSDictionary *)dic{
    NSMutableString* urls=[[NSMutableString alloc] initWithString:@"http://192.168.1.105:8080/VehicleIMServer"];
    [urls appendString:urlString];
    NSURL *url=[NSURL URLWithString:urls];
    NSMutableURLRequest *request=[NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:10];
    [request setHTTPMethod:@"POST"];
    if(dic!=nil){
        NSLog([[JSONUtil toJSON:dic] description]);
        NSData *data=[JSONUtil toJSON:dic];
        [request addValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        
        [request setHTTPBody:data];
    }
    //[NSURLConnection connectionWithRequest:request delegate:self];
    NSData *received=[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    return [JSONUtil fromJSON:received];
}

+(NSDictionary*)imGet:(NSString *)urlString :(NSDictionary *)dic{
    NSMutableString* urls=[[NSMutableString alloc] initWithString:@"http://192.168.1.105:8080/VehicleIMServer"];
    [urls appendString:urlString];
    NSURL *url=[NSURL URLWithString:urls];
    NSMutableURLRequest *request=[NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:10];
    [request setHTTPMethod:@"GET"];
    if(dic!=nil){
        NSData *data=[JSONUtil toJSON:dic];
        [request setHTTPBody:data];
    }
    //[NSURLConnection connectionWithRequest:request delegate:self];
    NSData *received=[NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    return [JSONUtil fromJSON:received];
}

@end
