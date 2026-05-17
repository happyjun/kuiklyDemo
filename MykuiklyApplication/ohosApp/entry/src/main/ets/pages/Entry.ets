import router from '@ohos.router';

@Entry
@Component
struct EntryPage {
  build() {
    Column() {
      Button('机票')
        .onClick(() => {
          router.pushUrl({ url: 'pages/Index' });
        })
    }
    .width('100%')
    .height('100%')
    .justifyContent(FlexAlign.Center)
  }
}
