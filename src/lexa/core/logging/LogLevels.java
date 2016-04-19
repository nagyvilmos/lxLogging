/*
 * ================================================================================
 * Lexa - Property of William Norman-Walker
 * --------------------------------------------------------------------------------
 * LogLevels.java
 *--------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: August 2013
 *--------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Ref:        Description:
 * ----------   --- ----------  --------------------------------------------------
 * -            -   -           -
 *================================================================================
 */
package lexa.core.logging;

import lexa.core.data.ConfigData;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.data.SimpleDataSet;
import lexa.core.data.ValueType;
import lexa.core.data.exception.DataException;

/**
 * Configured logging levels.
 * <p>Logging can be set up for each class and each message type.
 * <p>With no settings, logging is on for all messages.  In a production environment
 * the {@code DEBUG} type messages should be disabled.
 *
 * @author William
 * @since YYYY-MM
 */
public class LogLevels {

    private final static String WILD_CARD = "*";
    /** a tree containing the log levels */
    private final DataSet logTree;

    private Logger logger;
    /**
     * Create an empty set for logging.
     * <p>This sets up logging for all classes and all types to be on using the wildcard class and type.
     */
    LogLevels() {
        this.logTree = new SimpleDataSet()
				.put(LogLevels.WILD_CARD, new SimpleDataSet()
						.put(LogLevels.WILD_CARD,true));
    }

    /**
     * Indicates if a message should be logged.
     * <p>If no class is set up, returns the the type from the wildcard name.
     * <p>If the class exists but the item is not set-up returns the type wildcard for the name.
     * <p>If the type wildcard does not exists returns the name wildcard and type wildcard.
     *
     * @param   name
     *          the name of the logging class
     * @param   type
     *          the type of the logging message.
     * @return  {@code true} if the message should be logged,
     *          otherwise {@code false}
     */
    public boolean isLogged (String name, String type) {
        DataSet nameSet = this.logTree.getDataSet(name);
        if (nameSet == null) {
            return this.isLogged(LogLevels.WILD_CARD, type);
        }
        DataItem typeItem = nameSet.get(type);
        if (typeItem == null) {
            if (LogLevels.WILD_CARD.equals(type)) {
                return this.isLogged(LogLevels.WILD_CARD, LogLevels.WILD_CARD);
            }
            return this.isLogged(name, LogLevels.WILD_CARD);
        }
        return typeItem.getBoolean();
    }

    /**
     * Set the logging for a group of names and types.
     * <p>The structure of the config is names containing types as used in
     * {@link LogLevels#setLogging(java.lang.String, java.lang.String, boolean)
     * setLogging(String, String, boolean}}.
     * @param   data
     *          The configuration for the logging.
     */
    public void setLogging(DataSet data) {
        this.logger().info("Update log levels", data);
		if (data == null)
		{
			return;
		}
        try {
            for (DataItem di : data)
            {
                if (!di.getType().equals(ValueType.DATA_SET))
                {
                    throw new DataException("invalid log setting format",di.getKey());
                }
                DataSet logLevels = this.logTree.getDataSet(di.getKey());
                if (logLevels == null)
                {
                    logLevels = new SimpleDataSet();
                    this.logTree.put(di.getKey(), logLevels);
                }
                DataSet newLevels = di.getDataSet();
                for (DataItem ni : newLevels)
                {
                    if (!ni.getType().equals(ValueType.BOOLEAN))
                    {
                        throw new DataException("invalid log setting format",di.getKey(),ni.getKey());
                    }
                    logLevels.put(ni);
                }
            }
        } catch (DataException ex) {
            this.logger().error("Cannot set logging", data, ex);
        }
    }

    /**
     * Set the logging for a group of names and types.
     * <p>The structure of the config is names containing types as used in
     * {@link LogLevels#setLogging(java.lang.String, java.lang.String, boolean)
     * setLogging(String, String, boolean}}.
     * @param   config
     *          The configuration for the logging.
     */
    public void setLogging(ConfigData config) {
        try {
            this.setLogging(config.getAll());
        } catch (DataException ex) {
            this.logger().error("Cannot set logging", null, ex);
        }

    }

    /**
     * Set the logging for a name and type.
     *
     * @param   name
     *          the name of the class; use {@code *} for the wildcard name
     * @param   type
     *          the type of the message; use {@code *} for the wildcard type
     * @param   islogged
     *          {@code true} if the message should be logged,
     *          otherwise {@code false}
     */
    public void setLogging (String name, String type, boolean islogged) {
        this.setLogging(
                new SimpleDataSet().put(name,
                        new SimpleDataSet().put(type, islogged)));
    }

    
    private Logger logger()
    {
        if (this.logger == null)
        {
            this.logger = new Logger(LogLevels.class.getSimpleName(),null);
        }
        return this.logger;
    }
}
