package com.mqunar.mykuiklyapplication.module

import android.content.Context
import com.mqunar.mykuiklyapplication.KRApplication
import com.tencent.kuikly.core.render.android.export.KuiklyRenderBaseModule
import com.tencent.kuikly.core.render.android.export.KuiklyRenderCallback
import org.json.JSONObject

class KRStorageModule : KuiklyRenderBaseModule() {

    private fun prefs() =
        KRApplication.application.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun call(method: String, params: String?, callback: KuiklyRenderCallback?): Any? {
        val jo = runCatching { if (params.isNullOrBlank()) JSONObject() else JSONObject(params) }.getOrNull()
            ?: JSONObject()
        val p = prefs()
        val key = storageKey(jo)
        return when (method) {
            "putString" -> p.edit().putString(key, jo.optString("value")).commit().toString()
            "putInt" -> p.edit().putString(key, jo.optInt("value", 0).toString()).commit().toString()
            "putLong" -> p.edit().putString(key, jo.optLong("value", 0L).toString()).commit().toString()
            "putFloat" -> p.edit().putString(key, jo.optDouble("value", 0.0).toFloat().toString()).commit().toString()
            "putDouble" -> p.edit().putString(key, jo.optDouble("value", 0.0).toString()).commit().toString()
            "putBoolean" -> p.edit().putString(key, if (jo.optBoolean("value")) "1" else "0").commit().toString()
            "getString" -> p.getString(key, null) ?: jo.optString("defaultValue", "")
            "getInt" -> (p.getString(key, null)?.toIntOrNull() ?: jo.optInt("defaultValue", 0)).toString()
            "getLong" -> (p.getString(key, null)?.toLongOrNull() ?: jo.optLong("defaultValue", 0L)).toString()
            "getFloat" ->
                (p.getString(key, null)?.toFloatOrNull() ?: jo.optDouble("defaultValue", 0.0).toFloat()).toString()
            "getDouble" ->
                (p.getString(key, null)?.toDoubleOrNull() ?: jo.optDouble("defaultValue", 0.0)).toString()
            "getBoolean" -> when (p.getString(key, null)) {
                "1", "true" -> true
                "0", "false" -> false
                null -> jo.optBoolean("defaultValue", false)
                else -> p.getString(key, null) == "1"
            }.toString()
            "contains" -> p.contains(key).toString()
            "remove" -> p.edit().remove(key).commit().toString()
            else -> false.toString()
        }
    }

    private fun storageKey(jo: JSONObject): String {
        val sandbox = jo.optString("sandbox")
        val owner = jo.optString("owner")
        val key = jo.optString("key")
        return "$sandbox|$owner|$key"
    }

    companion object {
        private const val PREFS_NAME = "kuikly_kr_storage"
        const val MODULE_NAME = "KRStorageModule"
    }
}
