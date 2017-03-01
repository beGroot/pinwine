// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AsyncTask.java

package net.tsz.afinal.core;

import android.os.*;
import android.util.Log;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// Referenced classes of package net.tsz.afinal.core:
//            ArrayDeque

public abstract class AsyncTask
{
    private static class AsyncTaskResult
    {

        final AsyncTask mTask;
        final Object mData[];

        transient AsyncTaskResult(AsyncTask task, Object data[])
        {
            mTask = task;
            mData = data;
        }
    }

    private static class InternalHandler extends Handler
    {

        public void handleMessage(Message msg)
        {
            AsyncTaskResult result = (AsyncTaskResult)msg.obj;
            switch(msg.what)
            {
            case 1: // '\001'
                result.mTask.finish(result.mData[0]);
                break;

            case 2: // '\002'
                result.mTask.onProgressUpdate(result.mData);
                break;
            }
        }

        private InternalHandler()
        {
        }

        InternalHandler(InternalHandler internalhandler)
        {
            this();
        }
    }

    private static class SerialExecutor
        implements Executor
    {

        public synchronized void execute(final Runnable r)
        {
            mTasks.offer(new Runnable() {

                public void run()
                {
                    r.run();
                    break MISSING_BLOCK_LABEL_22;
                    Exception exception;
                    exception;
                    scheduleNext();
                    throw exception;
                    scheduleNext();
                    return;
                }

                final SerialExecutor this$1;
                private final Runnable val$r;

                
                {
                    this$1 = SerialExecutor.this;
                    r = runnable;
                    super();
                }
            }
);
            if(mActive == null)
                scheduleNext();
        }

        protected synchronized void scheduleNext()
        {
            if((mActive = (Runnable)mTasks.poll()) != null)
                AsyncTask.THREAD_POOL_EXECUTOR.execute(mActive);
        }

        final ArrayDeque mTasks;
        Runnable mActive;

        private SerialExecutor()
        {
            mTasks = new ArrayDeque();
        }

        SerialExecutor(SerialExecutor serialexecutor)
        {
            this();
        }
    }

    public static final class Status extends Enum
    {

        public static Status[] values()
        {
            Status astatus[];
            int i;
            Status astatus1[];
            System.arraycopy(astatus = ENUM$VALUES, 0, astatus1 = new Status[i = astatus.length], 0, i);
            return astatus1;
        }

        public static Status valueOf(String s)
        {
            return (Status)Enum.valueOf(net/tsz/afinal/core/AsyncTask$Status, s);
        }

        public static final Status PENDING;
        public static final Status RUNNING;
        public static final Status FINISHED;
        private static final Status ENUM$VALUES[];

        static 
        {
            PENDING = new Status("PENDING", 0);
            RUNNING = new Status("RUNNING", 1);
            FINISHED = new Status("FINISHED", 2);
            ENUM$VALUES = (new Status[] {
                PENDING, RUNNING, FINISHED
            });
        }

        private Status(String s, int i)
        {
            super(s, i);
        }
    }

    private static abstract class WorkerRunnable
        implements Callable
    {

        Object mParams[];

        private WorkerRunnable()
        {
        }

        WorkerRunnable(WorkerRunnable workerrunnable)
        {
            this();
        }
    }


    public static void init()
    {
        sHandler.getLooper();
    }

    public static void setDefaultExecutor(Executor exec)
    {
        sDefaultExecutor = exec;
    }

    public AsyncTask()
    {
        mStatus = Status.PENDING;
        mFuture = new FutureTask(mWorker) {

            protected void done()
            {
                try
                {
                    postResultIfNotInvoked(get());
                }
                catch(InterruptedException e)
                {
                    Log.w("AsyncTask", e);
                }
                catch(ExecutionException e)
                {
                    throw new RuntimeException("An error occured while executing doInBackground()", e.getCause());
                }
                catch(CancellationException e)
                {
                    postResultIfNotInvoked(null);
                }
            }

            final AsyncTask this$0;

            
            {
                this$0 = AsyncTask.this;
                super($anonymous0);
            }
        }
;
    }

    private void postResultIfNotInvoked(Object result)
    {
        boolean wasTaskInvoked = mTaskInvoked.get();
        if(!wasTaskInvoked)
            postResult(result);
    }

    private Object postResult(Object result)
    {
        Message message = sHandler.obtainMessage(1, new AsyncTaskResult(this, new Object[] {
            result
        }));
        message.sendToTarget();
        return result;
    }

    public final Status getStatus()
    {
        return mStatus;
    }

    protected transient abstract Object doInBackground(Object aobj[]);

    protected void onPreExecute()
    {
    }

    protected void onPostExecute(Object obj)
    {
    }

    protected transient void onProgressUpdate(Object aobj[])
    {
    }

    protected void onCancelled(Object result)
    {
        onCancelled();
    }

    protected void onCancelled()
    {
    }

    public final boolean isCancelled()
    {
        return mCancelled.get();
    }

    public final boolean cancel(boolean mayInterruptIfRunning)
    {
        mCancelled.set(true);
        return mFuture.cancel(mayInterruptIfRunning);
    }

    public final Object get()
        throws InterruptedException, ExecutionException
    {
        return mFuture.get();
    }

    public final Object get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException
    {
        return mFuture.get(timeout, unit);
    }

    public final transient AsyncTask execute(Object params[])
    {
        return executeOnExecutor(sDefaultExecutor, params);
    }

    public final transient AsyncTask executeOnExecutor(Executor exec, Object params[])
    {
        if(mStatus != Status.PENDING)
            switch($SWITCH_TABLE$net$tsz$afinal$core$AsyncTask$Status()[mStatus.ordinal()])
            {
            case 2: // '\002'
                throw new IllegalStateException("Cannot execute task: the task is already running.");

            case 3: // '\003'
                throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        mStatus = Status.RUNNING;
        onPreExecute();
        mWorker.mParams = params;
        exec.execute(mFuture);
        return this;
    }

    public static void execute(Runnable runnable)
    {
        sDefaultExecutor.execute(runnable);
    }

    protected final transient void publishProgress(Object values[])
    {
        if(!isCancelled())
            sHandler.obtainMessage(2, new AsyncTaskResult(this, values)).sendToTarget();
    }

    private void finish(Object result)
    {
        if(isCancelled())
            onCancelled(result);
        else
            onPostExecute(result);
        mStatus = Status.FINISHED;
    }

    static int[] $SWITCH_TABLE$net$tsz$afinal$core$AsyncTask$Status()
    {
        $SWITCH_TABLE$net$tsz$afinal$core$AsyncTask$Status;
        if($SWITCH_TABLE$net$tsz$afinal$core$AsyncTask$Status == null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        JVM INSTR pop ;
        int ai[] = new int[Status.values().length];
        try
        {
            ai[Status.FINISHED.ordinal()] = 3;
        }
        catch(NoSuchFieldError _ex) { }
        try
        {
            ai[Status.PENDING.ordinal()] = 1;
        }
        catch(NoSuchFieldError _ex) { }
        try
        {
            ai[Status.RUNNING.ordinal()] = 2;
        }
        catch(NoSuchFieldError _ex) { }
        return $SWITCH_TABLE$net$tsz$afinal$core$AsyncTask$Status = ai;
    }

    private static final String LOG_TAG = "AsyncTask";
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
    private static final ThreadFactory sThreadFactory;
    private static final BlockingQueue sPoolWorkQueue;
    public static final Executor THREAD_POOL_EXECUTOR;
    public static final Executor SERIAL_EXECUTOR;
    public static final Executor DUAL_THREAD_EXECUTOR;
    private static final int MESSAGE_POST_RESULT = 1;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final InternalHandler sHandler = new InternalHandler(null);
    private static volatile Executor sDefaultExecutor;
    private final WorkerRunnable mWorker = new WorkerRunnable() {

        public Object call()
            throws Exception
        {
            mTaskInvoked.set(true);
            Process.setThreadPriority(10);
            return postResult(doInBackground(mParams));
        }

        final AsyncTask this$0;

            
            {
                this$0 = AsyncTask.this;
                super(null);
            }
    }
;
    private final FutureTask mFuture;
    private volatile Status mStatus;
    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private static int $SWITCH_TABLE$net$tsz$afinal$core$AsyncTask$Status[];

    static 
    {
        sThreadFactory = new ThreadFactory() {

            public Thread newThread(Runnable r)
            {
                Thread tread = new Thread(r, (new StringBuilder("AsyncTask #")).append(mCount.getAndIncrement()).toString());
                tread.setPriority(4);
                return tread;
            }

            private final AtomicInteger mCount = new AtomicInteger(1);

        }
;
        sPoolWorkQueue = new LinkedBlockingQueue(10);
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, new java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy());
        SERIAL_EXECUTOR = new SerialExecutor(null);
        DUAL_THREAD_EXECUTOR = Executors.newFixedThreadPool(3, sThreadFactory);
        sDefaultExecutor = SERIAL_EXECUTOR;
    }




}
