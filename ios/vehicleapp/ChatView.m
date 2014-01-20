//
//  ChatViewController.m
//  vehicleapp
//
//  Created by will on 14-1-18.
//  Copyright (c) 2014年 vehicleapp. All rights reserved.
//

#import "ChatView.h"
#import "ChatCell.h"
#import "NetworkUtil.h"

@interface ChatView ()

@end

@implementation ChatView

- (void)viewDidLoad
{
    [super viewDidLoad];
    NSDictionary *dict = [NSDictionary dictionaryWithObjectsAndKeys:@"weixin",@"name",@"微信团队欢迎你。很高兴你开启了微信生活，期待能为你和朋友们带来愉快的沟通体检。",@"content", nil];
    NSDictionary *dict1 = [NSDictionary dictionaryWithObjectsAndKeys:@"rhl",@"name",@"hello",@"content", nil];
    NSDictionary *dict2 = [NSDictionary dictionaryWithObjectsAndKeys:@"rhl",@"name",@"0",@"content", nil];
    NSDictionary *dict3 = [NSDictionary dictionaryWithObjectsAndKeys:@"weixin",@"name",@"谢谢反馈，已收录。",@"content", nil];
    NSDictionary *dict4 = [NSDictionary dictionaryWithObjectsAndKeys:@"rhl",@"name",@"0",@"content", nil];
    NSDictionary *dict5 = [NSDictionary dictionaryWithObjectsAndKeys:@"weixin",@"name",@"谢谢反馈，已收录。",@"content", nil];
    NSDictionary *dict6 = [NSDictionary dictionaryWithObjectsAndKeys:@"rhl",@"name",@"大数据测试，长数据测试，大数据测试，长数据测试，大数据测试，长数据测试，大数据测试，长数据测试，大数据测试，长数据测试，大数据测试，长数据测试。",@"content", nil];
    
    _resultArray = [NSMutableArray arrayWithObjects:dict, nil];
    NSUserDefaults* memo=[NSUserDefaults standardUserDefaults];
    _remoteId=[memo stringForKey:@"remoteId"];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}
//点击屏幕空白处去掉键盘
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.sMsg resignFirstResponder];
}


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_resultArray count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *dict = [_resultArray objectAtIndex:indexPath.row];
    UIFont *font = [UIFont systemFontOfSize:14];
	CGSize size = [[dict objectForKey:@"content"] sizeWithFont:font constrainedToSize:CGSizeMake(180.0f, 20000.0f) lineBreakMode:NSLineBreakByWordWrapping];
    
    return size.height+44;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *CellIdentifier = @"ChatCell";
    ChatCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[ChatCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
    }
    
    NSMutableDictionary *dict = [_resultArray objectAtIndex:indexPath.row];
    [cell setContent:dict];
    
    return cell;
    
}

- (IBAction)sendMsg:(id)sender {
    NSString *msg=_sMsg.text;
    if([msg compare:@""]!=NSOrderedSame){
        NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:@"18755",@"source",@"18755",@"target",msg,@"content", nil];
        [NetworkUtil imPost:@"/rest/message/one2one" :dic];
        NSDictionary *dic2 = [NSDictionary dictionaryWithObjectsAndKeys:@"rhl",@"name",msg,@"content", nil];
        [self receiveMsg:dic2];
    }
    
}

-(void)receiveMsg:(NSDictionary *)msg{
    
    [_resultArray addObject:msg];
    NSIndexPath *index=[NSIndexPath indexPathForRow:_resultArray.count-1 inSection:0];
    [_table insertRowsAtIndexPaths:[NSArray arrayWithObject:index] withRowAnimation:UITableViewRowAnimationBottom];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.sMsg resignFirstResponder];
    [self receiveMsg:[_resultArray objectAtIndex:indexPath.row]];
}

@end
