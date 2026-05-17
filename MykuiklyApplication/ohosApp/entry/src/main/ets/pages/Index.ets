import { ErrorReason,
  IKuiklyRenderViewLifecycleCallback,
  KRNativeRenderController, KRRecord, Kuikly, KuiklyRenderBaseView } from '@kuikly-open/render';
import router from '@ohos.router';
import { KuiklyViewDelegate } from '../kuikly/KuiklyViewDelegate';
import globalNativeManager from '../kuikly/MyNativeManager';

/** Kuikly RenderView 细粒度生命周期；须 class implements，避免 arkts-no-untyped-obj-literals */
class FlightKmpHomePageKuiklyLifecycleCallback implements IKuiklyRenderViewLifecycleCallback {
  onInit(): void {
    console.log('KRRender [KuiklyLifecycle] onInit');
  }

  onInitCoreStart(): void {
    console.log('KRRender [KuiklyLifecycle] onInitCoreStart');
  }

  onInitCoreFinish(): void {
    console.log('KRRender [KuiklyLifecycle] onInitCoreFinish');
  }

  onInitContextStart(): void {
    console.log('KRRender [KuiklyLifecycle] onInitContextStart');
  }

  onInitContextFinish(): void {
    console.log('KRRender [KuiklyLifecycle] onInitContextFinish');
  }

  onCreateInstanceStart(): void {
    console.log('KRRender [KuiklyLifecycle] onCreateInstanceStart');
  }

  onCreateInstanceFinish(): void {
    console.log('KRRender [KuiklyLifecycle] onCreateInstanceFinish');
  }

  onFirstFramePaint(): void {
    console.log('KRRender [KuiklyLifecycle] onFirstFramePaint');
  }
}

@Entry
@Component
struct Index {
  kuiklyViewDelegate = new KuiklyViewDelegate();
  pageName?: string;
  pageData?: KRRecord;
  private kuiklyController: KRNativeRenderController | null = null;

  @State exception?: string = "";

  aboutToAppear() {
    AppStorage.setOrCreate<UIContext>("context", this.getUIContext());

    const params = router.getParams() as Record<string, Object>;
    this.pageName = params?.pageName as string;
    this.pageData = params?.pageData as KRRecord;
  }

  build() {
    Stack() {
      Kuikly({
        pagerName: this.pageName ?? 'router',
        pagerData: this.pageData ?? {},
        delegate: this.kuiklyViewDelegate,
        nativeManager: globalNativeManager,
        // onRenderException: (exception: Error, reason: ErrorReason) => {
        //   this.exception = `${exception.name}:\n${exception.message}`;
        // },
        onControllerReadyCallback: (controller) => {
          this.kuiklyController = controller;
          controller.addKuiklyRenderViewLifecycleCallback(new FlightKmpHomePageKuiklyLifecycleCallback());
          controller.registerExceptionCallback((_executeMode, stack) => {
          });
        }
      })
      if (this.exception) {
        Text(this.exception)
      }
    }
    .width('100%')
    .height('100%')
    .expandSafeArea([SafeAreaType.KEYBOARD])
  }

  onPageShow(): void {
    this.kuiklyViewDelegate.pageDidAppear();
  }

  onPageHide(): void {
    this.kuiklyViewDelegate.pageDidDisappear();
  }
}