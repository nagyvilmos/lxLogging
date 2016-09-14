/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexa.core.logging;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Date;
import lexa.core.data.formatting.DateTimeFormat;

/**
 *
 * @author william
 */
class StreamLogFile
        implements LogFile
{
    /** Stream to write to */
    private final PrintStream log;

    StreamLogFile()
    {
        this(System.out);
    }
    StreamLogFile(File file)
    {
		if (file == null) {
            throw new IllegalArgumentException("Null log stream");
		}
        PrintStream stream = null;
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
            stream = new PrintStream(file);
        }
        catch (IOException ex)
        {

            new Logger("Logger","static").error("Rename failed", null, ex);
        }
        this.log=stream;
    }
    StreamLogFile(PrintStream log)
    {
        this.log = log;
    }

    @Override
    public void close()
    {
        this.log.close();
    }

    @Override
    public void flush()
    {
        this.log.flush();
    }

    @Override
    public void write(Message message)
    {
        try
        {
            message.print(this.log);
        }
        catch (Exception ex)
        {
            this.log.print("\n**ERROR IN LOGGING**\n");
            ex.printStackTrace(this.log);
            this.log.print("****\n\n");
        }
    }

    
}
