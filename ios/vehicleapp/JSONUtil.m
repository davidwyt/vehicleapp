//
//  JSONUtil.m
//  vehicleapp
//
//  Created by will on 14-1-15.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "JSONUtil.h"

@implementation JSONUtil

+(NSDictionary*)fromJSON:(NSData*)json{
    NSDictionary* dic=[NSJSONSerialization JSONObjectWithData:json options:kNilOptions error:nil];
    return dic;
}

+(NSData*)toJSON:(id)dic{
    NSData *json=[NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:nil];
    if([json length]>0){
        NSLog([[NSString alloc] initWithData:json encoding:NSASCIIStringEncoding]);
        return json;
    } else {
        return nil;
    }
}



@end
