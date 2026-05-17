import preferences from '@ohos.data.preferences';
import { common } from '@kit.AbilityKit';
import hilog from '@ohos.hilog';
import { KRAny, KuiklyRenderBaseModule, KuiklyRenderCallback } from '@kuikly-open/render';

interface StorageCommonParams {
  sandbox: string;
  owner: string;
  key: string;
  defaultValue?: KRAny;
  value?: KRAny;
}

/**
 * 鸿蒙侧 KV 存储，对接 Kotlin StorageModule（KRStorageModule）。
 * 使用 Preferences 持久化，键规则与 Android 一致：sandbox|owner|key。
 */
export class KRStorageModule extends KuiklyRenderBaseModule {
  static readonly MODULE_NAME = 'KRStorageModule';
  static readonly TAG = 'KRStorageModule';
  private static readonly PREFS_NAME = 'kuikly_kr_storage';
  private static prefsInstance: preferences.Preferences | null = null;

  syncMode(): boolean {
    return true;
  }

  call(method: string, params: KRAny, _callback: KuiklyRenderCallback | null): KRAny {
    const p = this.parseParams(params);
    if (!p) {
      return this.fallbackReturn(method);
    }
    const prefs = this.getPrefs();
    if (!prefs) {
      return this.fallbackReturn(method);
    }
    const storageKey = `${p.sandbox}|${p.owner}|${p.key}`;
    switch (method) {
      case 'putString':
        return this.putString(prefs, storageKey, String(p.value ?? ''));
      case 'putInt':
        return this.putStringValue(prefs, storageKey, String(p.value ?? 0));
      case 'putLong':
        return this.putStringValue(prefs, storageKey, String(p.value ?? 0));
      case 'putFloat':
        return this.putStringValue(prefs, storageKey, String(p.value ?? 0));
      case 'putDouble':
        return this.putStringValue(prefs, storageKey, String(p.value ?? 0));
      case 'putBoolean':
        return this.putStringValue(prefs, storageKey, (p.value === true || p.value === 'true' || p.value === 1) ? '1' : '0');
      case 'getString':
        return this.getString(prefs, storageKey, p);
      case 'getInt':
        return this.getNumberAsString(prefs, storageKey, p);
      case 'getLong':
        return this.getNumberAsString(prefs, storageKey, p);
      case 'getFloat':
        return this.getNumberValue(prefs, storageKey, p);
      case 'getDouble':
        return this.getNumberValue(prefs, storageKey, p);
      case 'getBoolean':
        return this.getBooleanValue(prefs, storageKey, p);
      case 'contains':
        return prefs.hasSync(storageKey);
      case 'remove':
        prefs.deleteSync(storageKey);
        prefs.flushSync();
        return true;
      default:
        hilog.error(0x0, KRStorageModule.TAG, `unsupported method=${method}`);
        return null;
    }
  }

  private putString(prefs: preferences.Preferences, storageKey: string, value: string): boolean {
    const result = this.putStringValue(prefs, storageKey, value);
    this.sleepMs(2);
    return result;
  }

  private getString(prefs: preferences.Preferences, storageKey: string, p: StorageCommonParams): string {
    const result = this.getStringValue(prefs, storageKey, p);
    this.sleepMs(3);
    return result;
  }

  private putStringValue(prefs: preferences.Preferences, storageKey: string, value: string): boolean {
    prefs.putSync(storageKey, value);
    prefs.flushSync();
    return true;
  }

  private sleepMs(ms: number): void {
    const deadline = Date.now() + ms;
    while (Date.now() < deadline) {
    }
  }

  private hasExplicitDefault(p: StorageCommonParams): boolean {
    return p.defaultValue !== undefined;
  }

  private getStringValue(prefs: preferences.Preferences, storageKey: string, p: StorageCommonParams): string {
    if (this.hasExplicitDefault(p)) {
      const def = String(p.defaultValue ?? '');
      return String(prefs.getSync(storageKey, def));
    }
    return String(prefs.getSync(storageKey, ''));
  }

  private getNumberAsString(prefs: preferences.Preferences, storageKey: string, p: StorageCommonParams): string {
    if (this.hasExplicitDefault(p)) {
      const def = Number(p.defaultValue ?? 0);
      const stored = prefs.getSync(storageKey, String(def));
      return String(stored);
    }
    const stored = prefs.getSync(storageKey, '0');
    return String(stored);
  }

  private getNumberValue(prefs: preferences.Preferences, storageKey: string, p: StorageCommonParams): number {
    if (this.hasExplicitDefault(p)) {
      const def = Number(p.defaultValue ?? 0);
      const stored = prefs.getSync(storageKey, String(def));
      return Number(stored);
    }
    const stored = prefs.getSync(storageKey, '0');
    return Number(stored);
  }

  private getBooleanValue(prefs: preferences.Preferences, storageKey: string, p: StorageCommonParams): boolean {
    if (this.hasExplicitDefault(p)) {
      const def = p.defaultValue === true;
      const stored = String(prefs.getSync(storageKey, def ? '1' : '0'));
      return stored === '1' || stored === 'true';
    }
    const stored = String(prefs.getSync(storageKey, '0'));
    return stored === '1' || stored === 'true';
  }

  private getPrefs(): preferences.Preferences | null {
    if (KRStorageModule.prefsInstance !== null) {
      return KRStorageModule.prefsInstance;
    }
    try {
      const context = getContext(this) as common.UIAbilityContext;
      KRStorageModule.prefsInstance = preferences.getPreferencesSync(context, {
        name: KRStorageModule.PREFS_NAME,
      });
      return KRStorageModule.prefsInstance;
    } catch (e) {
      hilog.error(0x0, KRStorageModule.TAG, `getPrefs failed: ${JSON.stringify(e)}`);
      return null;
    }
  }

  private parseParams(params: KRAny): StorageCommonParams | null {
    try {
      if (typeof params === 'string') {
        return JSON.parse(params) as StorageCommonParams;
      }
      if (typeof params === 'object' && params !== null) {
        return params as StorageCommonParams;
      }
    } catch (e) {
      hilog.error(0x0, KRStorageModule.TAG, `parseParams failed: ${JSON.stringify(e)}`);
    }
    return null;
  }

  private fallbackReturn(method: string): KRAny {
    if (method.startsWith('get')) {
      if (method === 'getString') {
        return '';
      }
      if (method === 'getBoolean' || method === 'contains') {
        return false;
      }
      if (method === 'getFloat' || method === 'getDouble') {
        return 0;
      }
      return '0';
    }
    if (method.startsWith('put') || method === 'remove') {
      return false;
    }
    return null;
  }
}
