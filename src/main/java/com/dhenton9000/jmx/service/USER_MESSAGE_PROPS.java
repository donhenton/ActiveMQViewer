/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.jmx.service;

/**
 *
 * @author dhenton
 */
    public enum USER_MESSAGE_PROPS {

        
        BOOLEAN_PROPERTIES("BooleanProperties"),
        BYTE_PROPERTIES("ByteProperties"),
        DOUBLE_PROPERTIES("DoubleProperties"),
        FLOAT_PROPERTIES("FloatProperties"),
        INT_PROPERTIES("IntProperties"),
        LONG_PROPERTIES("LongProperties"),
        PROPERTIES_TEXT("PropertiesText"),
        SHORT_PROPERTIES("ShortProperties"),
        STRING_PROPERTIES("StringProperties")     ;
          
        private String propertyValue;

        private USER_MESSAGE_PROPS(String t) {
            propertyValue = t;
        }
        
        public String getPropertyValue(){return propertyValue;}
    }