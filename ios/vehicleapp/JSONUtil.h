//
//  JSONUtil.h
//  vehicleapp
//
//  Created by will on 14-1-15.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JSONUtil : NSObject
+(NSDictionary*)fromJSON:(NSData*)json;
+(NSString*)toJSON:(id)dic;
@end
