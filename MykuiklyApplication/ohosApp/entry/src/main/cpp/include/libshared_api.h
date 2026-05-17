#ifndef KONAN_LIBSHARED_H
#define KONAN_LIBSHARED_H
#ifdef __cplusplus
extern "C" {
#endif
#ifdef __cplusplus
typedef bool            libshared_KBoolean;
#else
typedef _Bool           libshared_KBoolean;
#endif
typedef unsigned short     libshared_KChar;
typedef signed char        libshared_KByte;
typedef short              libshared_KShort;
typedef int                libshared_KInt;
typedef long long          libshared_KLong;
typedef unsigned char      libshared_KUByte;
typedef unsigned short     libshared_KUShort;
typedef unsigned int       libshared_KUInt;
typedef unsigned long long libshared_KULong;
typedef float              libshared_KFloat;
typedef double             libshared_KDouble;
typedef float __attribute__ ((__vector_size__ (16))) libshared_KVector128;
typedef void*              libshared_KNativePtr;
struct libshared_KType;
typedef struct libshared_KType libshared_KType;

typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Byte;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Short;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Int;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Long;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Float;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Double;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Char;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Boolean;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_Unit;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_UByte;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_UShort;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_UInt;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_kotlin_ULong;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule;
typedef struct {
  libshared_KNativePtr pinned;
} libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule_Companion;


typedef struct {
  /* Service functions. */
  void (*DisposeStablePointer)(libshared_KNativePtr ptr);
  void (*DisposeString)(const char* string);
  libshared_KBoolean (*IsInstance)(libshared_KNativePtr ref, const libshared_KType* type);
  libshared_kref_kotlin_Byte (*createNullableByte)(libshared_KByte);
  libshared_KByte (*getNonNullValueOfByte)(libshared_kref_kotlin_Byte);
  libshared_kref_kotlin_Short (*createNullableShort)(libshared_KShort);
  libshared_KShort (*getNonNullValueOfShort)(libshared_kref_kotlin_Short);
  libshared_kref_kotlin_Int (*createNullableInt)(libshared_KInt);
  libshared_KInt (*getNonNullValueOfInt)(libshared_kref_kotlin_Int);
  libshared_kref_kotlin_Long (*createNullableLong)(libshared_KLong);
  libshared_KLong (*getNonNullValueOfLong)(libshared_kref_kotlin_Long);
  libshared_kref_kotlin_Float (*createNullableFloat)(libshared_KFloat);
  libshared_KFloat (*getNonNullValueOfFloat)(libshared_kref_kotlin_Float);
  libshared_kref_kotlin_Double (*createNullableDouble)(libshared_KDouble);
  libshared_KDouble (*getNonNullValueOfDouble)(libshared_kref_kotlin_Double);
  libshared_kref_kotlin_Char (*createNullableChar)(libshared_KChar);
  libshared_KChar (*getNonNullValueOfChar)(libshared_kref_kotlin_Char);
  libshared_kref_kotlin_Boolean (*createNullableBoolean)(libshared_KBoolean);
  libshared_KBoolean (*getNonNullValueOfBoolean)(libshared_kref_kotlin_Boolean);
  libshared_kref_kotlin_Unit (*createNullableUnit)(void);
  libshared_kref_kotlin_UByte (*createNullableUByte)(libshared_KUByte);
  libshared_KUByte (*getNonNullValueOfUByte)(libshared_kref_kotlin_UByte);
  libshared_kref_kotlin_UShort (*createNullableUShort)(libshared_KUShort);
  libshared_KUShort (*getNonNullValueOfUShort)(libshared_kref_kotlin_UShort);
  libshared_kref_kotlin_UInt (*createNullableUInt)(libshared_KUInt);
  libshared_KUInt (*getNonNullValueOfUInt)(libshared_kref_kotlin_UInt);
  libshared_kref_kotlin_ULong (*createNullableULong)(libshared_KULong);
  libshared_KULong (*getNonNullValueOfULong)(libshared_kref_kotlin_ULong);

  /* User functions. */
  struct {
    struct {
      struct {
        struct {
          struct {
            struct {
              struct {
                struct {
                  libshared_KType* (*_type)(void);
                  libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule_Companion (*_instance)();
                  const char* (*get_MODULE_NAME)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule_Companion thiz);
                } Companion;
                libshared_KType* (*_type)(void);
                libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule (*StorageModule)();
                libshared_KBoolean (*contains)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, const char* owner);
                libshared_kref_kotlin_Boolean (*getBoolean)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_kref_kotlin_Boolean defaultValue, const char* owner);
                libshared_kref_kotlin_Double (*getDouble)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_kref_kotlin_Double defaultValue, const char* owner);
                libshared_kref_kotlin_Float (*getFloat)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_kref_kotlin_Float defaultValue, const char* owner);
                libshared_kref_kotlin_Int (*getInt)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_kref_kotlin_Int defaultValue, const char* owner);
                libshared_kref_kotlin_Long (*getLong)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_kref_kotlin_Long defaultValue, const char* owner);
                const char* (*getString)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, const char* defaultValue, const char* owner);
                const char* (*moduleName)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz);
                libshared_KBoolean (*putBoolean)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_KBoolean value, const char* owner);
                libshared_KBoolean (*putDouble)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_KDouble value, const char* owner);
                libshared_KBoolean (*putFloat)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_KFloat value, const char* owner);
                libshared_KBoolean (*putInt)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_KInt value, const char* owner);
                libshared_KBoolean (*putLong)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, libshared_KLong value, const char* owner);
                libshared_KBoolean (*putString)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, const char* value, const char* owner);
                libshared_KBoolean (*remove)(libshared_kref_com_mqunar_mykuiklyapplication_module_StorageModule thiz, const char* sandbox, const char* key, const char* owner);
              } StorageModule;
            } module;
          } mykuiklyapplication;
        } mqunar;
      } com;
      libshared_KInt (*initKuikly)();
    } root;
  } kotlin;
} libshared_ExportedSymbols;
extern libshared_ExportedSymbols* libshared_symbols(void);
#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif  /* KONAN_LIBSHARED_H */
