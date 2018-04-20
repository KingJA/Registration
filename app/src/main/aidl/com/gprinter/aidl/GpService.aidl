// GpService.aidl
package com.gprinter.aidl;

// Declare any non-default types here with import statements

interface GpService {
    int openPort(int PrinterId,int PortType,String DeviceName,int PortNumber);
    	void closePort(int PrinterId);
    	int getPrinterConnectStatus(int PrinterId);
    	int printeTestPage(int PrinterId);
      	int queryPrinterStatus(int PrinterId,int Timesout);
      	int getPrinterCommandType(int PrinterId);
    	int sendEscCommand(int PrinterId, String b64);
      	int sendTscCommand(int PrinterId, String  b64);
}
