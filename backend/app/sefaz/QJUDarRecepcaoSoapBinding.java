package sefaz;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 5.11.3.0
//
// Created by Quasar Development 
//
//----------------------------------------------------



import org.ksoap2.HeaderProperty;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.*;
import play.Play;

import java.util.ArrayList;
import java.util.List;


public class QJUDarRecepcaoSoapBinding
{
    interface QJUIWcfMethod
    {
        QJUExtendedSoapSerializationEnvelope CreateSoapEnvelope() throws Exception;

        Object ProcessResult(QJUExtendedSoapSerializationEnvelope __envelope, Object result) throws Exception;
    }

    private static String url = Play.configuration.getProperty("sefaz.url.wsdl");

    int timeOut=60000;
    private List< HeaderProperty> httpHeaders= new ArrayList< HeaderProperty>();
    private boolean enableLogging;


    public List< HeaderProperty> getHttpHeaders()
    {
        return this.httpHeaders;
    }
    
    public boolean getEnableLogging()
    {
        return this.enableLogging;
    }
    
    public void setEnableLogging(boolean value)
    {
        this.enableLogging=value;
    }
    

    
    public QJUDarRecepcaoSoapBinding(){}

    public QJUDarRecepcaoSoapBinding(String url)
    {
        this.url = url;
    }

    public QJUDarRecepcaoSoapBinding(String url,int timeOut)
    {
        this.url = url;
        this.timeOut=timeOut;
    }

    protected Transport createTransport()
    {
        try
        {
            java.net.URI uri = new java.net.URI(url);
            if(uri.getScheme().equalsIgnoreCase("https"))
            {
                int port=uri.getPort()>0?uri.getPort():443;
                String path=uri.getPath();
                if(uri.getQuery()!=null && uri.getQuery()!="")
                {
                    path+="?"+uri.getQuery();
                }
                return new com.easywsdl.exksoap2.transport.AdvancedHttpsTransportSE(uri.getHost(), port, path, timeOut);
            }
            else
            {
                return new com.easywsdl.exksoap2.transport.AdvancedHttpTransportSE(url,timeOut);
            }

        }
        catch (java.net.URISyntaxException e)
        {
        }
        return null;
    }

    protected QJUExtendedSoapSerializationEnvelope createEnvelope()
    {
        QJUExtendedSoapSerializationEnvelope envelope= new QJUExtendedSoapSerializationEnvelope(QJUExtendedSoapSerializationEnvelope.VER11);
        envelope.setEnableLogging( enableLogging);
    
        return envelope;
    }

    protected List sendRequest(String methodName, QJUExtendedSoapSerializationEnvelope envelope, Transport transport , com.easywsdl.exksoap2.ws_specifications.profile.WS_Profile profile )throws Exception
    {
        if(transport instanceof com.easywsdl.exksoap2.transport.AdvancedHttpTransportSE )
        {
            return ((com.easywsdl.exksoap2.transport.AdvancedHttpTransportSE)transport).call(methodName, envelope,httpHeaders,null,profile);
        }
        else
        {
            return ((com.easywsdl.exksoap2.transport.AdvancedHttpsTransportSE)transport).call(methodName, envelope,httpHeaders,null,profile);
        }
    }

    Object getResult(Class destObj, Object source, String resultName, QJUExtendedSoapSerializationEnvelope __envelope) throws Exception
    {
        if(source==null)
        {
            return null;
        }
        if(source instanceof SoapPrimitive)
        {
            SoapPrimitive soap =(SoapPrimitive)source;
            if(soap.getName().equals(resultName))
            {
                Object instance=__envelope.get(source,destObj,false);
                return instance;
            }
        }
        else
        {
            SoapObject soap = (SoapObject)source;
            if (soap.hasProperty(resultName))
            {
                Object j=soap.getProperty(resultName);
                if(j==null)
                {
                    return null;
                }
                Object instance=__envelope.get(j,destObj,false);
                return instance;
            }
            else if( soap.getName().equals(resultName)) 
            {
                Object instance=__envelope.get(source,destObj,false);
                return instance;
            }
        }

        return null;
    }

    
    
    
    public String processar(final String darCabecalho,final String darDados) throws Exception
    {
        com.easywsdl.exksoap2.ws_specifications.profile.WS_Profile __profile = new com.easywsdl.exksoap2.ws_specifications.profile.WS_Profile();
        return (String)execute(new QJUIWcfMethod()
        {
            @Override
            public QJUExtendedSoapSerializationEnvelope CreateSoapEnvelope(){
                QJUExtendedSoapSerializationEnvelope __envelope = createEnvelope();
                SoapObject __soapReq = new SoapObject("http://www.daravulso.ap.gov.br", "processar");
                __envelope.setOutputSoapObject(__soapReq);
                
                PropertyInfo __info=null;
                __info = new PropertyInfo();
                __info.namespace="http://www.daravulso.ap.gov.br";
                __info.name="darCabecalho";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(darCabecalho);
                __soapReq.addProperty(__info);
                __info = new PropertyInfo();
                __info.namespace="http://www.daravulso.ap.gov.br";
                __info.name="darDados";
                __info.type=PropertyInfo.STRING_CLASS;
                __info.setValue(darDados);
                __soapReq.addProperty(__info);
                return __envelope;
            }
            
            @Override
            public Object ProcessResult(QJUExtendedSoapSerializationEnvelope __envelope, Object __result)throws Exception {
                SoapObject __soap=(SoapObject)__result;
                Object obj = __soap.getProperty("processarReturn");
                if (obj instanceof SoapPrimitive)
                {
                    SoapPrimitive j =(SoapPrimitive) obj;
                    return j.toString();
                }
                else if (obj!= null && obj instanceof String){
                    return (String)obj;
                }
                return null;
            }
        },"",__profile);
    }

    protected Object execute(QJUIWcfMethod wcfMethod,String methodName,com.easywsdl.exksoap2.ws_specifications.profile.WS_Profile profile) throws Exception
    {
        Transport __httpTransport=createTransport();
        __httpTransport.debug=enableLogging;
        QJUExtendedSoapSerializationEnvelope __envelope=wcfMethod.CreateSoapEnvelope();
        try
        {
            sendRequest(methodName, __envelope, __httpTransport,profile);
        }
        finally {
            if (__httpTransport.debug) {
                if (__httpTransport.requestDump != null) {
                    System.out.println("requestDump: "+__httpTransport.requestDump);

                }
                if (__httpTransport.responseDump != null) {
                    System.out.println("responseDump: "+__httpTransport.responseDump);
                }
            }
        }
        Object __retObj = __envelope.bodyIn;
        if (__retObj instanceof org.ksoap2.SoapFault){
            org.ksoap2.SoapFault __fault = (org.ksoap2.SoapFault)__retObj;
            throw convertToException(__fault,__envelope);
        }else{
            return wcfMethod.ProcessResult(__envelope,__retObj);
        }
    }


    protected Exception convertToException(org.ksoap2.SoapFault fault,QJUExtendedSoapSerializationEnvelope envelope)
    {
        org.ksoap2.SoapFault newException = fault;
        return newException;
    }
}

