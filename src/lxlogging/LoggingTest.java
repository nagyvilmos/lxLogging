/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lxlogging;

import lexa.core.data.SimpleDataSet;
import lexa.core.logging.Logger;

/**
 *
 * @author william
 */
public class LoggingTest {
    public static void main(String ... args) {
        new Logger("LoggingTest", "Test").message("test","this is the test", new SimpleDataSet().put("A","B"), new IllegalArgumentException("an exception"), " ", "plus arguments");
        Logger.close();
    }
    
}
