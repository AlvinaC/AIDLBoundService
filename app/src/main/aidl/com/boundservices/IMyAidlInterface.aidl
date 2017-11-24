// IMyAidlInterface.aidl
package com.boundservices;

// Declare any non-default types here with import statements
import com.boundservices.IMyAidlCallbackInterface;

 interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    oneway void getPid(IMyAidlCallbackInterface callback);
    oneway void getRandomNo(IMyAidlCallbackInterface callback);
}
