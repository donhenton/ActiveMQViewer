/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jmx.service;

/**
 *
 * @author dhenton
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.jms.Connection;
import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a number of methods for talking to activeMQ. These are
 * used in the UI to get a various information broker name: from the jmx section
 * of the activemq server activemq.xml conf file State is maintained for queues
 * and connections and all, so a new connection requires a new instance of this
 * class
 *
 * @author dhenton
 */
public class ActiveMQJmxComm {
    //broker name is from the conf file of the activemq server

    private static final String ACTIVEMQ_BROKER_BEAN_NAME_TEMPLATE = "org.apache.activemq:BrokerName=%s,Type=Broker";
    private static final String CONNECTION_TEMPLATE = "service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi";
    private static final Logger logger = LoggerFactory.getLogger(ActiveMQJmxComm.class);
    private static final int MESSAGE_LIST_MAXIMUM = 250;
    private static final String PROPERTIES_TEXT_KEY = "PropertiesText";
    private MBeanServerConnection connection = null;
    private BrokerViewMBean brokerViewBean = null;
    private String brokerName;
    private int serverPort;
    private String serverName;
    private HashMap<String, QueueViewMBean> queueMap;
    private List<QueueViewMBean> queueList;
    private JMXConnector jmxcf = null;

    public enum QUEUE_PROPS {

        ENQUEUE_COUNT, DEQUEUE_COUNT, EXPIRED_COUNT,
        DISPATCH_COUNT,
        CONSUMER_COUNT, PENDING_MESSAGES
    }

    public ActiveMQJmxComm() {
    }

// service:jmx:rmi:///jndi/rmi://aqserver:9999/server
    public BrokerViewMBean getBrokerViewBean() throws MalformedURLException,
            MalformedObjectNameException, IOException {
        if (brokerViewBean == null) {
            String activeMQBrokerBeanName =
                    String.format(ACTIVEMQ_BROKER_BEAN_NAME_TEMPLATE, getBrokerName());

            ObjectName activeMQ =
                    new ObjectName(activeMQBrokerBeanName);
            brokerViewBean =
                    MBeanServerInvocationHandler.newProxyInstance(getConnection(), activeMQ,
                    BrokerViewMBean.class, true);
        }

        return brokerViewBean;

    }

    public static void main(String[] args) {
        try {
            
            sendJunkMessages();
            doJunkStuff();
        } catch (Exception ex) {
            logger.error("main problem", ex);
        }
    }

    /**
     * get an ArrayList of message Ids for a queue the number of messages is
     * limited to MESSAGE_LIST_MAXIMUM
     *
     * @param qName
     * @param selector jms filter selector or null for all
     * @return null if any problems
     * @throws OpenDataException
     * @throws InvalidSelectorException
     */
    public ArrayList<String> getMessageIDsForQueue(String qName, String selector)
            throws OpenDataException, InvalidSelectorException {
        // this gets the messages eg TextMessage
        // List<?> browseData = q.browseMessages();
        QueueViewMBean q = null;
        ArrayList<String> idList = null;
        // logger.debug("queueName " + qName);
        try {
            q = findQueueBean(qName);

        } catch (Exception ex) {
            logger.error(ex.getClass().getName() + " " + ex.getMessage());
            return null;
        }
        if (q != null) {
            // logger.debug(" 2 queueName " + qName);
            idList = new ArrayList<String>();
            CompositeData[] browseData = q.browse(selector);
            int cc = 0;
            for (CompositeData d : browseData) {
                String messageID =
                        (String) d.get(
                        BASIC_MESSAGE_PROPS.JMS_MessageID.getPropertyValue());

                if (cc < MESSAGE_LIST_MAXIMUM) {
                    idList.add(messageID);
                    cc++;
                } else {
                    break;
                }
            }

        } else {
            logger.warn("could not find queue '" + qName
                    + "' in getMessageIDsForQueue returning null");
            return null;
        }
        return idList;
    }

    /**
     * Get the Basic properties for a message
     *
     * @param qName
     * @param messageID
     * @return map of properties , empty set if not found
     * @throws OpenDataException
     */
    public EnumMap<BASIC_MESSAGE_PROPS, String> getMessageProperties(String qName, String messageID)
            throws OpenDataException {
        logger.debug("starting getMessage Properties");
        EnumMap<BASIC_MESSAGE_PROPS, String> props =
                new EnumMap<BASIC_MESSAGE_PROPS, String>(BASIC_MESSAGE_PROPS.class);
        QueueViewMBean q = null;
        logger.debug("q "+qName+" message "+messageID);
        if (StringUtils.isEmpty(qName) || StringUtils.isEmpty(messageID)) {
            return props;
        }
        try {
            q = findQueueBean(qName);
        } catch (Exception ex) {
            logger.error(ex.getClass().getName() + " " + ex.getMessage());
        }
        if (q != null) {

            CompositeDataSupport messageData =
                    (CompositeDataSupport) q.getMessage(messageID);
           

            for (BASIC_MESSAGE_PROPS dataItem : BASIC_MESSAGE_PROPS.values()) {
                if (dataItem != null) {
                    Object item = messageData.get(dataItem.getPropertyValue());
                    if (item != null) {
                        props.put(dataItem, item.toString());
                    }
                }
            }

        } else {
            logger.warn("could not find queue '" + qName + "' in getMessageProperties returning null");

        }
        return props;
    }

    
     /**
     * Get the User properties for a message
     *
     * @param qName
     * @param messageID
     * @return map of properties , empty set if not found
     * @throws OpenDataException
     */
    public HashMap<String, String> getUserProperties(String qName, String messageID)
            throws OpenDataException {
        HashMap<String, String> props = new HashMap<String, String>();
        QueueViewMBean q = null;

        if (StringUtils.isEmpty(qName) || StringUtils.isEmpty(messageID)) {
            return props;
        }
        try {
            q = findQueueBean(qName);
        } catch (Exception ex) {
            logger.error(ex.getClass().getName() + " " + ex.getMessage());
        }
        if (q != null) {

            CompositeDataSupport messageData =
                    (CompositeDataSupport) q.getMessage(messageID);
            String item = (String) messageData.get(PROPERTIES_TEXT_KEY);
            item = item.replaceAll("}", "");
            item = item.replaceAll("\\{", "");
            String[] items = item.split(",");
            for (String t: items)
            {
                String[] pair = t.split("=");
                props.put(pair[0].trim(),pair[1].trim());
            }
            

        } else {
            logger.warn("could not find queue '" + qName + "' in getUserProperties returning null");

        }
        return props;
    }

    
    
    
    
    /**
     * get the list of queue Viewer Beans
     *
     * @return return the list
     * @throws MalformedURLException
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    public List<QueueViewMBean> getQueueList()
            throws MalformedURLException,
            MalformedObjectNameException, IOException {


        if (queueList == null) {
            queueList = new ArrayList();
            queueMap = new HashMap<String, QueueViewMBean>();
            for (ObjectName name : getBrokerViewBean().getQueues()) {
                QueueViewMBean queueMbean =
                        MBeanServerInvocationHandler.newProxyInstance(getConnection(),
                        name,
                        QueueViewMBean.class, true);

                queueList.add(queueMbean);
                queueMap.put(queueMbean.getName(), queueMbean);

            }



        }

        return queueList;

    }

    private QueueViewMBean findQueueBean(String qName) throws
            MalformedURLException,
            MalformedObjectNameException, IOException {
        if (queueMap == null) {
            getQueueList();
        }
        QueueViewMBean foundBean = queueMap.get(qName);


        return foundBean;
    }

    /**
     * move messages from one queue to another
     *
     * @param sourceQueue
     * @param destQueue
     * @throws MalformedURLException
     * @throws MalformedObjectNameException
     * @throws IOException
     * @throws Exception
     */
    public void move(String sourceQueue, String destQueue)
            throws MalformedURLException, MalformedObjectNameException,
            IOException, Exception {

        QueueViewMBean sourceQueueBean = findQueueBean(sourceQueue);
        if (sourceQueueBean == null) {
            throw new MalformedObjectNameException("could not find queuebean for '" + sourceQueue + "'");
        }

        sourceQueueBean.moveMatchingMessagesTo("", destQueue);
    }

    /**
     * @return the connection
     */
    public MBeanServerConnection getConnection() throws MalformedURLException, IOException {
        if (connection == null) {
            jmxcf = null;
            JMXServiceURL url = new JMXServiceURL(getBrokerUrl());
            jmxcf = JMXConnectorFactory.connect(url);
            connection = jmxcf.getMBeanServerConnection();

        }

        return connection;
    }

    /**
     * call this code when the form closes
     *
     * @throws IOException
     */
    public void close() throws IOException {
        jmxcf.close();

    }

    /**
     * @return the brokerUrl
     */
    private String getBrokerUrl() {


        return String.format(CONNECTION_TEMPLATE,
                getServerName(), getServerPort() + "");
    }

    /**
     * @return the brokerName
     */
    public String getBrokerName() {
        return brokerName;
    }

    /**
     * @param brokerName the brokerName to set, from the jmx element in the
     * server conf file
     */
    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    /**
     * @return the serverPort
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * @param serverPort the serverPort to set
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @param serverName the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * return the size of a queue
     *
     * @param selectedQueue
     * @return
     * @throws MalformedURLException
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    Long getQueueCount(String selectedQueue) throws MalformedURLException,
            MalformedObjectNameException, IOException {
        Long qCount = null;
        QueueViewMBean qBean = findQueueBean(selectedQueue);
        qCount = qBean.getQueueSize();
        return qCount;


    }

    /**
     * return the properties of a queue
     *
     * @param queueName
     * @return
     * @throws MalformedURLException
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    HashMap<String, String> getQueueProperties(String queueName)
            throws MalformedURLException,
            MalformedObjectNameException, IOException {
        QueueViewMBean qBean = findQueueBean(queueName);
        HashMap<String, String> props = new HashMap<String, String>();
        props.put(QUEUE_PROPS.DEQUEUE_COUNT.toString(), qBean.getDequeueCount() + "");
        props.put(QUEUE_PROPS.ENQUEUE_COUNT.toString(), qBean.getEnqueueCount() + "");
        props.put(QUEUE_PROPS.EXPIRED_COUNT.toString(), qBean.getExpiredCount() + "");
        props.put(QUEUE_PROPS.DISPATCH_COUNT.toString(), qBean.getDispatchCount() + "");
        props.put(QUEUE_PROPS.CONSUMER_COUNT.toString(), qBean.getConsumerCount() + "");
        props.put(QUEUE_PROPS.PENDING_MESSAGES.toString(), qBean.getQueueSize() + "");


        return props;

    }

    private static void sendJunkMessages() {
        Connection conn = null;
        try {
            conn = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616").createConnection();
            conn.start();
            ActiveMQQueue tomcatQueue = new ActiveMQQueue("fred");
            Session queueSession = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //  QueueBrowser browser = queueSession.createBrowser();

            MessageProducer producer = queueSession.createProducer(tomcatQueue);
            TextMessage jmsMessage = queueSession.createTextMessage();
            jmsMessage.setText("get a job!");
            jmsMessage.setBooleanProperty("Boolean", true);
            jmsMessage.setLongProperty("Long property", 45L);
            jmsMessage.setStringProperty("String", "string property");
            jmsMessage.setStringProperty("Bozo", "get a job!");
            producer.send(jmsMessage);

            //  logger.debug("#### "+browser.getQueue().getQueueName());

        } catch (JMSException ex) {
            logger.error("JMS Error " + ex.getMessage());
        } finally {
            try {
                conn.stop();
                conn.close();
            } catch (JMSException ex) {
            }
        }

    }

    private static void doJunkStuff() throws OpenDataException, IOException, InvalidSelectorException, MalformedObjectNameException {
        ActiveMQJmxComm c = new ActiveMQJmxComm();
        c.setBrokerName("ubuntu");
        c.setServerName("localhost");
        c.setServerPort(2011);
        long t = c.getQueueCount("fred");
        logger.debug("fred count " + t);
        List<String> mIds = c.getMessageIDsForQueue("fred", null);
        if (mIds.size() > 0) {
            String mId = mIds.get(0);
            EnumMap<BASIC_MESSAGE_PROPS, String> props = c.getMessageProperties("fred", mId);
            Set<BASIC_MESSAGE_PROPS> basicKeys = props.keySet();
            for (BASIC_MESSAGE_PROPS j : basicKeys) {
                logger.debug(j + " --> " + props.get(j));
            }
            HashMap<String, String> userMap = c.getUserProperties("fred", mId);
           
            for (String k:userMap.keySet())
            {
                logger.debug(k + " --> " + userMap.get(k));
            }
             

        }
    }
}