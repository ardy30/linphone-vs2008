/*
LinphoneCore.java
Copyright (C) 2010  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package org.linphone.core;


import java.util.Vector;




	
public interface LinphoneCore {
	/*
	 * linphone core states
	 */
	static public class 	GeneralState {
		static private Vector values = new Vector();
		/* states for GSTATE_GROUP_POWER */
		static public GeneralState GSTATE_POWER_OFF = new GeneralState(0,"GSTATE_POWER_OFF");        /* initial state */
		static public GeneralState GSTATE_POWER_STARTUP = new GeneralState(1,"GSTATE_POWER_STARTUP");
		static public GeneralState GSTATE_POWER_ON = new GeneralState(2,"GSTATE_POWER_ON");
		static public GeneralState GSTATE_POWER_SHUTDOWN = new GeneralState(3,"GSTATE_POWER_SHUTDOWN");
		/* states for GSTATE_GROUP_REG */
		static public GeneralState GSTATE_REG_NONE = new GeneralState(10,"GSTATE_REG_NONE");       /* initial state */
		static public GeneralState GSTATE_REG_OK  = new GeneralState(11,"GSTATE_REG_OK");
		static public GeneralState GSTATE_REG_FAILED = new GeneralState(12,"GSTATE_REG_FAILED");
		static public GeneralState GSTATE_REG_PENDING = new GeneralState(13,"GSTATE_REG_PENDING");
		/* states for GSTATE_GROUP_CALL */
		static public GeneralState GSTATE_CALL_IDLE = new GeneralState(20,"GSTATE_CALL_IDLE");      /* initial state */
		static public GeneralState GSTATE_CALL_OUT_INVITE = new GeneralState(21,"GSTATE_CALL_OUT_INVITE");
		static public GeneralState GSTATE_CALL_OUT_CONNECTED = new GeneralState(22,"GSTATE_CALL_OUT_CONNECTED");
		static public GeneralState GSTATE_CALL_IN_INVITE = new GeneralState(23,"GSTATE_CALL_IN_INVITE");
		static public GeneralState GSTATE_CALL_IN_CONNECTED = new GeneralState(24,"GSTATE_CALL_IN_CONNECTED");
		static public GeneralState GSTATE_CALL_END = new GeneralState(25,"GSTATE_CALL_END");
		static public GeneralState GSTATE_CALL_ERROR = new GeneralState(26,"GSTATE_CALL_ERROR");
		static public GeneralState GSTATE_INVALID = new GeneralState(27,"GSTATE_INVALID");
		static public GeneralState GSTATE_CALL_OUT_RINGING = new GeneralState(28,"GSTATE_CALL_OUT_RINGING");
		private final int mValue;
		private final String mStringValue;

		private GeneralState(int value,String stringValue) {
			mValue = value;
			values.addElement(this);
			mStringValue=stringValue;
		}
		public static GeneralState fromInt(int value) {

			for (int i=0; i<values.size();i++) {
				GeneralState state = (GeneralState) values.elementAt(i);
				if (state.mValue == value) return state;
			}
			throw new RuntimeException("state not found ["+value+"]");
		}
		public String toString() {
			return mStringValue;
		}
	}

	static public class Transport {
		public final static Transport udp =new Transport("udp");
		public final static Transport tcp =new Transport("tcp");
		private final String mStringValue;

		private Transport(String stringValue) {
			mStringValue=stringValue;
		}
		public String toString() {
			return mStringValue;
		}		
	}
	/**
	 * clear all added proxy config
	 */
	public void clearProxyConfigs();
	
	public void addProxyConfig(LinphoneProxyConfig proxyCfg) throws LinphoneCoreException;

	public void setDefaultProxyConfig(LinphoneProxyConfig proxyCfg);
	
	/**
	 * @return null if no default proxy config 
	 */
	public LinphoneProxyConfig getDefaultProxyConfig() ;
	
	/**
	 * clear all the added auth info
	 */
	void clearAuthInfos();
	
	void addAuthInfo(LinphoneAuthInfo info);
	
	/**
	 * Build an address according to the current proxy config. In case destination is not a sip address, the default proxy domain is automatically appended
	 * @param destination
	 * @return
	 * @throws If no LinphoneAddress can be built from destination
	 */
	public LinphoneAddress interpretUrl(String destination) throws LinphoneCoreException;
	
	/**
	 * Starts a call given a destination. Internally calls interpretUrl() then invite(LinphoneAddress).
	 * @param uri
	 */
	public void invite(String destination)throws LinphoneCoreException;
	
	public void invite(LinphoneAddress to)throws LinphoneCoreException;
	
	public void terminateCall();
	/**
	 * get the remote address in case of in/out call
	 * @return null if no call engaged yet
	 */
	public LinphoneAddress getRemoteAddress();
	/**
	 *  
	 * @return  TRUE if there is a call running or pending.
	 */
	public boolean isIncall();
	/**
	 * 
	 * @return Returns true if in incoming call is pending, ie waiting for being answered or declined.
	 */
	public boolean isInComingInvitePending();
	public void iterate();
	/**
	 * Accept an incoming call.
	 *
	 * Basically the application is notified of incoming calls within the
	 * {@link LinphoneCoreListener#inviteReceived(LinphoneCore, String)} listener.
	 * The application can later respond positively to the call using
	 * this method.
	 * @throws LinphoneCoreException 
	 */
	public void acceptCall() throws LinphoneCoreException;
	
	
	/**
	 * @return a list of LinphoneCallLog 
	 */
	public Vector getCallLogs();
	
	/**
	 * This method is called by the application to notify the Linphone core library when network is reachable.
	 * Calling this method with true trigger Linphone to initiate a registration process for all proxy
	 * configuration with parameter register set to enable.
	 * This method disable the automatic registration mode. It means you must call this method after each network state changes
	 * @param network state  
	 *
	 */
	public void setNetworkStateReachable(boolean isReachable);
	/**
	 * destroy linphone core and free all underlying resources
	 */
	public void destroy();
	/**
	 * Allow to control play level before entering  sound card:  
	 * @param level in db
	 */
	public void setPlaybackGain(float gain);
	/**
	 * get play level before entering  sound card:  
	 * @return level in db
	 */
	public float getPlaybackGain();
	/**
	 * set play level
	 * @param level [0..100]
	 */
	public void setPlayLevel(int level);
	/**
	 * get playback level [0..100];
	 * -1 if not cannot be determined
	 * @return
	 */
	public int getPlayLevel();
	/**
	 *  Mutes or unmutes the local microphone.
	 * @param isMuted
	 */
	public void muteMic(boolean isMuted);
	/**
	 * 
	 * @return true is mic is muted
	 */
	public boolean isMicMuted();
	
	/**
	 * Initiate a dtmf signal if in call
	 * @param number
	 */
	public void sendDtmf(char number);
	/**
	 * 
	 */
	public void clearCallLogs();
	
	
	/***
	 * get payload type  from mime type an clock rate
	 * 
	 * return null if not found
	 */
	public PayloadType findPayloadType(String mime,int clockRate); 
	
	public void enablePayloadType(PayloadType pt, boolean enable) throws LinphoneCoreException;
	
	public void enableEchoCancellation(boolean enable);
	
	public boolean isEchoCancellationEnabled();
	
	public void setSignalingTransport(Transport aTransport);

}
