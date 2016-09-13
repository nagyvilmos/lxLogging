/*
 * ================================================================================
 * Lexa - Property of William Norman-Walker
 * --------------------------------------------------------------------------------
 * Logger.java
 *--------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: May 2013
 *--------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Ref:        Description:
 * ----------   --- ----------  --------------------------------------------------
 * 2013-08-08   WNW -           Split name and instance to support log levels.
 * 2013-09-19	WNW -			An existing log file will be archived before
 *								opening the stream to write to it.
 * 2016-09-13   WNW             Update in line with lxData-16-09 changes to config
 *================================================================================
 */
package lexa.core.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Date;
import lexa.core.data.DataSet;
import lexa.core.data.config.ConfigDataSet;
import lexa.core.data.exception.DataException;
import lexa.core.data.formatting.DateTimeFormat;

/**
 * A logger is used to write messages to a shared output stream.
 * <p>In normal use, each class instance would use an instance of the logger.
 * Each logger has a name and supports several standard messages with pre-defined types
 * as well as a generic message for application specific types.
 *
 * @author William
 * @since 2013-04
 */
public class Logger {
    /** Text for DEBUG log messages */
    private static final String DEBUG   = "DEBUG";
    /** Text for ERROR log messages */
    private static final String ERROR   = "ERROR";
    /** Text for INFO log messages */
    private static final String INFO    = "INFO";
    /** Text for START log messages */
    private static final String START   = "START";

    /** The {@link LogWriter} shared by all the {@link Logger} objects. */
    private static LogWriter logWriter;
    /** The {@link LogLevels} shared by all the {@link Logger} objects. */
    private static final LogLevels LOG_LEVELS = new LogLevels();

    public static void close() {
        Logger.logWriter.message("Logger", "CLOSE", "Closing log stream",null,null);
    }
    /** The name to assign for all this instance's messages */
    private final String name;
    private final String className;

    /**
     * Create a logger using a given name.
     *
     * @param   className
     *          the name of the class to apply to all messages.
     * @param   instance
     *          the name of the instance to apply to all messages.
     */
    public Logger (String className, String instance) {
		if (Logger.logWriter == null) {
			Logger.logWriter = new LogWriter();
		}
        this.className = className;
        this.name = (instance == null ?
                        "" :
                        instance + "@") +
				this.className;
        this.message(Logger.START,"Start logging",null, null);
    }

    /**
     * Create a message of type DEBUG
     *
     * @param message
     */
    /**
     * Write a debug message to the log.
     *
     * @param   message
     *          the text for this message
     */
    public void debug(String message) {
        this.debug(message, null);
    }

    /**
     * Write a debug message to the log.
     *
     * @param   message
     *          the text for this message
     * @param   data
     *          a {@link DataSet} to include in the message
     */
    public void debug(String message, DataSet data) {
        this.message(Logger.DEBUG, message, data, null);
    }
    /**
     * Write a debug message to the log.
     *
     * @param   message
     *          the text for this message
     * @param   data
     *          a {@link DataSet} to include in the message
     * @param   args
     *          additional arguments to append to the message.
     */
    public void debug(String message, DataSet data, Object ... args) {
        this.message(Logger.DEBUG, message, data, null, args);
    }

    /**
     * Write an error message to the log.
     *
     * @param   message
     *          the text for this message
     */
    public void error(String message) {
        this.error(message,null);
    }
    /**
     * Write an error message to the log.
     *
     * @param   message
     *          the text for this message
     * @param   throwable
     *          a {@link Throwable} exception to include in the message
     */
    public void error(String message, Throwable throwable) {
        this.error(message, null, throwable);
    }

    /**
     * Write an error message to the log.
     *
     * @param   message
     *          the text for this message
     * @param   data
     *          a {@link DataSet} to include in the message
     * @param   throwable
     *          a {@link Throwable} exception to include in the message
     */
    public void error(String message, DataSet data, Throwable throwable) {
        this.message(Logger.ERROR, message, data, throwable);
    }
    /**
     * Write an error message to the log.
     *
     * @param   message
     *          the text for this message
     * @param   data
     *          a {@link DataSet} to include in the message
     * @param   throwable
     *          a {@link Throwable} exception to include in the message
     * @param   args
     *          additional arguments to append to the message.
     */
    public void error(String message, DataSet data, Throwable throwable, Object ... args) {
        this.message(Logger.ERROR, message, data, throwable, args);
    }

    /**
     * Write an information message to the log.
     *
     * @param   message
     *          the text for this message
     */
    public void info(String message) {
        this.info(message, null);
    }


    /**
     * Write an information message to the log.
     *
     * @param   message
     *          the text for this message
     * @param   data
     *          a {@link DataSet} to include in the message
     */
    public void info(String message, DataSet data) {
        this.message(Logger.INFO, message, data, null);
    }
    /**
     * Write an information message to the log.
     *
     * @param   message
     *          the text for this message
     * @param   data
     *          a {@link DataSet} to include in the message
     * @param   args
     *          additional arguments to append to the message.
     */
    public void info(String message, DataSet data, Object ... args) {
        this.message(Logger.INFO, message, data, null,args);
    }

//    /**
//     * Write a message to the log.
//     *
//     * @param   type
//     *          the type of message to be written
//     * @param   message
//     *          the text for this message
//     * @param   data
//     *          a {@link DataSet} to include in the message
//     * @param   throwable
//     *          a {@link Throwable} exception to include in the message
//     */
//    public synchronized final void message(String type, String message, DataSet data, Throwable throwable) {
//        if (!Logger.logLevels().isLogged(this.className, type)) {
//            return;
//        }
//        Logger.logWriter.message(this.name,type, message, data, throwable);
//    }

    /**
     * Write a message to the log.
     *
     * @param   type
     *          the type of message to be written
     * @param   message
     *          the text for this message
     * @param   data
     *          a {@link DataSet} to include in the message
     * @param   throwable
     *          a {@link Throwable} exception to include in the message
     * @param   args
     *          additional arguments to append to the message.
     */
    public synchronized final void message(String type, String message, DataSet data, Throwable throwable, Object ... args) {
        if (!Logger.logLevels().isLogged(this.className, type)) {
            return;
        }
//        StringBuilder sb = new StringBuilder(message);
//        for (Object obj : args)
//        {
//            sb.append(obj);
//        }
        Logger.logWriter.message(this.name,type, message, data, throwable,args);
    }

    /**
     * Configure the log writer being used by this logger.
     * The logging is configured useing configuration data in the following
     * format:
     * <pre>[type - &lt;Type of logging; takes the values {@code stdout|file|dataSet}, default is {@code stdout}&gt;]
     * [file - &lt;File for logging; not required for {@code stdout}, optional for {@code dataSet}&gt;]
     * [levels {
     *   &lt;Log levels as expected by {@link LogLevels#setLogging(lexa.core.data.DataSet) setLogging(DataSet)}&gt;
     * }]</pre>
     * At least one of the items is required.
     * @param   config
     *          Configuration data as described above.
     * @throws  lexa.core.data.exception.DataException
     *          when there is an error in the configuration.
     */
    public synchronized static void configure(ConfigDataSet config)
            throws DataException
    {
        final String TYPE = "type";
        final String TYPE_STDOUT = "stdout";
        final String TYPE_FILE = "file";
        final String TYPE_DATA_SET = "dataSet";
        final String FILE = "file";
        final String LEVELS = "levels";
        
        if (config.isEmpty())
        {
            throw new DataException("Empty configuration", config.getPath());
        }

        if (config.contains(LEVELS))
        {
            Logger.logLevels().setLogging(config.getDataSet(LEVELS));
        }

        String type = config.getString(TYPE);
        switch (type)
        {
            case TYPE_STDOUT : 
            {
                if (config.contains(FILE))
                {
                    throw new DataException("Invalid configuration item", config.getPath(), FILE);
                }
                Logger.setLogWriter(System.out);
                break;
            }
            case TYPE_FILE : 
            {
                if (!config.contains(FILE))
                {
                    throw new DataException("Missing configuration option", config.getPath(), FILE);
                }
                try
                {
                    Logger.setLogWriter(
                            new File(config.getString(FILE))
                    );
                } catch (FileNotFoundException ex)
                {
                    throw new DataException("Unable to set logging file", config.getPath(), FILE, ex);
                }
                break;
            }
            case TYPE_DATA_SET :
            {
                throw new DataException("Option not implimented - dataSet", config.getPath(), TYPE);
            }
            default :
            {
                throw new DataException("Invalid logging type", config.getPath(), TYPE);
            }
        }
        config.close();
        // write a message that this has been logged:
        Logger.logWriter.message("Logger", "CONFIGURE", "Logging configuration updated",config,null);
    }

    public synchronized static void setLogWriter(File file) throws FileNotFoundException {
		if (file == null) {
            throw new IllegalArgumentException("Null log stream");
		}
		if (!file.exists()) {
			// does the directory?
			file.getParentFile().mkdirs();
		}
		else
		{
			try
			{
				// move to archive
				String path = file.getParent() + "//archive//";
				String timeStamp = new DateTimeFormat(".yyyyMMdd_HHmmss_SSS.").toString(
						new Date(file.lastModified()));
				String archiveName = file.getName().replaceFirst("\\.", timeStamp);
				File archive = new File(path,archiveName);
				archive.getParentFile().mkdirs();
				Files.move(file.toPath(), archive.toPath());
			}
			catch (IOException ex)
			{
				new Logger("Logger","static").error("Rename failed", null, ex);
			}
		}
		setLogWriter(new PrintStream(file));
	}
    /**
     * Set the log writer to use the supplied stream.
     *
     * @param   log
     *          a stream to write out log messages.
     */
    private synchronized static void setLogWriter(PrintStream log) {
        if (Logger.logWriter != null) {
            Logger.close();
        }
        Logger.logWriter = new LogWriter(log);
    }

    /**
     * Get the logging levels.
     * @return  the logging levels
     */
    public static synchronized LogLevels logLevels() {
        return Logger.LOG_LEVELS;
    }
}
