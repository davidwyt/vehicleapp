//
//  RegisterView.m
//  vehicleapp
//
//  Created by will on 14-1-18.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "RegisterView.h"
#import "NetworkUtil.h"

@interface RegisterView ()

@end

@implementation RegisterView

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

- (IBAction)register:(id)sender {
    
    NSMutableString* url=[[NSMutableString alloc]init];
    [url appendString:@"/personal/personalCreate?userName="];
    [url appendString:_userName.text];
    [url appendString:@"&pass="];
    [url appendString:_pass.text];
    [url appendString:@"&email="];
    [url appendString:_email.text];
    NSDictionary* ret=[NetworkUtil post:url :nil];
}
@end
