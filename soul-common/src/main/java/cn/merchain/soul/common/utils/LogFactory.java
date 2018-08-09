package cn.merchain.soul.common.utils;

import org.apache.commons.logging.Log;

public class LogFactory
{
    private static LogFactory instance = null;
    public static LogFactory Instance ()
    {
        if (instance == null)
        {
            instance = new LogFactory();
        }
        return instance;
    }

    private Log log = null;

    private LogFactory()
    {
        this.log = org.apache.commons.logging.LogFactory.getLog("soul logs");
    }

    public Log getLog ()
    {
        return this.log;
    }
}
