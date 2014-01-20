//
//  JPushUtil.m
//  vehicleapp
//
//  Created by will on 14-1-18.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "JPushUtil.h"
#import "APService.h"

@implementation JPushUtil

+(void)registerUser:(NSString *)memberId{
    [APService setAlias:memberId callbackSelector:nil object:nil];
}

@end
