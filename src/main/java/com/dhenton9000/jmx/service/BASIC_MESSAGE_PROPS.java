/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jmx.service;

/**
 *
 * @author dhenton
 */
    public enum BASIC_MESSAGE_PROPS {

        JMS_Correlation_ID("JMSCorrelationID"),
        MS_Delivery_Mode("JMSDeliveryMode"),
        JMS_Destination("JMSDestination"),
        JMS_MessageID("JMSMessageID"),
        JMS_Priority("JMSPriority"),
        JMS_Redelivered("JMSRedelivered"),
        JMS_Timestamp("JMSTimestamp"),
        JMS_Type("JMSType"),
        JMS_XGroupSeq("JMSXGroupSeq")     ;
          
        private String propertyValue;

        private BASIC_MESSAGE_PROPS(String t) {
            propertyValue = t;
        }
        
        public String getPropertyValue(){return propertyValue;}
    }