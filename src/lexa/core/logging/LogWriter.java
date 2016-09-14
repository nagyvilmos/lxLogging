/*
 * ================================================================================
 * Lexa - Property of William Norman-Walker
 * --------------------------------------------------------------------------------
 * LogWriter.java
 *--------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: June 2013
 *--------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Ref:        Description:
 * ----------   --- ----------  --------------------------------------------------
 * 2016.02.10   WNW             Tidy up to make code more consistant with standards
 * 2016-09-14   WNW             Add support to output the log to a DataWriter
 * 2016-09-14   WNW             Update javadoc
 *================================================================================
 */
package lexa.core.logging;

import java.util.*;
import java.util.logging.Logger;
import lexa.core.data.*;

/**
 * A single stream, shared by all the {@link Logger} instances for writing out log messages.
 *
 * @author William
 * @since 2013-06
 */
class LogWriter
{
    /** the log file for writing messages */
    private final LogFile logFile;
    /** Messages */
    private final List<Message> messages;

    /**
     * Create a new writer using the standard output {@link System#out}
     * <br>
     * This is equivalent to using {@code LogWriter(System.out)}.
     */
    LogWriter() {
        this(new StreamLogFile());
    }

    /**
     * Create a new writer using the supplied stream.
     *
     * @param   log
     *          a stream to write out log messages.
     */
    LogWriter (LogFile logFile) {
        this.logFile = logFile;
        this.messages = new LinkedList<>();
        this.writeMessage("LogWriter", "START", "Logging started", null, null);
    }

    /**
     * Writes a message out to the log.
     * <br>
     * The message is written in the toString:
     * <pre>
     * nnn  Day YYYY.MM.DD HH:MM:SS.SSS +0100	name	TYPE
     * message
     *   [data]
     *   [throwable]
     * </pre>
     * @param   name
     *          The name of the log; used to distinguish area of logging.
     * @param   type
     *          The type of message; e.g. DEBUG, INFO, ERROR
     * @param   message
     *          The text of the message
     * @param   data
     *          An associated data set, this will be formatted when printed
     * @throwable
     *          Any {@link Throwable} to associated with the message.
     */
    void message(String name,
            String type,
            String message,
            DataSet data,
            Throwable throwable,
            Object ... args)
    {
        writeMessage(name, type, message, data, throwable, args);
    }
    synchronized private void writeMessage(String name,
            String type,
            String message,
            DataSet data,
            Throwable throwable,
            Object ... args)
    {
        this.messages.add(new Message(name, type, message, data, throwable, args));
        if (this.messages.size() > 1)
            return;
        
        while (this.messages.size() >0)
        {
            this.logFile.write(this.messages.get(0));
            this.messages.remove(0);
        }
        this.logFile.flush();
    }

    /**
     * Close the log writer
     */
    void close()
    {
        this.logFile.close();
    }
}
