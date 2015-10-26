//
//  CDVBaiduLocationSdk.h
//  Hello
//
//  Created by scuhmz on 10/9/15.
//
//

#import "CDVBaiduLocationSdk.h"
#import <Cordova/CDV.h>
#import <BaiduMapAPI_Location/BMKLocationComponent.h>
#import <BaiduMapAPI_Search/BMKSearchComponent.h>



@interface CDVBaiduLocationSdk : CDVPlugin <BMKGeoCodeSearchDelegate , BMKLocationServiceDelegate>{
  
  BMKMapManager* _mapManager;
  BMKLocationService* _locService;
  BMKGeoCodeSearch* _geocodesearch;
  NSMutableDictionary*_addrinfos;
  
  
}

- (void)getLocation:(CDVInvokedUrlCommand *)command;
@end
