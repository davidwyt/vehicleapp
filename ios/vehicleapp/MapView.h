//
//  MapViewController.h
//  vehicleapp
//
//  Created by will on 14-1-16.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MAMapKit/MAMapKit.h>

@interface MapView : UIViewController<MAMapViewDelegate>

@property (strong, nonatomic) MAMapView *mapView;

@end
