package com.mqunar.mykuiklyapplication.module

import com.tencent.kuikly.core.module.Module
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject

/**
 * StorageModule
 *
 * KMP 存储接口层，封装跨平台 KV 存储能力。
 *
 * 读取语义（单一 get 契约，不另增 OrNull 等接口）：
 * - [defaultValue] 为 `null`（默认）：先 [contains]，不存在则返回 Kotlin `null`；存在再调 Native get）。
 * - [defaultValue] 非 `null`：直接调 Native get（params 带 defaultValue）。
 *
 * 模块注册名常量见 [MODULE_NAME]（需与宿主 native 注册名一致）。
 */
class StorageModule : Module() {

    override fun moduleName(): String = MODULE_NAME

    companion object {
        const val MODULE_NAME = "KRStorageModule"
    }

    fun putString(sandbox: String, key: String, value: String, owner: String? = null): Boolean {
        val validOwner = validOwnerOrNull(owner) ?: return false
        val params = buildParams(sandbox, validOwner, key) { put("value", value) }
        return syncToNativeMethod("putString", params, null).toBoolean()
    }

    fun putInt(sandbox: String, key: String, value: Int, owner: String? = null): Boolean {
        val validOwner = validOwnerOrNull(owner) ?: return false
        val params = buildParams(sandbox, validOwner, key) { put("value", value) }
        return syncToNativeMethod("putInt", params, null).toBoolean()
    }

    fun putLong(sandbox: String, key: String, value: Long, owner: String? = null): Boolean {
        val validOwner = validOwnerOrNull(owner) ?: return false
        val params = buildParams(sandbox, validOwner, key) { put("value", value) }
        return syncToNativeMethod("putLong", params, null).toBoolean()
    }

    fun putFloat(sandbox: String, key: String, value: Float, owner: String? = null): Boolean {
        val validOwner = validOwnerOrNull(owner) ?: return false
        val params = buildParams(sandbox, validOwner, key) { put("value", value) }
        return syncToNativeMethod("putFloat", params, null).toBoolean()
    }

    fun putDouble(sandbox: String, key: String, value: Double, owner: String? = null): Boolean {
        val validOwner = validOwnerOrNull(owner) ?: return false
        val params = buildParams(sandbox, validOwner, key) { put("value", value) }
        return syncToNativeMethod("putDouble", params, null).toBoolean()
    }

    fun putBoolean(sandbox: String, key: String, value: Boolean, owner: String? = null): Boolean {
        val validOwner = validOwnerOrNull(owner) ?: return false
        val params = buildParams(sandbox, validOwner, key) { put("value", value) }
        return syncToNativeMethod("putBoolean", params, null).toBoolean()
    }

    fun getString(sandbox: String, key: String, defaultValue: String? = null, owner: String? = null): String? {
        val validOwner = validOwnerOrNull(owner) ?: return null
        if (defaultValue != null) {
            val params = buildParams(sandbox, validOwner, key) { put("defaultValue", defaultValue) }
            val raw = syncToNativeMethod("getString", params, null)
            return raw?.toString() ?: defaultValue
        }
        if (!contains(sandbox, key, validOwner)) return null
        val params = buildParams(sandbox, validOwner, key)
        val raw = syncToNativeMethod("getString", params, null)
        return raw?.toString()
    }

    fun getInt(sandbox: String, key: String, defaultValue: Int? = null, owner: String? = null): Int? {
        val validOwner = validOwnerOrNull(owner) ?: return null
        if (defaultValue != null) {
            val params = buildParams(sandbox, validOwner, key) { put("defaultValue", defaultValue) }
            val raw = syncToNativeMethod("getInt", params, null)
            return raw.toSafeInt() ?: defaultValue
        }
        if (!contains(sandbox, key, validOwner)) return null
        val params = buildParams(sandbox, validOwner, key)
        return syncToNativeMethod("getInt", params, null).toSafeInt()
    }

    fun getLong(sandbox: String, key: String, defaultValue: Long? = null, owner: String? = null): Long? {
        val validOwner = validOwnerOrNull(owner) ?: return null
        if (defaultValue != null) {
            val params = buildParams(sandbox, validOwner, key) { put("defaultValue", defaultValue) }
            val raw = syncToNativeMethod("getLong", params, null)
            return raw.toSafeLong() ?: defaultValue
        }
        if (!contains(sandbox, key, validOwner)) return null
        val params = buildParams(sandbox, validOwner, key)
        return syncToNativeMethod("getLong", params, null).toSafeLong()
    }

    fun getFloat(sandbox: String, key: String, defaultValue: Float? = null, owner: String? = null): Float? {
        val validOwner = validOwnerOrNull(owner) ?: return null
        if (defaultValue != null) {
            val params = buildParams(sandbox, validOwner, key) { put("defaultValue", defaultValue) }
            val raw = syncToNativeMethod("getFloat", params, null)
            return raw.toSafeFloat() ?: defaultValue
        }
        if (!contains(sandbox, key, validOwner)) return null
        val params = buildParams(sandbox, validOwner, key)
        return syncToNativeMethod("getFloat", params, null).toSafeFloat()
    }

    fun getDouble(sandbox: String, key: String, defaultValue: Double? = null, owner: String? = null): Double? {
        val validOwner = validOwnerOrNull(owner) ?: return null
        if (defaultValue != null) {
            val params = buildParams(sandbox, validOwner, key) { put("defaultValue", defaultValue) }
            val raw = syncToNativeMethod("getDouble", params, null)
            return raw.toSafeDouble() ?: defaultValue
        }
        if (!contains(sandbox, key, validOwner)) return null
        val params = buildParams(sandbox, validOwner, key)
        return syncToNativeMethod("getDouble", params, null).toSafeDouble()
    }

    fun getBoolean(sandbox: String, key: String, defaultValue: Boolean? = null, owner: String? = null): Boolean? {
        val validOwner = validOwnerOrNull(owner) ?: return null
        if (defaultValue != null) {
            val params = buildParams(sandbox, validOwner, key) { put("defaultValue", defaultValue) }
            val raw = syncToNativeMethod("getBoolean", params, null)
            return raw.toSafeBoolean() ?: defaultValue
        }
        if (!contains(sandbox, key, validOwner)) return null
        val params = buildParams(sandbox, validOwner, key)
        return syncToNativeMethod("getBoolean", params, null).toSafeBoolean()
    }

    fun contains(sandbox: String, key: String, owner: String? = null): Boolean {
        val validOwner = validOwnerOrNull(owner) ?: return false
        val params = buildParams(sandbox, validOwner, key)
        val raw = syncToNativeMethod("contains", params, null) ?: return false
        return raw.toSafeBoolean() ?: false
    }

    fun remove(sandbox: String, key: String, owner: String? = null): Boolean {
        val validOwner = validOwnerOrNull(owner) ?: return false
        val params = buildParams(sandbox, validOwner, key)
        return syncToNativeMethod("remove", params, null)?.toSafeBoolean() ?: false
    }

    private fun buildParams(
        sandbox: String,
        owner: String,
        key: String,
        extra: (JSONObject.() -> Unit)? = null
    ): JSONObject {
        return JSONObject().apply {
            put("sandbox", sandbox)
            put("owner", owner)
            put("key", key)
            extra?.invoke(this)
        }
    }

    private fun validOwnerOrNull(owner: String?): String? {
        return owner?.takeIf { it.isNotEmpty() }
    }

    private fun Any?.toSafeInt(): Int? {
        return when (this) {
            is Number -> toInt()
            is String -> toIntOrNull() ?: toDoubleOrNull()?.toInt()
            else -> null
        }
    }

    private fun Any?.toSafeLong(): Long? {
        return when (this) {
            is Number -> toLong()
            is String -> toLongOrNull() ?: toDoubleOrNull()?.toLong()
            else -> null
        }
    }

    private fun Any?.toSafeFloat(): Float? {
        return when (this) {
            is Number -> toFloat()
            is String -> toFloatOrNull()
            else -> null
        }
    }

    private fun Any?.toSafeDouble(): Double? {
        return when (this) {
            is Number -> toDouble()
            is String -> toDoubleOrNull()
            else -> null
        }
    }

    private fun Any?.toSafeBoolean(): Boolean? {
        return when (this) {
            is Boolean -> this
            is Number -> toInt() != 0
            is String -> toBooleanStrictOrNull() ?: toDoubleOrNull()?.let { it.toInt() != 0 }
            else -> null
        }
    }
}

private fun Any?.toBoolean(): Boolean {
    return when (this) {
        is Boolean -> this
        is Number -> toInt() != 0
        is String -> this == "true" || this == "1"
        else -> false
    }
}
