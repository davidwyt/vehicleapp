//
//  cn_edu_sjtu_vehicleapp_AppDelegate.m
//  vehicleapp
//
//  Created by will on 14-1-13.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "cn_edu_sjtu_vehicleapp_AppDelegate.h"
#import "APService.h"
#import "ChatView.h"
#import <MAMapKit/MAMapKit.h>

@implementation cn_edu_sjtu_vehicleapp_AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
    // Jpush register
    [APService registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge |UIRemoteNotificationTypeSound)];
    [APService setupWithOption:launchOptions];
    
    //MAMap apikey
    [MAMapServices sharedServices].apiKey=@"854165fc21d9b1b567dcf4d5382ffc2e";
    
    NSNotificationCenter *defaultCenter = [NSNotificationCenter defaultCenter];
    
    [defaultCenter addObserver:self selector:@selector(networkDidSetup:) name:kAPNetworkDidSetupNotification object:nil];
    [defaultCenter addObserver:self selector:@selector(networkDidClose:) name:kAPNetworkDidCloseNotification object:nil];
    [defaultCenter addObserver:self selector:@selector(networkDidRegister:) name:kAPNetworkDidRegisterNotification object:nil];
    [defaultCenter addObserver:self selector:@selector(networkDidLogin:) name:kAPNetworkDidLoginNotification object:nil];
    [defaultCenter addObserver:self selector:@selector(networkDidReceiveMessage:) name:kAPNetworkDidReceiveMessageNotification object:nil];
    return YES;
}

- (void)networkDidSetup:(NSNotification *)notification {
    NSLog(@"已连接");
}

- (void)networkDidClose:(NSNotification *)notification {
    NSLog(@"未连接。。。");
}

- (void)networkDidRegister:(NSNotification *)notification {
    NSLog(@"已注册");
}

- (void)networkDidLogin:(NSNotification *)notification {
    NSLog(@"已登录");
}

- (void)networkDidReceiveMessage:(NSNotification *)notification {
    NSDictionary * userInfo = [notification userInfo];
    NSString *title = [userInfo valueForKey:@"title"];
    NSString *content = [userInfo valueForKey:@"content"];
    //NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    
    //[dateFormatter setDateFormat:@"yyyy-MM-dd hh:mm:ss"];
    //NSString dateString=[dateFormatter stringFromDate:<#(NSDate *)#>]
    if(title!=nil&&[title compare:@"chat"]==NSOrderedSame){
        //UIViewController *contr=[[UIApplication sharedApplication] keyWindow].rootViewController;
        for (UIView *view in [[[UIApplication sharedApplication] keyWindow] subviews] ) {
            UIViewController *contr=[self getViewController:view];
            NSLog(contr.title);
            if ([contr.title compare:@"chat"]==NSOrderedSame) {
                ChatView *view=(ChatView *)contr;
                NSArray *temparr=[content componentsSeparatedByString:@"####"];
                if([view.remoteId compare:[temparr objectAtIndex:1]]==NSOrderedSame){
                    [view receiveMsg:[NSDictionary dictionaryWithObjectsAndKeys:view.remoteId,@"name",[temparr objectAtIndex:0],@"content", nil]];
                }
            }
        }
        
    } else {
        
    }


}

- (UIViewController *)getViewController:(UIView *)next {
    
        UIResponder* nextResponder = [next nextResponder];
        if ([nextResponder isKindOfClass:[UIViewController
                                         class]]) {
            return (UIViewController*)nextResponder;
        }
    return nil;
}


-(void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error
{
    NSLog(@"Failed ! error is: %@", error);
}

- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    // Required
    [APService registerDeviceToken:deviceToken];
    }
- (void)application:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo {

// Required
    [APService handleRemoteNotification:userInfo];
    UIViewController *contr=[[UIApplication sharedApplication] keyWindow].rootViewController;
    NSLog(contr.title);
    
    NSNotification *notification=[NSNotification notificationWithName:@"name" object:userInfo];

    
    
}


							
- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
