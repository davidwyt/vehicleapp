//
//  LoginViewController.m
//  vehicleapp
//
//  Created by will on 14-1-15.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "LoginView.h"
#import "NetworkUtil.h"
#import "JSONUtil.h"
#import "JPushUtil.h"

@implementation LoginView

-(IBAction)login:(id)sender{
    //NSDictionary* dic=[NSDictionary dictionaryWithObjectsAndKeys:_username.text, @"userName",_password.text,@"pass",nil];
    NSMutableString* url=[[NSMutableString alloc]init];
    [url appendString:@"/index/login?userName="];
    [url appendString:_username.text];
    [url appendString:@"&pass="];
    [url appendString:_password.text];
    NSDictionary* ret=[NetworkUtil post:url :nil];
    if ([[ret objectForKey:@"code"] isEqualToString:@"10000"]) {
        id memberId=[[[ret objectForKey:@"result"] objectForKey:@"Personal"] objectForKey:@"member_id"];
        //NSDictionary* temp=[JSONUtil fromJSON:personal];
        NSUserDefaults* memo=[NSUserDefaults standardUserDefaults];
        [memo setObject:memberId forKey:@"id"];
        [memo synchronize];
        [JPushUtil registerUser:[memberId description]];
        //NSUserDefaults* memo2=[NSUserDefaults standardUserDefaults];
        //NSLog([memo2 stringForKey:@"id"]);
        [self performSegueWithIdentifier:@"ownermain" sender:self];
         
    } else {
        UIAlertView *authAlert=[[UIAlertView alloc] initWithTitle:@"auth fail" message:@"wrong username or password" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [authAlert show];
    }
    //NSArray *keys=[ret allKeys];
    //int count=[keys count];
    //for(int i=0;i<count;i++){
      //  id key=[keys objectAtIndex:i];
        //id value=[ret objectForKey:key];
        //NSLog(@"key:%@ and value: %@",key,value);
    //}
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}
//点击屏幕空白处去掉键盘
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.username resignFirstResponder];
    [self.password resignFirstResponder];
}

@end
