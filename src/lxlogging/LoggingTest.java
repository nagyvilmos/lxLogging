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

import lexa.core.data.ArrayDataSet;
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
    /**
     * Test the logger is working correctly
     * @param args not needed
     */
    public static void main(String ... args)
    {
        try
        {
            Logger.configure(
                    new ConfigDataSet(
                            new ArrayDataSet()
                                .put("type","dataSet")
                                .put("file",".\\log\\logging.log")
                    )
            );
            new Logger("LoggingTest", "Test").message("test","this is the test", new ArrayDataSet().put("A","B"), new IllegalArgumentException("an exception"), " ", "plus arguments");
            Logger.close();
        }
        catch (DataException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
    
}
