package com.androidgallerypicker
import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
class PhotoPickerModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext), ActivityEventListener {
  companion object {
    private const val NAME = "PhotoPicker"
    private const val REQUEST_CODE_SINGLE = 1001
    private const val REQUEST_CODE_MULTI = 1002
  }
  private var pickerPromise: Promise? = null
  private var selectionLimit = 1
  init {
    // Register to listen for onActivityResult
    reactContext.addActivityEventListener(this)
  }
  override fun getName(): String = NAME
  @ReactMethod
  fun pick(options: ReadableMap, promise: Promise) {
    val activity = currentActivity
    if (activity == null) {
      promise.reject("ACTIVITY_NULL", "Activity context is null")
      return
    }
    pickerPromise = promise
    // Determine single vs multi
    selectionLimit = if (options.hasKey("selectionLimit")) options.getInt("selectionLimit") else 1
    // Build intent for picking images or videos
    val intent = Intent(Intent.ACTION_PICK).apply {
      type = when {
        options.hasKey("mediaType") && options.getString("mediaType") == "video" -> "video/*"
        else -> "image/*"
      }
      putExtra(Intent.EXTRA_ALLOW_MULTIPLE, selectionLimit > 1)
    }
    // Launch with distinct request codes for single vs multi
    val requestCode = if (selectionLimit > 1) REQUEST_CODE_MULTI else REQUEST_CODE_SINGLE
    activity.startActivityForResult(intent, requestCode)
  }
  override fun onActivityResult(
    activity: Activity?,
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    if (pickerPromise == null) return
    if (resultCode == Activity.RESULT_CANCELED) {
      pickerPromise?.reject("CANCELLED", "User cancelled the picker")
      pickerPromise = null
      return
    }
    try {
      when (requestCode) {
        REQUEST_CODE_SINGLE -> {
          val uri: Uri? = data?.data
          if (uri != null) {
            val result = Arguments.createMap().apply {
              putString("uri", uri.toString())
            }
            pickerPromise?.resolve(result)
          } else {
            pickerPromise?.reject("NO_DATA", "No image URI returned")
          }
        }
        REQUEST_CODE_MULTI -> {
          val clipData = data?.clipData
          val results = Arguments.createArray()
          if (clipData != null) {
            for (i in 0 until clipData.itemCount.coerceAtMost(selectionLimit)) {
              val itemUri = clipData.getItemAt(i).uri
              results.pushMap(Arguments.createMap().apply {
                putString("uri", itemUri.toString())
              })
            }
            pickerPromise?.resolve(results)
          } else {
            // fallback to single
            val uri: Uri? = data?.data
            if (uri != null) {
              val singleResult = Arguments.createArray().apply {
                pushMap(Arguments.createMap().apply {
                  putString("uri", uri.toString())
                })
              }
              pickerPromise?.resolve(singleResult)
            } else {
              pickerPromise?.reject("NO_DATA", "No URIs returned")
            }
          }
        }
      }
    } catch (e: Exception) {
      pickerPromise?.reject("ERROR", e.message)
    } finally {
      pickerPromise = null
    }
  }
  override fun onNewIntent(intent: Intent?) {
    // no-op
  }
}