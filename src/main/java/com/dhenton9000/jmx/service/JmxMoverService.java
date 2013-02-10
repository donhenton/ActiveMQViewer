/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jmx.service;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author dhenton
 */
public interface JmxMoverService {
    /**
     * get a string list of queue names
     * @return 
     */
    List<String> getQueueList();
    /**
     * set or reset the service so it points to a server
     * @param brokerName the broker name from jmx element 
     * in the server conf file
     * @param serverName DNS listing fo the activemq server
     * @param serverPort server port
     * @return errorMessage or null if all is okay
     */
    public String set(String brokerName, String serverName, int serverPort);
    /**
     * whether the set method was called successfully
     * @return 
     */
    public boolean isSetSuccessful();
    /**
     * move messages from one queue to another
     * @param selectedFromQueue
     * @param selectedToQueue 
     * @return errorMessage or null if all is okay
     */
    public String moveMessages(String selectedFromQueue, String selectedToQueue);

    public void close();

    public Long getQueueCount(String selectedQueue);
    /**
     * return the properties of a queue the key is the property name
     * the value the current value
     * @param queueName
     * @return null if failure or the map
     */
    public HashMap<String, String> getQueueProperties(String queueName);
    
    /**
     * for a give message Id on a queue, return properties
     * @param messageId
     * @return hashmap of properties, or empty list if nothing found
     */
    public HashMap<String,String> getMessageProperties(String qName, String messageId);
    
    /**
     * get a list of message ids for a given queue
     * @param qName
     * @return message ids or an empty list if nothing found
     */
    public List<String> getQueueMessageIds(String qName);
    
}
