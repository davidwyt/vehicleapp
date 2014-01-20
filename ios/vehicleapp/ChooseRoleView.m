//
//  ChooseRoleViewController.m
//  vehicleapp
//
//  Created by will on 14-1-15.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "ChooseRoleView.h"

@interface ChooseRoleView ()

@end

@implementation ChooseRoleView

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

- (IBAction)gotoOwnerLogin:(id)sender {
    [self performSegueWithIdentifier:@"chooseowner" sender:self];
}

- (IBAction)gotoBusinessLogin:(id)sender {
    [self performSegueWithIdentifier:@"choosebusiness" sender:self];
}
@end
