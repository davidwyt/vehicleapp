//
//  NetworkUtil.h
//  vehicleapp
//
//  Created by will on 14-1-15.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NetworkUtil : NSObject

-(BOOL)isConnect;

+(NSDictionary*)post:(NSString*)urlString:(NSDictionary*)dic;
+(NSDictionary*)imPost:(NSString *)urlString :(NSDictionary *)dic;
+(NSDictionary*)imGet:(NSString *)urlString :(NSDictionary *)dic;
//+(NSDictionary*)get:(NSString*)urlString:(NSString*)content;
@end
