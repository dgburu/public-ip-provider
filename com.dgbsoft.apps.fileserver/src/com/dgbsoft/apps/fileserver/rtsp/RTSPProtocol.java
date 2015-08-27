package com.dgbsoft.apps.fileserver.rtsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.InetAddress;
import java.util.StringTokenizer;

import com.dgbsoft.apps.fileserver.IAction;
import com.dgbsoft.apps.fileserver.Protocol;

public class RTSPProtocol extends Protocol {
	
	private final static int INIT = 0;
	private final static int READY = 1;
	private final static int PLAYING = 2;
	  //rtsp message types
	private final static int SETUP = 3;
	private final static int PLAY = 4;
	private final static int PAUSE = 5;
	private final static int TEARDOWN = 6;


	private InetAddress clientAddress = null;
	private int state = INIT;
	
	
	private String VideoFileName;
	private int RTSPSeqNb;
	private int RTP_dest_port;

	
	
	public RTSPProtocol(InetAddress clientAddress) {
		this.clientAddress = clientAddress;
	}

	@Override
	public IAction getAction() {
		
		return null;
	}
	
	private int parse_RTSP_request(BufferedReader inputReader) {
	    int request_type = -1;
	    try {
	        //parse request line and extract the request_type:
	        String RequestLine = inputReader.readLine();
	        //System.out.println("RTSP Server - Received from Client:");
	        System.out.println(RequestLine);

	        StringTokenizer tokens = new StringTokenizer(RequestLine);
	        String request_type_string = tokens.nextToken();

		    //convert to request_type structure:
		    if ((new String(request_type_string)).compareTo("SETUP") == 0) {
		    	request_type = SETUP;
		    } else if ((new String(request_type_string)).compareTo("PLAY") == 0) {
		    	request_type = PLAY;
		    } else if ((new String(request_type_string)).compareTo("PAUSE") == 0) {
		    	request_type = PAUSE;
			} else if ((new String(request_type_string)).compareTo("TEARDOWN") == 0) {
				request_type = TEARDOWN;
			}
	
		    if (request_type == SETUP) {
		    	//extract VideoFileName from RequestLine
		    	VideoFileName = tokens.nextToken();
		    }

		    //parse the SeqNumLine and extract CSeq field
		    String SeqNumLine = inputReader.readLine();
		    System.out.println(SeqNumLine);
		    tokens = new StringTokenizer(SeqNumLine);
		    tokens.nextToken();
		    RTSPSeqNb = Integer.parseInt(tokens.nextToken());
			
		    //get LastLine
		    String LastLine = inputReader.readLine();
		    System.out.println(LastLine);

	        if (request_type == SETUP) {
	    	    //extract RTP_dest_port from LastLine
	    	    tokens = new StringTokenizer(LastLine);
	    	    for (int i=0; i<3; i++) {
	    		    tokens.nextToken(); //skip unused stuff
	    	    }
	    	    RTP_dest_port = Integer.parseInt(tokens.nextToken());
		    }
	        //else LastLine will be the SessionId line ... do not check for now.
	    }
	    catch(Exception ex) {
		    System.out.println("Exception caught: "+ex);
		    System.exit(0);
	    }
	    return(request_type);
	}
	
}
