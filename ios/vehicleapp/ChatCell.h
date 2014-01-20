//
//  ChatCell.h
//  vehicleapp
//
//  Created by will on 14-1-18.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChatCell : UITableViewCell

@property (nonatomic, strong) UIView *bubbleView;
@property (nonatomic, strong) UIImageView *photo;

-(void)setContent:(NSMutableDictionary*)dict;

@end
