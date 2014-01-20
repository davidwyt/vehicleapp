//
//  ChatListView.m
//  vehicleapp
//
//  Created by will on 14-1-18.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "ChatListView.h"

@interface ChatListView ()

@end

@implementation ChatListView

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)startChat:(id)sender {
    NSUserDefaults* memo=[NSUserDefaults standardUserDefaults];
    [memo setObject:@"123" forKey:@"remoteId"];
    [memo synchronize];
    [self performSegueWithIdentifier:@"chat" sender:self];
    
}
@end
