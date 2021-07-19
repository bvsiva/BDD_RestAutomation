package Utils;


import org.junit.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class JavaUtils{

    public static String getGlobalValue(String key) throws IOException// to fetch data from property file
    {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//Configs//global.properties");
        prop.load(fis);
        return prop.getProperty(key);


    }


    public static boolean validateJSONInteger(Integer value1,Integer value2)
    //user defined softAssert to catch assert exception to proceed with next step instead of skipping them which is default with HardAssert
    {
        Boolean valStatus=true;
        try {
            Assert.assertEquals(value1,value2);
        }
        catch(AssertionError e)
        {
            System.out.println((e.getMessage()));
            System.out.println(value1 + "," +value2 +" are not equal");
            valStatus=false;
        }
        return valStatus;
    }
    public static boolean validateJSONString(String value1,String value2)
    {
        //user defined softAssert to catch assert exception to proceed with next step instead of skipping them which is default with HardAssert
        Boolean valStatus=true;
        try {
            Assert.assertEquals(value1,value2);
        }
        catch(AssertionError e)
        {
            System.out.println((e.getMessage()));
            System.out.println(value1 + "," +value2 +" are not equal");
            valStatus=false;
        }
        return valStatus;
    }



}
