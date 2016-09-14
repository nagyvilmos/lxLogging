/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexa.core.logging;

/**
 *
 * @author william
 */
interface LogFile
{

    void close();
    
    void flush();

    void write(Message message);

}
