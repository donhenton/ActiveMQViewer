/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jmx.service;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.jms.InvalidSelectorException;
import javax.management.MalformedObjectNameException;
import javax.management.openmbean.OpenDataException;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class JmxBrokerServiceImpl implements JmxBrokerService {

    private final Logger logger = LoggerFactory.getLogger(JmxBrokerServiceImpl.class);
    private ActiveMQJmxComm jmxComm = null;
    private boolean setSuccessful = false;

    public List<String> getQueueList() {
        ArrayList<String> queues = new ArrayList<String>();
        if (isSetSuccessful()) {
            try {
                List<QueueViewMBean> queueBeans = jmxComm.getQueueList();
                for (QueueViewMBean qq : queueBeans) {
                    queues.add(qq.getName());
                }
            } catch (Exception ex) {
                logger.error("problem n getQueueList: " + ex.getClass().getName() + " " + ex.getMessage());
            }
        }

        return queues;
    }

    public String set(String brokerName, String serverName, int serverPort) {
        String errorMessage = null;
        jmxComm = new ActiveMQJmxComm();
        jmxComm.setBrokerName(brokerName);
        jmxComm.setServerName(serverName);
        jmxComm.setServerPort(serverPort);
        
        if (StringUtils.isEmpty(brokerName) || StringUtils.isEmpty(serverName))
        {
            setSuccessful = false;
            errorMessage = "Broker Name and Server Name cannot be empty.";
            return errorMessage;
            
        }
        
        
        // attempt to get the queueList
        try {
            jmxComm.getQueueList();
        } catch (MalformedURLException ex) {
            errorMessage = "Malformed url of some type: " + ex.getMessage();
        } catch (MalformedObjectNameException ex) {
            errorMessage = "Object Name problem: " + ex.getMessage();
        } catch (IOException ex) {
            errorMessage = "IO problem: " + ex.getMessage();
        } catch (UndeclaredThrowableException uex) {
            errorMessage = "Possible problem with brokerName '" + brokerName + "'";
        }
        if (errorMessage == null) {
            setSuccessful = true;
        } else {
            setSuccessful = false;
        }
        return errorMessage;
    }

    public boolean isSetSuccessful() {
        return setSuccessful;
    }

    public void close() {
        try {
            jmxComm.close();
        } catch (IOException ex) {
        }
    }

    public String moveMessages(String selectedFromQueue, String selectedToQueue) {
        String errorMessage = null;
        try {
            jmxComm.move(selectedFromQueue, selectedToQueue);
        } catch (Exception ex) {
            errorMessage = "Error class: " + ex.getClass().getName() + "\n";
            errorMessage += "Message: " + ex.getMessage() + "\n";

        }
        return errorMessage;
    }

    public Long getQueueCount(String selectedQueue) {
        Long qCount = null;
        String errorMessage = "";
        if (isSetSuccessful()) {
            try {
                qCount = new Long(jmxComm.getQueueCount(selectedQueue));
            } catch (Exception ex) {
                errorMessage = "Error class: " + ex.getClass().getName() + "\n";
                errorMessage += "Message: " + ex.getMessage() + "\n";
                logger.error("In getQueueCount " + errorMessage);

            }

        }

        return qCount;
    }

    public HashMap<String, String> getQueueProperties(String queueName) {
        HashMap<String, String> props = null;
        if (queueName == null) {
            return null;
        }
        String errorMessage = "";

        if (isSetSuccessful()) {
            try {
                props = jmxComm.getQueueProperties(queueName);


            } catch (Exception ex) {
                errorMessage = "Error class: " + ex.getClass().getName() + "\n";
                errorMessage += "Message: " + ex.getMessage() + "\n";
                logger.error("In getQueueProperties " + errorMessage);
                props = null;

            }

        }

        return props;
    }

    /**
     * filed under the propertyValue
     * @param qName
     * @param messageId
     * @return 
     */
    public HashMap<String, String> getMessageProperties(String qName, String messageId) {
        HashMap<String, String> props = new HashMap<String, String>();
        if (StringUtils.isEmpty(qName)  || StringUtils.isEmpty(messageId))
        {
            return props;
        }
        
        String errorMessage = null;
        String errorMarker = "";
        if (isSetSuccessful()) {
            errorMarker += " 1 ";
            try {
                EnumMap<BASIC_MESSAGE_PROPS, String> map =
                        jmxComm.getMessageProperties(qName, messageId);
                errorMarker += " 2 "+map;
                for (BASIC_MESSAGE_PROPS k : map.keySet()) {
                    props.put(k.getPropertyValue(), map.get(k));
                }
                errorMarker += " 3 ";


            } catch (Exception ex) {
                errorMessage = "Error class: " + ex.getClass().getName() + "\n";
                errorMessage += "Message: " + ex.getMessage() + "\n";
                errorMessage += errorMarker;
                logger.error("In getMessageProperties " + errorMessage);
            }


        }// end if setSuccessful

        return props;

    }

    public List<String> getQueueMessageIds(String qName) {
        List<String> idList = new ArrayList<String>();
        String errorMessage = null;
        if (isSetSuccessful()) {
            try {
               idList =  jmxComm.getMessageIDsForQueue(qName, null);
            } catch (Exception ex) {
                errorMessage = "Error class: " + ex.getClass().getName() + "\n";
                errorMessage += "Message: " + ex.getMessage() + "\n";
                logger.error("In getQueueMessageIds " + errorMessage);
            }


        }// end if setSuccessful

        return idList;
    }

    public HashMap<String, String> getUserProperties(String qName, String messageId) {
       
        HashMap<String, String> props = new HashMap<String, String>();
        String errorMessage = "";
        try {
            return  jmxComm.getUserProperties(qName, messageId);
        } catch (OpenDataException ex) {
           errorMessage = "Error class: " + ex.getClass().getName() + "\n";
                errorMessage += "Message: " + ex.getMessage() + "\n";
                logger.error("In getUserProperties " + errorMessage);
        }
        return props;
    }

    public String getMessageText(String queueName,String messageId) {
         
        return jmxComm.getMessageText(queueName,messageId);
    }
}
