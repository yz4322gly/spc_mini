package com.ruoyi.common.utils;

/**
 * @author guolinyuan
 */
public class ErrorUtil
{
    public static String getST(Exception e)
    {
        StringBuilder sb = new StringBuilder(e.getMessage() + "\n");
        for (StackTraceElement element : e.getStackTrace())
        {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
