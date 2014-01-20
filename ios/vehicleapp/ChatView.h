//
//  ChatViewController.h
//  vehicleapp
//
//  Created by will on 14-1-18.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChatView : UIViewController<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong) NSMutableArray *resultArray;
@property (weak, nonatomic) IBOutlet UITableView *table;
@property (nonatomic, strong) NSString *remoteId;
@property (weak, nonatomic) IBOutlet UITextField *sMsg;
- (IBAction)sendMsg:(id)sender;

-(void)receiveMsg:(NSDictionary *)msg;

@end
