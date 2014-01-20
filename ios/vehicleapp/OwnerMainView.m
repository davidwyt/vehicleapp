//
//  OwnerMainViewController.m
//  vehicleapp
//
//  Created by will on 14-1-17.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "OwnerMainView.h"

@interface OwnerMainView ()

@end

@implementation OwnerMainView

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

- (IBAction)login:(id)sender {
    [self performSegueWithIdentifier:@"mainlogin" sender:self];
}
@end
