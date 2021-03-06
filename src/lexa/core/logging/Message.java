/*
/*
 * ================================================================================
 * Lexa - Property of William Norman-Walker
 * --------------------------------------------------------------------------------
 * Message.java
 *--------------------------------------------------------------------------------
 * Author:  William Norman-Walker
 * Created: June 2015
 *--------------------------------------------------------------------------------
 * Change Log
 * Date:        By: Ref:        Description:
 * ----------   --- ----------  --------------------------------------------------
 * 2016-09-14   WNW             Add support to output the log to a DataWriter
 * 2016-09-14   WNW             Update javadoc
 *================================================================================
 */
package lexa.core.logging;

import java.io.PrintStream;
import java.util.Date;
import lexa.core.data.ArrayDataArray;
import lexa.core.data.DataItem;
import lexa.core.data.DataSet;
import lexa.core.data.ArrayDataSet;
import lexa.core.data.DataArray;
import lexa.core.data.DataType;
import lexa.core.data.DataValue;
import lexa.core.data.formatting.DateTimeFormat;

/**
 * A single message to be logged
 * @author william
 * @since 2015-06
 */
class Message
{
    /** Format for writing the date */
    //private static final ArrayDateFormat dateFormat =
    //  new ArrayDateFormat ("E yyyy.MM.dd HH:mm:ss.SSS Z");
    private static final DateTimeFormat DATE_TIME_FORMAT = new DateTimeFormat("E yyyy.MM.dd HH:mm:ss.SSS Z");
    private static long lastMessage = 0;
    /**
     * Get the date and time formatted for output.
     * @param date the date to format
     * @return  the formatted date and time
     */
    private static String formattedDate(Date date) {
        return Message.DATE_TIME_FORMAT.toString(new Date());
    }
    private final Date dateStamp;
    private final String name;
    private final String type;
    private final String message;
    private final DataSet data;
    private final Throwable throwable;
    private final Object[] args;
    private final String id;


    Message(String name,
            String type,
            String message,
            DataSet data,
            Throwable throwable,
            Object ... args) {
        this.id = Long.toHexString(Message.lastMessage++);
        this.dateStamp = new Date();
        this.name = name;
        this.type = type.toUpperCase();
        this.message = message;
        this.data = data;
        this.throwable = throwable;
        this.args = args;
    }

    void print(PrintStream stream) {
        stream.print(this.id);
        stream.print('\t');
        stream.print(Message.formattedDate(this.dateStamp));
        stream.print('\t');
        stream.print(this.name);
        stream.print('\t');
        stream.print(this.type);
        stream.print('\n');
        stream.print(this.message);
        if (this.args != null)
        {
            for (Object obj : args)
            {
                stream.print(obj);
            }
        }
        stream.print('\n');
        if (data != null) {
            this.printDataSet(stream, "\t",data);
        }
        if (throwable != null) {
            stream.print("Exception:\n");
            throwable.printStackTrace(stream);
        }
        stream.print('\n');
    }
    /**
     * Write the content of a {@link DataSet} to the log stream.
     * @param   data
     *          a {@link DataSet} to write to the log stream.
     * @param   prefix
     *          a string to write prior to each {@link DataItem}
     */
    private void printDataSet(PrintStream stream, String prefix, DataSet data) {
        if (data == null || data.isEmpty()) {
            stream.print(prefix);
            stream.println("[empty]");
        }
        for (DataItem item : data) {
            this.printDataValue(stream, prefix +  item.getKey(), item.getValue());
        }
    }
    /**
     * Write the content of a {@link DataSet} to the log stream.
     * @param   data
     *          a {@link DataSet} to write to the log stream.
     * @param   indent
     *          a string to write prior to each {@link DataItem}
     */
    private void printDataValue(PrintStream stream, String prefix, DataValue value) {
        DataType type = value.getType();
        switch (type)
        {
            case ARRAY :
            {
                DataArray array = value.getArray();
                for (int i = 0; i < array.size(); i++)
                {
                    this.printDataValue(stream, prefix + "[" + i + "]", array.get(i));
                }
                break;
            }
            case DATA_SET :
            {
                this.printDataSet(stream, "\t" + prefix + ".",value.getDataSet());
                break;
            }
            default:
            {
                stream.print(prefix);
                stream.print(type.getTypeChar());
                Object obj = value.getObject();
                if (obj != null) {
                    stream.print('\t');
                    stream.print(obj);
                }
                else
                {
                    stream.print("\t[null]");
                }
                stream.print('\n');
                break;
            }
        }
    }

    DataSet getData()
    {
        String fullMessage = message;
            if (this.args != null)
            {
                for (Object obj : args)
                {
                    fullMessage = fullMessage + obj;
                }
            }
        DataSet msgData = new ArrayDataSet()
                .put("dateStamp",this.dateStamp)
                .put("name",this.name)
                .put("type",this.type)
                .put("message",fullMessage);
        if (this.data != null) {
            msgData.put("data",data);
        }
        if (this.throwable != null) {
            DataArray stack = new ArrayDataArray();
            for (StackTraceElement ste : this.throwable.getStackTrace())
            {
                stack.add(ste.toString());
            }
            msgData.put("exception",
                    new ArrayDataSet()
                        .put("message",this.throwable.getMessage())
                        .put("stack",stack)
            );
        }
        return new ArrayDataSet()
                .put(
                        this.id,
                        msgData
                );
    }

}
