//
//  LoginViewController.h
//  vehicleapp
//
//  Created by will on 14-1-15.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginView : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *username;
@property (weak, nonatomic) IBOutlet UITextField *password;

-(IBAction)login:(id)sender;

@end
