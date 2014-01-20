//
//  WelcomeViewController.m
//  vehicleapp
//
//  Created by will on 14-1-15.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "WelcomeView.h"
#import "NetworkUtil.h"

@interface WelcomeView ()

@end

@implementation WelcomeView

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(IBAction)toLogin:(id)sender{
    [self performSegueWithIdentifier:@"chooseIdentifier" sender:self];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    NetworkUtil *util=[NetworkUtil new];
    if([util isConnect]){
        //LoginViewController *controller=[self.storyboard instantiateViewControllerWithIdentifier:@"welcomeIdentifier"];
        //[self.navigationController pushViewController:controller animated:YES];
        
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
