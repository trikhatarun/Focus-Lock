package com.android.trikh.focusLock.data;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by trikh on 06-08-2017.
 */

public class JsonHelper {

    private Context contextInstance;

    public JsonHelper(Context context) {
        contextInstance = context;
    }

    public String loadJSONFromAsset() {
        String json;
        try {

            InputStream is = contextInstance.getAssets().open("data.json");


            int size = is.available();


            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d("Json: ", "loadJSONFromAsset: " + json);
        return json;

    }
}
