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

#include "libohos_render/scheduler/KRContextScheduler.h"

#include <atomic>
#include <future>
#include <memory>
#include "libohos_render/foundation/thread/KRMainThread.h"

class KRContextSchedulerInternal {
 public:
    virtual ~KRContextSchedulerInternal() = default;
    virtual void ScheduleTask(bool sync, int delayMs, const KRSchedulerTask &task) = 0;
    virtual void ScheduleTaskOnMainThread(bool sync, const KRSchedulerTask &task) = 0;
    virtual void DirectRunOnMainThread(bool isSync, const KRSchedulerTask &task) = 0;

    virtual bool IsCurrentOnContextThread() = 0;
};

class KRContextSchedulerMultiThreaded : public KRContextSchedulerInternal {
 public:
    void ScheduleTask(bool sync, int delayMs, const KRSchedulerTask &task) override;
    void ScheduleTaskOnMainThread(bool sync, const KRSchedulerTask &task) override;
    void DirectRunOnMainThread(bool isSync, const KRSchedulerTask &task) override;
    bool IsCurrentOnContextThread() override;

 private:
    static KRThread *GetContextThread() {
        static KRThread *gContextThread = new KRThread("kuikly");
        return gContextThread;
    }
    static std::atomic_bool runningOnMainThread;
    static std::thread::id mainThreadId;
};

std::atomic_bool KRContextSchedulerMultiThreaded::runningOnMainThread{false};
std::thread::id KRContextSchedulerMultiThreaded::mainThreadId;

void KRContextSchedulerMultiThreaded::DirectRunOnMainThread(bool isSync, const KRSchedulerTask &task) {
    KR_LOG_INFO << "多线程模式下 调用 DirectRunOnMainThread  isSync=" <<isSync;
    if (isSync) {
         KR_LOG_INFO << "多线程模式下 调用 DirectRunOnMainThread  同步调用";
        GetContextThread()->DirectRunOnCurThread([task]() {
            mainThreadId = std::this_thread::get_id();
            runningOnMainThread.store(true);
            task();
            runningOnMainThread.store(false);
        });
    } else {
         KR_LOG_INFO << "多线程模式下 调用 DirectRunOnMainThread  异步调用 DispatchAsync=";
        GetContextThread()->DispatchAsync(task, 0);
    }
}

void KRContextSchedulerMultiThreaded::ScheduleTask(bool sync, int delayMs, const KRSchedulerTask &task) {
    if (sync) {
        GetContextThread()->DispatchSync(task);
    } else {
        GetContextThread()->DispatchAsync(task, delayMs);
    }
}

void KRContextSchedulerMultiThreaded::ScheduleTaskOnMainThread(bool sync, const KRSchedulerTask &task) {
    if (sync) {
        KR_LOG_INFO << "ScheduleTaskOnMainThread  内部 同步调用" ;
        if (GetContextThread()->IsCurrentThreadWorkerThread()) {
            
             //尝试修复版本 begin
//             KR_LOG_INFO << "在kuikly 线程 ScheduleTaskOnMainThread   内部 同步调用" ;
//            GetContextThread()->SyncMainTaskMutex(true, false, false);
//            auto done = std::make_shared<std::promise<void>>();
//            std::future<void> fut = done->get_future();
//            KRMainThread::RunOnMainThread([task, done]() {
//                KR_LOG_INFO << "开始 运行主线程task RunOnMainThread " ;
//                try {
//                    task();
//                    done->set_value();
//                } catch (...) {
//                    done->set_exception(std::current_exception());
//                }
//                KR_LOG_INFO << "完成 运行主线程task RunOnMainThread finish " ;
//            });
//            KR_LOG_INFO << "等待主线程 task 完成" ;
//            fut.get();
//            KR_LOG_INFO << "主线程任务 运行完成 kuikly 继续执行1" ;
//            GetContextThread()->SyncMainTaskMutex(false, true, false);
//            KR_LOG_INFO << "主线程任务 运行完成 kuikly 继续执行2" ;
            
             //尝试修复版本 end
            
                    //原有版本 begin
             KR_LOG_INFO << "在kuikly 线程 ScheduleTaskOnMainThread   内部 同步调用" ;
               GetContextThread()->SyncMainTaskMutex(true, false, false);
            std::mutex mtx;
            std::condition_variable cv;
            std::atomic<bool> taskCompleted(false);
            std::unique_lock<std::mutex> lock(mtx);
            KRMainThread::RunOnMainThread([task, &cv, &taskCompleted] {
                 KR_LOG_INFO << "开始 运行主线程task RunOnMainThread " ;
                task();
                taskCompleted.store(true);
                  KR_LOG_INFO << "完成 运行主线程task RunOnMainThread finish " ;
                cv.notify_one();
                 KR_LOG_INFO << "完成 运行主线程task RunOnMainThread finish notify kuikly现成继续执行" ;
            });
             KR_LOG_INFO << "等待主线程 task 完成" ;
            cv.wait(lock, [&taskCompleted] { return taskCompleted.load(); });
             KR_LOG_INFO << "主线程任务 运行完成 kuikly 继续执行1" ;
            GetContextThread()->SyncMainTaskMutex(false, true, false);
               KR_LOG_INFO << "主线程任务 运行完成 kuikly 继续执行2" ;
            //原有版本 end
        } else {
            // 说明在主线程, 直接同步
            KR_LOG_INFO << "主线程 直接  同步调用" ;
            task();
        }
        

    } else {
        KR_LOG_INFO << "ScheduleTaskOnMainThread  内部 异步调用" ;
        if (GetContextThread()->IsCurrentThreadWorkerThread()) {
            KR_LOG_INFO << "ScheduleTaskOnMainThread  在 kuikly线程 调用RunOnMainThread" ;
            KRMainThread::RunOnMainThread([task] { task(); });
        } else {
               KR_LOG_INFO << "ScheduleTaskOnMainThread  主线程直接调用 task" ;
            task();
        }
    }
}

bool KRContextSchedulerMultiThreaded::IsCurrentOnContextThread() {
    if (runningOnMainThread.load()) {
        return std::this_thread::get_id() == mainThreadId;
    }
    return GetContextThread()->IsCurrentThreadWorkerThread();
}

class KRContextSchedulerSingleThreaded : public KRContextSchedulerInternal {
 public:
    void ScheduleTask(bool sync, int delayMs, const KRSchedulerTask &task) override;
    void ScheduleTaskOnMainThread(bool sync, const KRSchedulerTask &task) override;
    void DirectRunOnMainThread(bool isSync, const KRSchedulerTask &task) override;
    bool IsCurrentOnContextThread() override;
    std::thread::id mainThreadId;
};

void KRContextSchedulerSingleThreaded::DirectRunOnMainThread(bool isSync, const KRSchedulerTask &task) {
    mainThreadId = std::this_thread::get_id();
    if (isSync) {
        task();
    } else {
        KRMainThread::RunOnMainThread(task, 0);
    }
}

void KRContextSchedulerSingleThreaded::ScheduleTask(bool sync, int delayMs, const KRSchedulerTask &task) {
    if (sync) {
        task();
    } else {
        KRMainThread::RunOnMainThread(task, delayMs);
    }
}

void KRContextSchedulerSingleThreaded::ScheduleTaskOnMainThread(bool sync, const KRSchedulerTask &task) {
    if (sync) {
        task();
    } else {
        KRMainThread::RunOnMainThread([task] { task(); });
    }
}

bool KRContextSchedulerSingleThreaded::IsCurrentOnContextThread() {
    return mainThreadId == std::this_thread::get_id();
}

static KRContextScheduler::ThreadingMode gThreadingMode = KRContextScheduler::ThreadingMode::MultiThread;

void KRContextScheduler::SetThreadingMode(ThreadingMode mode) {
    // 仅应在初始化前调用一次，并仅仅使用一次，无需考虑多线程问题
    gThreadingMode = mode;
}

std::shared_ptr<KRContextSchedulerInternal> KRContextScheduler::GetInstance() {
    static std::shared_ptr<KRContextSchedulerInternal> instance_ = nullptr;
    static std::once_flag flag;
    std::call_once(flag, []() {
        instance_ = gThreadingMode == KRContextScheduler::ThreadingMode::MultiThread
                        ? std::dynamic_pointer_cast<KRContextSchedulerInternal>(
                              std::make_shared<KRContextSchedulerMultiThreaded>())
                        : std::dynamic_pointer_cast<KRContextSchedulerInternal>(
                              std::make_shared<KRContextSchedulerSingleThreaded>());
    });
    return instance_;
}

void KRContextScheduler::ScheduleTask(bool sync, int delayMs, const KRSchedulerTask &task) {
    GetInstance()->ScheduleTask(sync, delayMs, task);
}
void KRContextScheduler::ScheduleTaskOnMainThread(bool sync, const KRSchedulerTask &task) {
    KR_LOG_INFO << "调用 ScheduleTaskOnMainThread  issync=" << sync;
    GetInstance()->ScheduleTaskOnMainThread(sync, task);
}
void KRContextScheduler::DirectRunOnMainThread(bool isSync, const KRSchedulerTask &task) {
    GetInstance()->DirectRunOnMainThread(isSync, task);
}
bool KRContextScheduler::IsCurrentOnContextThread() {
    return GetInstance()->IsCurrentOnContextThread();
}

EXTERN_C_START
/**
 * 让用户设置线程模型。
 * @param mode 0 ：默认模式，多线程， 1 ：单线程同步模式
 *
 * 线程模式暂时仅允许深度合作用户进行设置，暂不暴露到头文件。
 */
void KRSetThreadingMode(int mode) {
    KRContextScheduler::SetThreadingMode(static_cast<KRContextScheduler::ThreadingMode>(!!mode));
}
EXTERN_C_END
