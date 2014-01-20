//
//  MapViewController.m
//  vehicleapp
//
//  Created by will on 14-1-16.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "MapView.h"

@interface MapView ()

@end

@implementation MapView

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

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear: animated];
    self.mapView=[[MAMapView alloc] initWithFrame:CGRectMake(0, 0, 320, 460)];
    self.mapView.delegate = self;
    [self.view addSubview:self.mapView];
}

@end
