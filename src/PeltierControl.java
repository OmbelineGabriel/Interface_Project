
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

public class PeltierControl {

	/*
	 * Class that controls the peltier device. CONNECTION IS DODGY, PLEASE FIND A BETTER WAY!
	 * Uses bluecove java module.
	 */
	
	static boolean peltierReady;
	static boolean peltierConnected;
	static int currentTemp, currentTemp2, response;
	static OutputConnection notifier;
	static OutputStream outputStream;
	static InputStream inputStream;

	/*
	 * Method establishes a connection	 to the peltier device. Method, upon creating connection, opens 
	 * output and input streams for reading and writing to the peltier.
	 */
	public static void init() {
		peltierConnected = false;
		try {
			peltierReady = false;
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			final DiscoveryAgent discAgent = localDevice.getDiscoveryAgent();
			discAgent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener(){

				public void inquiryCompleted(int arg0) {
					// TODO Auto-generated method stub
					
				}

				public void serviceSearchCompleted(int arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}

				public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
					// TODO Auto-generated method stub
					try {
						notifier = (OutputConnection) Connector.open(arg1[0].getConnectionURL(ServiceRecord.
						         AUTHENTICATE_ENCRYPT, false));
						outputStream = notifier.openOutputStream();
						InputConnection notifier_in = (InputConnection) notifier;
						inputStream = notifier_in.openInputStream();
						peltierConnected = true;	
						peltierReady = true;
						setPeltierTemperature(300);
						InterfaceView.peltierStatus.setText("Ready");

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

				public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
					// TODO Auto-generated method stub
					try {
						System.out.println("DEVICE: " + arg0.getFriendlyName(true));
						if (arg0.getFriendlyName(true).equals("SK HP2 SN0000")){
							UUID uuidSet[] = new UUID[1];
							uuidSet[0]     = new UUID("0000110100001000800000805F9B34FB", false);
							int searchID  = discAgent.searchServices(null,uuidSet,
					                                                 arg0,this);
							discAgent.cancelInquiry(this);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			});
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*
	 * Method takes an integer value for temperature and sends a command to the peltier to set that temperature
	 */
	public static void setPeltierTemperature(int temp){
		if (peltierReady){
			if (peltierConnected){
				String thermalString1 = "$CMD," + parseFixedLengthHex(temp, 4) + ",01";
				String thermalString2 = "$CMD," + parseFixedLengthHex(temp, 4) + ",02";
				try {
					outputStream.write(thermalString1.getBytes());
					outputStream.flush();
					while (inputStream.available() > 0)
					{
						inputStream.read();
					}
					outputStream.write(thermalString2.getBytes());
					outputStream.flush();
					while (inputStream.available() > 0)
					{
						inputStream.read();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
		
		public static int getPeltierTemperature()
		{
			if(peltierReady)
			{
				if (peltierConnected)
				{
					String readTemp1 = "$CMD,0001,05";
					String readTemp2 = "$CMD,0002,05";
					try 
					{
						while (inputStream.available() > 0)
						{
							inputStream.read();
						}
						outputStream.write(readTemp1.getBytes());
						outputStream.flush();
						while (inputStream.available() < 12)
						{
						}						
						byte[] b = new byte[12]; 
						inputStream.read(b, 0, 12);
						String str1 = new String(b, StandardCharsets.UTF_8);
						System.out.println(str1);
						String hex1 = getHex(str1);
						currentTemp = parseHexToDec(hex1);						
						
						while (inputStream.available() > 0)
						{
							inputStream.read();
						}
						outputStream.write(readTemp2.getBytes());
						outputStream.flush();
						while (inputStream.available() < 12)
						{
						}	
						b = new byte[12];
						inputStream.read(b, 0, 12);
						String str2 = new String(b, StandardCharsets.UTF_8);
						System.out.println(str2);
						String hex2 = getHex(str2);
						currentTemp2 = parseHexToDec(hex2);
						
						System.out.println("Displaying temp1: "+currentTemp);
						System.out.println("Displaying temp1: "+currentTemp2);			
						
						while (inputStream.available() > 0)
						{
							inputStream.read();
						}
					} 
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return currentTemp;
		}
	
	public static String getHex(String ack)
	{
		//int hex;
		String temp_hex = ack.substring(5, 9);
		System.out.println(temp_hex);
		//hex = new Integer(temp_hex).intValue();
		return temp_hex;
	}
		
		
	/**
	 * Converts integer value to string of fixed length that represents the hex value of that integer
	 * @param value		value to be converted
	 * @param length	minimum length of output
	 * @return
	 */
	public static String parseFixedLengthHex(int value, int length){
		String hexString = Integer.toHexString(value);
		while (hexString.length() < length){
			hexString = '0' + hexString;
		}
		return hexString;
	}
	
	public static int parseHexToDec(String hex)
	{
		try{
			response = Integer.parseInt(hex, 16);
			System.out.println("hex: " +hex+ " -> decimal: " + response);
		}
		catch(NumberFormatException ne){
			System.out.println("Invalid Input");
		}
		return response;

	}

	/*
	 * Method is called to cool the peltier and stops access to new restaurant windows being access. When
	 * cooldown is complete peltierReady is set to true. Method updates the title text in the main window
	 * to inform user of the status of the Peltier.
	 */
	public static void cooldown(final long l) {
		setPeltierTemperature(300);
		Thread cooldownThread = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				peltierReady = false;
				String timeVal_str;
				long timeVal;
				for (int i = 0; i < l; i++) {
					timeVal = l - i;
					if (timeVal < 10) {
						timeVal_str = "0" + timeVal;
					} else {
						timeVal_str = "" + timeVal;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(peltierConnected){
					peltierReady = true;
				}
			}

		});
		cooldownThread.start();
	}
}
