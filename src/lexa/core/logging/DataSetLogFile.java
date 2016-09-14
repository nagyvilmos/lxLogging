/*
 * ================================================================================
 * Lexa - Property of William Norman-Walker
 * --------------------------------------------------------------------------------
 * DataSetLogWriter.java
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import lexa.core.data.formatting.DateTimeFormat;
import lexa.core.data.io.DataWriter;

/**
 * A log file written to a DAtaWriter
 * @author william
 * @since 2016-09
 */
class DataSetLogFile
        implements LogFile
    {

    /** the data writer */
    private final DataWriter writer;

    /**
     * Create a data writer to a file
     * <br>
     * Creates a new writer, if the named file exists, it is archived.
     * @param file the file for writing the log 
     * @throws FileNotFoundException when the file could not be found
     */
    public DataSetLogFile(File file)
    {
        //this.writer = new DataWriter(file);
		if (file == null) {
            throw new IllegalArgumentException("Null log stream");
		}
        DataWriter writer = null;
        try
        {
            if (!file.exists()) {
                File parent = file.getParentFile();
                // does the directory?
                if (parent != null && !parent.exists())
                {
                    parent.mkdirs();
                }
            }
            else
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
            writer = new DataWriter(file);
        }
        catch (IOException ex)
        {
            new Logger("Logger","static").error("Rename failed", null, ex);
        }
        this.writer=writer;
    }

    /**
     * Create a log file for a data writer
     * @param writer the writer for the log
     */
    public DataSetLogFile(DataWriter writer)
    {        
        this.writer = writer;
    }

    @Override
    public void close()
    {
        try
        {
            this.writer.close();
        } catch (IOException ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public void flush()
    {
        // can't flush
    }

    @Override
    public void write(Message message)
    {
        try
        {
            this.writer.write(message.getData());
        } catch (IOException ex)
        {
            System.err.println("lexa.core.logging.DataSetLogFile.write()");
            ex.printStackTrace(System.err);
        }
    }
    
}
