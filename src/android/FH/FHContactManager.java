package org.apache.cordova.contacts;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

public class FHContactManager extends ContactManager {

  
  private static final int PICK_CONTACT_REQUEST = 2;
  private static final int INSERT_CONTACT_REQUEST = 3;
  private CallbackContext mCallbackContext;
  
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    boolean handled = super.execute(action, args, callbackContext);
    mCallbackContext = callbackContext;
    if(!handled){
      if(action.equalsIgnoreCase("insert")){
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION, ContactsContract.Contacts.CONTENT_URI);
        this.cordova.startActivityForResult(this, intent, INSERT_CONTACT_REQUEST);
        PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
        r.setKeepCallback(true);
        callbackContext.sendPluginResult(r);
        return true;
      }
      if(action.equalsIgnoreCase("choose")){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        this.cordova.startActivityForResult(this, intent, PICK_CONTACT_REQUEST);
        PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
        r.setKeepCallback(true);
        callbackContext.sendPluginResult(r);
        return true;
      }
    }
    return handled;
  }
  
  private JSONObject getContactByUri(Uri pUri) throws JSONException {
    String id = pUri.getLastPathSegment();
    return this.contactAccessor.getContactById(id);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if(resultCode == Activity.RESULT_OK){
      Uri contactUri = intent.getData();
      JSONObject result = null;
      try{
        result = getContactByUri(contactUri);
        if(null != result){
          this.mCallbackContext.success(result);
        } else {
          this.mCallbackContext.error("Error reading contact");
        }
      } catch (Exception e){
        this.mCallbackContext.error("Error reading contact: " + e.getMessage());
      }
    } else if(resultCode == Activity.RESULT_CANCELED){
      this.mCallbackContext.error("Cancelled");
    } else {
      this.mCallbackContext.error("Not completed");
    }
  }
  
}
