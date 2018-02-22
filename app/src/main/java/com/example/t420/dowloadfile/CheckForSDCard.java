package com.example.t420.dowloadfile;

import android.os.Environment;

/**
 * Created by t420 on 22-Feb-18.
 */

public class CheckForSDCard {
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
