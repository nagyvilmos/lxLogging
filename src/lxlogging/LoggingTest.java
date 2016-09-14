/*
 * ================================================================================
 * Lexa - Property of William Norman-Walker
 * --------------------------------------------------------------------------------
 * LoggingTest.java
 *--------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2013
 *--------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Ref:        Description:
 * ----------   --- ----------  --------------------------------------------------
 * 2016-09-13   WNW             Update in line with lxData-16-09
 * 2016-09-14   WNW             Add support to output the log to a DataWriter
 *================================================================================
 */

package lxlogging;

import lexa.core.data.SimpleDataSet;
import lexa.core.data.config.ConfigDataSet;
import lexa.core.data.exception.DataException;
import lexa.core.logging.Logger;

/**
 * Test the log writer works
 * <br>
 * Sets the config and then submits a single message. 
 * @author william
 * @since 2013-05
 */
public class LoggingTest {
    public static void main(String ... args)
            throws DataException
    {
        Logger.configure(
                new ConfigDataSet(
                        new SimpleDataSet()
                            .put("type","file")
                            .put("file","logging.log")
                )
        );
        new Logger("LoggingTest", "Test").message("test","this is the test", new SimpleDataSet().put("A","B"), new IllegalArgumentException("an exception"), " ", "plus arguments");
        Logger.close();
    }
    
}
