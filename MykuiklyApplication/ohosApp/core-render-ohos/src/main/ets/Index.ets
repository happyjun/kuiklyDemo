/*
 * Tencent is pleased to support the open source community by making KuiklyUI
 * available.
 * Copyright (C) 2025 Tencent. All rights reserved.
 * Licensed under the License of KuiklyUI;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://github.com/Tencent-TDS/KuiklyUI/blob/main/LICENSE
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

export { KRNative } from './KRNative';

export { KRNative as KTNative } from './KRNative';

export * from './utils/KRDataTypes';

// 兼容旧版本
export {
  KRValue as KTValue,
  KRArray as KTArray,
  KRRecord as KTRecord,
  KRAny as KTAny,
} from './utils/KRDataTypes';

export * from './adapter/export';

export * from './exception/KuiklyRenderExportExceptions';

export { KuiklyNativeManager } from './native/KuiklyNativeManager';

export * from './modules/export';

export { KRNativeRender as Kuikly } from './KRNativeRender';

export { IKuiklyViewDelegate, KRNativeRenderController, IKuiklyRenderViewLifecycleCallback, KRMonitorType } from './KRNativeRenderController';

// 兼容旧版本
export { KRNativeRenderController as KTNativeRenderController } from './KRNativeRenderController';

export * from './components/export';

export { KuiklyRenderAdapterManager } from './adapter/KRAdapterManager';

export { ViewsRegisterEntry } from './components/ViewsRegisterEntry';

export { ModulesRegisterEntry } from './modules/ModulesRegisterEntry';

export { KRConvertUtil } from './utils/KRConvertUtil';

// 兼容旧版本
export { KRConvertUtil as KTConvertUtil } from './utils/KRConvertUtil';

export { IKuiklyRenderContextHandler,
  IKuiklyContextNativeCommunicator } from './context/IKuiklyRenderContextHandler';

export { IKRPAGViewAdapter, IKRPAGViewController, IKRPAGViewListener } from './adapter/IKRPAGViewAdapter';

export { KRRenderExecuteModeBase } from './context/KRRenderExecuteModeBase';

export { KRRenderNativeMode } from './context/KRRenderNativeMode';
