//
//  RegisterView.h
//  vehicleapp
//
//  Created by will on 14-1-18.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RegisterView : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *email;
@property (weak, nonatomic) IBOutlet UITextField *userName;
@property (weak, nonatomic) IBOutlet UITextField *pass;
- (IBAction)register:(id)sender;

@end
