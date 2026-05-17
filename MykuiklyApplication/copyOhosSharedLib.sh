#!/bin/sh
# 编译 shared 鸿蒙 so，并拷贝到 ohosApp entry 模块
#
# 用法:
#   ./copyOhosSharedLib.sh          # 先 linkOhosArm64，再拷贝 debug 产物
#   ./copyOhosSharedLib.sh --copy-only   # 仅拷贝（需已执行过 gradle 编译）
#   ./copyOhosSharedLib.sh --release     # 使用 releaseShared 目录产物

set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

BUILD=1
VARIANT=debugShared

for arg in "$@"; do
  case "$arg" in
    --copy-only)
      BUILD=0
      ;;
    --release)
      VARIANT=releaseShared
      ;;
    -h|--help)
      echo "Usage: $0 [--copy-only] [--release]"
      exit 0
      ;;
    *)
      echo "Unknown option: $arg" >&2
      exit 1
      ;;
  esac
done

SRC_DIR="$ROOT/shared/build/bin/ohosArm64/$VARIANT"
DST_HEADER="$ROOT/ohosApp/entry/src/main/cpp/include/libshared_api.h"
DST_SO="$ROOT/ohosApp/entry/libs/arm64-v8a/libshared.so"

if [ "$BUILD" = 1 ]; then
  echo ">> ./gradlew -c settings.ohos.gradle.kts :shared:linkOhosArm64"
  ./gradlew -c settings.ohos.gradle.kts :shared:linkOhosArm64
fi

if [ ! -f "$SRC_DIR/libshared.so" ] || [ ! -f "$SRC_DIR/libshared_api.h" ]; then
  echo "error: 产物不存在，请先编译:" >&2
  echo "  $SRC_DIR/libshared.so" >&2
  echo "  $SRC_DIR/libshared_api.h" >&2
  exit 1
fi

mkdir -p "$(dirname "$DST_HEADER")" "$(dirname "$DST_SO")"
cp -f "$SRC_DIR/libshared_api.h" "$DST_HEADER"
cp -f "$SRC_DIR/libshared.so" "$DST_SO"

echo ">> copied ($VARIANT)"
echo "   $SRC_DIR/libshared_api.h -> $DST_HEADER"
echo "   $SRC_DIR/libshared.so    -> $DST_SO"
