// Copyright (c) 2011 Bhavin Bharat Joshi - Neustar, Inc. All Rights Reserved.

package urlFunctions;

import java.net.URLEncoder;
import java.net.URLDecoder;

import org.apache.commons.codec.binary.Base64;

import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;

import java.util.zip.InflaterOutputStream;
import java.util.zip.Inflater;

public class urlFunctions 
{   
    def log;
    
	// Constructors
	urlFunctions(log) 
    {
		this.log = log;
	}	
	urlFunctions() 
    {
	}
    
    // Methods  
    def String urlEncode(def userInput)
    {    
        def urlEncodedOutput = "";
        
        try
        {  
            URLEncoder urlEncoder = new URLEncoder();        
            urlEncodedOutput = urlEncoder.encode(userInput.toString(), "UTF-8");
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
        }
        
        if (this.log)
		{
			this.log.info "The URL Encoded String for your input is '" + urlEncodedOutput + "'.";
		}  
        
        return urlEncodedOutput;                                                                        
    } 
    
    def String urlDecode(def userInput)
    {    
        def urlDecodedOutput = "";
        
        try
        {  
            URLDecoder urlDecoder = new URLDecoder();        
            urlDecodedOutput = urlDecoder.decode(userInput.toString(), "UTF-8");
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
        }
        
        if (this.log)
		{
			this.log.info "The URL Decoded String for your input is '" + urlDecodedOutput + "'.";
		}  
        
        return urlDecodedOutput;                                                                        
    } 
    
    def String base64Encode(def userInput)
    {    
        def base64EncodedOutput = "";
        
        try
        {  
            byte[] inputBytes = userInput.getBytes("UTF-8");         
            Base64 base64Encoder = new Base64();
            byte[] base64EncodedByteArray = base64Encoder.encode(inputBytes);
            base64EncodedOutput = new String(base64EncodedByteArray);            
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
        }
        
        if (this.log)
		{
			this.log.info "The Base64 Encoded String for your input is '" + base64EncodedOutput + "'.";
		}  
        
        return base64EncodedOutput;                                                                        
    }
    
    def String base64Decode(def userInput)
    {    
        def base64DecodedOutput = "";
        
        try
        {  
            byte[] inputBytes = userInput.getBytes("UTF-8");         
            Base64 base64Decoder = new Base64();
            byte[] base64DecodedByteArray = base64Decoder.decode(inputBytes);
            base64DecodedOutput = new String(base64DecodedByteArray);            
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
        }
        
        if (this.log)
		{
			this.log.info "The Base64 Decoded String for your input is '" + base64DecodedOutput + "'.";
		}  
        
        return base64DecodedOutput;                                                                        
    }
    
    def String deflateBase64Encode(def userInput)
    {    
        def deflatedBase64EncodedOutput = "";
        
        try
        {  
            byte[] inputBytes = userInput.getBytes("UTF-8");         
            Deflater compresser = new Deflater(9, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, compresser);
            deflaterOutputStream.write(inputBytes, 0, inputBytes.length);
            deflaterOutputStream.close();
                   
            Base64 base64Encoder = new Base64();
            byte[] base64EncodedByteArray = base64Encoder.encode(byteArrayOutputStream.toByteArray());
            deflatedBase64EncodedOutput = new String(base64EncodedByteArray);             
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
        }
        
        if (this.log)
		{
			this.log.info "The Deflated and Base64 Decoded String for your input is '" + deflatedBase64EncodedOutput + "'.";
		}  
        
        return deflatedBase64EncodedOutput;                                                                        
    } 
    
    def String base64DecodeInflate(def userInput)
    {    
        def base64DecodedInflatedOutput = "";
        
        try
        {  
            byte[] inputBytes = userInput.getBytes("UTF-8");         
            Base64 base64Decoder = new Base64();
            byte[] base64DecodedByteArray = base64Decoder.decode(inputBytes);
            //base64DecodedOutput = new String(base64DecodedByteArray);
            
            Inflater decompresser = new Inflater(true);
            decompresser.setInput(base64DecodedByteArray);
            byte[] inflatedOutput = new byte[1024];
            int inflatedOutputLength = decompresser.inflate(inflatedOutput);
            decompresser.end();
            
            base64DecodedInflatedOutput = new String(inflatedOutput, 0, inflatedOutputLength, "UTF-8");                                      
            
            
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
        }
        
        if (this.log)
		{
			this.log.info "The Base64 Decoded and Inflated String for your input is '" + base64DecodedInflatedOutput + "'.";
		}  
        
        return base64DecodedInflatedOutput;                                                                        
    }  
}
