/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexa.core.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexa.core.data.io.DataWriter;

/**
 *
 * @author william
 */
class DataSetLogFile
        implements LogFile
{
    private final DataWriter writer;

    public DataSetLogFile(File file)
            throws FileNotFoundException
    {
        this(new DataWriter(file));
    }

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
