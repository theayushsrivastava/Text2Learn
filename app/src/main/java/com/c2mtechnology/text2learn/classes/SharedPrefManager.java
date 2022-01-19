package com.c2mtechnology.text2learn.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SharedPrefManager {
    

    //the constants
    private static final String SHARED_PREF_NAME = "UserSharedPreference";


    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TOKEN = "APIKEY";
    private static final String KEY_OBJECTID = "objectID";
    private static final String KEY_ADDRESSES = "useraddres";
    private static final String KEY_LIST_ITEMS = "listItems";
    private static final String KEY_CART_MERCHANT_DETAILS = "cartMerchantDetails";
    private static final String KEY_CART_CATALOGUE_PRODUCTS = "cartCatalogueProducts";
    private static final String KEY_CART_IMAGE_ITEMS   = "cartImageProducts";


    private static final String KEY_FILE = "files";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void addFiles(String data)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FILE, data);
        editor.apply();
    }

    public String getFile(String name)
    {
        HashMap<String,String> filesArray;
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            Type fileType = new TypeToken<HashMap<String, String>>() {}.getType();
            String files = sharedPreferences.getString(KEY_FILE,null);
            if(files != null)
            {
                filesArray = gson.fromJson(files,fileType);
                if(filesArray.containsKey(name))
                {
                    return filesArray.get(name);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String,String> getAllFiles()
    {
        HashMap<String,String> filesArray;
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            Type fileType = new TypeToken<HashMap<String, String>>() {}.getType();
            String files = sharedPreferences.getString(KEY_FILE,null);
            if(files != null)
            {
                filesArray = gson.fromJson(files,fileType);
                return filesArray;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteFile(String fileName)
    {
        HashMap<String,String> filesArray;
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            Type fileType = new TypeToken<HashMap<String, String>>() {}.getType();
            String files = sharedPreferences.getString(KEY_FILE,null);
            if(files != null)
            {
                filesArray = gson.fromJson(files,fileType);
                if(filesArray.containsKey(fileName))
                {
                    filesArray.remove(fileName);
                    this.addFiles(gson.toJson(filesArray));
                    return true;
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * function to get cart list items for
     */

    public String getCartMerchantDetails()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CART_MERCHANT_DETAILS,null);
    }


    public void makeNewList(String merchantData,String listItems)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_CART_MERCHANT_DETAILS, merchantData);
        editor.putString(KEY_LIST_ITEMS,listItems);
        editor.apply();
    }


    public String getListItems()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LIST_ITEMS,null);

    }

    public String getCartMerchantId()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String merchantDetails = sharedPreferences.getString(KEY_CART_MERCHANT_DETAILS,null);
        if(merchantDetails != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(merchantDetails);
                if(jsonObject.has("_id"))
                {
                    return jsonObject.getJSONObject("_id").getString("$oid");
                }else{
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            return null;
        }
        return null;
    }

    public String getCartCatalogueItems()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CART_CATALOGUE_PRODUCTS,null);
    }



    public void setCartImageItems(String merchantData,ArrayList<String> images)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(KEY_CART_IMAGE_ITEMS, gson.toJson(images));
        editor.putString(KEY_CART_MERCHANT_DETAILS,merchantData);
        editor.apply();
    }
    public void setCartImageItems(ArrayList<String> images)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(KEY_CART_IMAGE_ITEMS, gson.toJson(images));
        editor.apply();
    }

    public ArrayList<String> getCartImageItems()
    {
        ArrayList<String> cartImagesArray;
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            Type cartType = new TypeToken<ArrayList<String>>() {}.getType();
            String cartImages = sharedPreferences.getString(KEY_CART_IMAGE_ITEMS,null);
            if(cartImages != null)
            {
                cartImagesArray = gson.fromJson(cartImages,cartType);
                return cartImagesArray;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * function to remove cart image
     * @param index => index of the image uri
     * @return
     */

    public void removeCartImage(int index)
    {
        ArrayList<String> images = getCartImageItems();
        if(images != null)
        {
            if(images.size() > 0)
            {
                try{
                    Uri uri = Uri.parse(images.get(index));
                    File fdelete = new File(uri.getPath());
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            images.remove(index);
                            setCartImageItems(images);
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }
    }


    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String objectId = sharedPreferences.getString(KEY_OBJECTID,null);
        return objectId != null;
    }


    public void login(JSONObject userdata)
    {
        try {
            String objectId = userdata.getJSONObject("_id").getString("$oid");
            String phone = userdata.getString("phone");
            String name = userdata.getString("name");
            String key = userdata.getString("apiKey");
            String address = userdata.getString("address");
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(KEY_OBJECTID,objectId);
            editor.putString(KEY_PHONE,phone);
            editor.putString(KEY_NAME,name);
            editor.putString(KEY_TOKEN,key);
            editor.putString(KEY_ADDRESSES,address);
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // the string is a jsonarray of objects each object containig address details like addr1, addr2, latitude and longitude
    public String getUserAddress()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ADDRESSES,null);
    }

    public HashMap<String,String> getUser()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        HashMap<String,String> user = new HashMap<>();
        user.put("userId",sharedPreferences.getString(KEY_OBJECTID,null));
        user.put("name",sharedPreferences.getString(KEY_NAME,null));
        user.put("phone",sharedPreferences.getString(KEY_PHONE,null));
        return user;
    }

    public HashMap<String,String> getMerchantDetails()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String merchantDetails = sharedPreferences.getString(KEY_CART_MERCHANT_DETAILS,null);
        HashMap<String,String> merchant = new HashMap<>();
        if(merchantDetails != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(merchantDetails);
                if(jsonObject.has("_id"))
                {
                    merchant.put("merchantId",jsonObject.getJSONObject("_id").getString("$oid"));
                    merchant.put("city",jsonObject.getString("city"));
                    return merchant;
                }else{
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            return null;
        }
        return null;
    }


    public String getObjectId()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_OBJECTID,null);
    }

    public String getToken()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN,null);
    }

    public String getKeyPhone() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE,null);
    }

    public void clearCart()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_LIST_ITEMS);
        editor.remove(KEY_CART_CATALOGUE_PRODUCTS);
        editor.remove(KEY_CART_MERCHANT_DETAILS);

        //delete the image files
        ArrayList<String> cartImagesArray;
        Gson gson = new Gson();
        try {
            Type cartType = new TypeToken<ArrayList<String>>() {}.getType();
            String cartImages = sharedPreferences.getString(KEY_CART_IMAGE_ITEMS,null);
            if(cartImages != null)
            {
                cartImagesArray = gson.fromJson(cartImages,cartType);
                for(int i = 0 ; i < cartImagesArray.size() ; i++)
                {
                    Uri uri = Uri.parse(cartImagesArray.get(i));
                    File fdelete = new File(uri.getPath());
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            System.out.println("file Deleted :" + uri.getPath());
                        } else {
                            System.out.println("file not Deleted :" + uri.getPath());
                        }
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        editor.remove(KEY_CART_IMAGE_ITEMS);
        editor.apply();
    }


}
