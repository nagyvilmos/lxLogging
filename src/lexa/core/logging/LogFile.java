/*
 * ================================================================================
 * Lexa - Property of William Norman-Walker
 * --------------------------------------------------------------------------------
 * LogWriter.java
 *--------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: September 2016
 *--------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Ref:        Description:
 * ----------   --- ----------  --------------------------------------------------
 * 2016-09-14   WNW             Add support to output the log to a DataWriter
 * 2016-09-14   WNW             Update javadoc
 *================================================================================
 */
package lexa.core.logging;

/**
 * Interface for the logger file
 * @author william
 * @since 2016-09
 */
interface LogFile
{

    /**
     * Close the file
     */
    void close();
    
    /**
     * Flush the file of all updates
     * <br>
     * A flush is called after a set of messages have been written
     */
    void flush();

    /**
     * Write s single message to the log
     * @param message the message to be logged
     */
    void write(Message message);

}
