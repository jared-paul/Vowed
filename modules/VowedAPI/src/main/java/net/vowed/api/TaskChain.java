package net.vowed.api;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * TaskChain v2.3.2 - by Daniel Ennis <aikar@aikar.co>
 *
 * Facilitates Control Flow for the Bukkit Scheduler to easily jump between
 * Async and Sync tasks without deeply nested callbacks, passing the response of the
 * previous task to the next task to use.
 *
 * REQUIREMENT: Must call TaskChain.initialize() in your plugin onEnable like so:
 *
 * TaskChain.initialize(this);
 *
 * Usage example:
 * @see #example
 */
@SuppressWarnings({"unused", "FieldAccessedSynchronizedAndUnsynchronized"})
public class TaskChain<T> {

    /**
     * A useless example of registering multiple task signatures and states
     */
    public static void example() {
        log("Starting example");

        TaskChain<?> chain = TaskChain.newSharedChain("TEST");
        chain
                .delay(20 * 3)
                .sync(() -> {
                    Object test = chain.setTaskData("test", 1);
                    log("==> 1st test");
                })
                .delay(20)
                .async(() -> {
                    Object test = chain.getTaskData("test");
                    log("==> 2nd test: " + test + " = should be 1");
                })
                .sync(TaskChain::abort)
                .execute();


        // This chain essentially appends onto the previous one, and will not overlap
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            TaskChain<?> chain2 = TaskChain.newSharedChain("TEST");
            chain2
                    .sync(() -> {
                        Object test = chain2.getTaskData("test");
                        log("==> 3rd test: " + test + " = should be null");
                    })
                    .delay(20)
                    .async(TaskChain::abort)
                    .execute();

            TaskChain
                    .newSharedChain("TEST")
                    .async(() -> log("==> 4th test - should print"))
                    .returnData("notthere")
                    .abortIfNull()
                    .syncLast((val) -> log("Shouldn't execute due to null abort"))
                    .execute();
        });
        TaskChain
                .newSharedChain("TEST2")
                .delay(20 * 3)
                .sync(() -> log("this should run at same time as 1st test"))
                .delay(20)
                .async(() -> log("this should run at same time as 2nd test"))
                .execute();
        TaskChain
                .newChain()
                .sync(() -> log("THE FIRST!"))
                .delay(20 * 10) // Wait 20s to start any task
                .async(() -> log("This ran async - with no input or return"))
                .<Integer>asyncFirstCallback(next -> {
                    // Use a callback to provide result
                    log("this also ran async, but will call next task in 3 seconds.");
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> next.accept(3), 60);
                })
                .sync(input -> { // Will be ran 3s later but didn't use .delay()
                    log("should of got 3: " + input);
                    return 5 + input;
                })
                .storeAsData("Test1")
                .syncLast(input2 -> log("should be 8: " + input2)) // Consumes last result, but doesn't pass a new one
                .delay(20) // Wait 1s until next
                .sync(() -> log("Generic 1s later")) // no input expected, no output, run sync
                .asyncFirst(() -> 3) // Run task async and return 3
                .delay(5 * 20) // Wait 5s
                .asyncLast(input1 -> log("async last value 5s later: " + input1)) // Run async again, with value of 3
                .<Integer>returnData("Test1")
                .asyncLast((val) -> log("Should of got 8 back from data: " + val))
                .sync(TaskChain::abort)
                .sync(() -> log("Shouldn't be called"))
                .execute();
    }

    /**
     * Util method for example logging
     * @param log
     */
    private static void log(String log) {
        Logger.getGlobal().info(log);
    }

    /**
     * =============================================================================================
     */
    private static Plugin plugin;
    private static final Map<String, TaskChain<?>> sharedChains = new HashMap<>();

    private boolean shared = false;
    private boolean done = false;
    private boolean executed = false;
    private boolean async = !Bukkit.isPrimaryThread();
    private String sharedName;
    private Object previous;
    private final Map<String, Object> taskMap = new HashMap<>(0);

    private TaskHolder<?, ?> currentHolder;
    protected Runnable doneCallback;
    protected final ConcurrentLinkedQueue<TaskHolder<?,?>> chainQueue = new ConcurrentLinkedQueue<>();

    /**
     * =============================================================================================
     */

    /**
     * Initializes TaskChain with the owning plugin. Must be called before any use.
     * @param owner
     */
    public static void initialize(Plugin owner) {
        plugin = owner;
    }

    /**
     * Starts a new chain.
     * @return
     */
    public static <T> TaskChain<T> newChain() {
        if (plugin == null) {
            throw new IllegalStateException("TaskChain has not been configured. Please call TaskChain.initialize(plugin);");
        }
        return new TaskChain<>();
    }

    /**
     * Allows re-use of a Chain by giving it a name. This lets you keep adding Tasks to
     * an already executing chain. This allows you to assure a sequence of events to only
     * execute one at a time, but may be registered and executed from multiple execution points
     * or threads.
     *
     * Task Data is not shared between chains of the same name. The only thing that is shared
     * is execution order, in that 2 sequences of events can not run at the same time.
     *
     * If 2 chains are created at same time under same name, the first chain will execute fully before the 2nd chain will start, no matter how long
     *
     * @param name
     * @param <T>
     * @return
     */
    public static synchronized <T> TaskChain<T> newSharedChain(String name) {
        TaskChain<?> chain;
        synchronized (sharedChains) {
            chain = sharedChains.get(name);
        }

        if (chain != null) {
            synchronized (chain) {
                if (chain.done) {
                    chain = null;
                }
            }
        }

        if (chain == null) {
            chain = newChain();
            chain.shared = true;
            chain.sharedName = name;
            sharedChains.put(name, chain);
        }

        return new SharedTaskChain<>((TaskChain<T>) chain);
    }

    /**
     * Creates a shared chain bound to a specific player with default name
     * @see #newSharedChain(String) for full documentation
     * @param player
     * @param <T>
     * @return
     */
    public static <T> TaskChain<T> newSharedChain(Player player) {
        return newSharedChain(player, "__MAIN__");
    }

    /**
     * Creates a shared chain bound to a specific player with specified name
     * @see #newSharedChain(String) for full documentation
     * @param player
     * @param name
     * @param <T>
     * @return
     */
    public static <T> TaskChain<T> newSharedChain(Player player, String name) {
        return newSharedChain(player.getUniqueId() + "__PlayerChain__" + name);
    }


    /**
     * Call to abort execution of the chain.
     */
    public static void abort() throws AbortChainException {
        throw new AbortChainException();
    }

    /**
     * =============================================================================================
     */

    /**
     * Checks if the chain has a value saved for the specified key.
     * @param key
     * @return
     */
    public boolean hasTaskData(String key) {
        return taskMap.containsKey(key);
    }

    /**
     * Retrieves a value relating to a specific key, saved by a previous task.
     *
     * @param key
     * @param <R>
     * @return
     */
    public <R> R getTaskData(String key) {
        return (R) taskMap.get(key);
    }

    /**
     * Saves a value for this chain so that a task furthur up the chain can access it.
     *
     * Useful for passing multiple values to the next (or furthur) tasks.
     *
     * @param key
     * @param val
     * @param <R>
     * @return
     */
    public <R> R setTaskData(String key, Object val) {
        return (R) taskMap.put(key, val);
    }

    /**
     * Removes a saved value on the chain.
     *
     * @param key
     * @param <R>
     * @return
     */
    public <R> R removeTaskData(String key) {
        return (R) taskMap.remove(key);
    }

    /**
     * =============================================================================================
     */

    /**
     * Checks if the previous task return was null.
     *
     * If not null, the previous task return will forward to the next task.
     * @return
     */
    public TaskChain<T> abortIfNull() {
        return abortIfNull(null, null);
    }

    /**
     * Checks if the previous task return was null, and aborts if it was, optionally
     * sending a message to the player.
     *
     * If not null, the previous task return will forward to the next task.
     * @param player
     * @param msg
     * @return
     */
    public TaskChain<T> abortIfNull(Player player, String msg) {
        return current((obj) -> {
            if (obj == null) {
                if (msg != null && player != null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
                abort();
                return null;
            }
            return obj;
        });
    }

    /**
     * Takes the previous tasks return value, stores it to the specified key
     * as Task Data, and then forwards that value to the next task.
     *
     * @param key
     * @return
     */
    public TaskChain<T> storeAsData(String key) {
        return current((val) -> {
            setTaskData(key, val);
            return val;
        });
    }

    /**
     * Reads the specified key from Task Data, and passes it to the next task.
     *
     * Will need to pass expected type such as chain.<Foo>returnData("key")
     *
     * @param key
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> returnData(String key) {
        return currentFirst(() -> (R) getTaskData(key));
    }

    /**
     * Adds a delay to the chain execution.
     *
     * @param ticks # of ticks to delay before next task (20 = 1 second)
     * @return
     */
    public TaskChain<T> delay(final int ticks) {
        //noinspection CodeBlock2Expr
        return currentCallback((input, next) -> {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> next.accept(input), ticks);
        });
    }

    /**
     * Execute a task on the main thread, with no previous input, and a callback to return the response to.
     *
     * It's important you don't perform blocking operations in this method. Only use this if
     * the task will be scheduling a different sync operation outside of the TaskChains scope.
     *
     * Usually you could achieve the same design with a blocking API by switching to an async task
     * for the next task and running it there.
     *
     * This method would primarily be for cases where you need to use an API that ONLY provides
     * a callback style API.
     *
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> syncFirstCallback(AsyncExecutingFirstTask<R> task) {
        return add0(new TaskHolder<>(this, false, task));
    }

    /**
     * @see #syncFirstCallback(AsyncExecutingFirstTask) but ran off main thread
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> asyncFirstCallback(AsyncExecutingFirstTask<R> task) {
        return add0(new TaskHolder<>(this, true, task));
    }

    /**
     * @see #syncFirstCallback(AsyncExecutingFirstTask) but ran on current thread the Chain was created on
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> currentFirstCallback(AsyncExecutingFirstTask<R> task) {
        return add0(new TaskHolder<>(this, null, task));
    }

    /**
     * Execute a task on the main thread, with the last output, and a callback to return the response to.
     *
     * It's important you don't perform blocking operations in this method. Only use this if
     * the task will be scheduling a different sync operation outside of the TaskChains scope.
     *
     * Usually you could achieve the same design with a blocking API by switching to an async task
     * for the next task and running it there.
     *
     * This method would primarily be for cases where you need to use an API that ONLY provides
     * a callback style API.
     *
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> syncCallback(AsyncExecutingTask<R, T> task) {
        return add0(new TaskHolder<>(this, false, task));
    }

    public TaskChain<?> syncCallback(AsyncExecutingGenericTask task) {
        return add0(new TaskHolder<>(this, false, task));
    }

    /**
     * @see #syncCallback(AsyncExecutingTask) but ran off main thread
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> asyncCallback(AsyncExecutingTask<R, T> task) {
        return add0(new TaskHolder<>(this, true, task));
    }

    /**
     * @see #syncCallback(AsyncExecutingTask) but ran off main thread
     * @param task
     * @return
     */
    public TaskChain<?> asyncCallback(AsyncExecutingGenericTask task) {
        return add0(new TaskHolder<>(this, true, task));
    }

    /**
     * @see #syncCallback(AsyncExecutingTask) but ran on current thread the Chain was created on
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> currentCallback(AsyncExecutingTask<R, T> task) {
        return add0(new TaskHolder<>(this, null, task));
    }

    /**
     * @see #syncCallback(AsyncExecutingTask) but ran on current thread the Chain was created on
     * @param task
     * @return
     */
    public TaskChain<?> currentCallback(AsyncExecutingGenericTask task) {
        return add0(new TaskHolder<>(this, null, task));
    }

    /**
     * Execute task on main thread, with no input, returning an output
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> syncFirst(FirstTask<R> task) {
        return add0(new TaskHolder<>(this, false, task));
    }

    /**
     * @see #syncFirst(FirstTask) but ran off main thread
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> asyncFirst(FirstTask<R> task) {
        return add0(new TaskHolder<>(this, true, task));
    }

    /**
     * @see #syncFirst(FirstTask) but ran on current thread the Chain was created on
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> currentFirst(FirstTask<R> task) {
        return add0(new TaskHolder<>(this, null, task));
    }

    /**
     * Execute task on main thread, with the last returned input, returning an output
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> sync(Task<R, T> task) {
        return add0(new TaskHolder<>(this, false, task));
    }

    /**
     * Execute task on main thread, with no input or output
     * @param task
     * @return
     */
    public TaskChain<?> sync(GenericTask task) {
        return add0(new TaskHolder<>(this, false, task));
    }

    /**
     * @see #sync(Task) but ran off main thread
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> async(Task<R, T> task) {
        return add0(new TaskHolder<>(this, true, task));
    }
    /**
     * @see #sync(GenericTask) but ran off main thread
     * @param task
     * @return
     */
    public TaskChain<?> async(GenericTask task) {
        return add0(new TaskHolder<>(this, true, task));
    }

    /**
     * @see #sync(Task) but ran on current thread the Chain was created on
     * @param task
     * @param <R>
     * @return
     */
    public <R> TaskChain<R> current(Task<R, T> task) {
        return add0(new TaskHolder<>(this, null, task));
    }
    /**
     * @see #sync(GenericTask) but ran on current thread the Chain was created on
     * @param task
     * @return
     */
    public TaskChain<?> current(GenericTask task) {
        return add0(new TaskHolder<>(this, null, task));
    }


    /**
     * Execute task on main thread, with the last output, and no furthur output
     * @param task
     * @return
     */
    public TaskChain<?> syncLast(LastTask<T> task) {
        return add0(new TaskHolder<>(this, false, task));
    }

    /**
     * @see #syncLast(LastTask) but ran off main thread
     * @param task
     * @return
     */
    public TaskChain<?> asyncLast(LastTask<T> task) {
        return add0(new TaskHolder<>(this, true, task));
    }

    /**
     * @see #syncLast(LastTask) but ran on current thread the Chain was created on
     * @param task
     * @return
     */
    public TaskChain<?> currentLast(LastTask<T> task) {
        return add0(new TaskHolder<>(this, null, task));
    }


    /**
     * Finished adding tasks, begins executing them.
     */
    public void execute() {
        execute0();
    }
    protected void execute0() {
        synchronized (this) {
            if (this.executed) {
                if (this.shared) {
                    return;
                }
                throw new RuntimeException("Already executed and not a shared chain");
            }
            this.executed = true;
        }
        nextTask();
    }
    public void executeNext() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::execute, 0);
    }

    public void execute(Runnable done) {
        this.doneCallback = done;
        execute();
    }

    protected void done() {
        this.done = true;
        if (this.shared) {
            synchronized (sharedChains) {
                sharedChains.remove(this.sharedName);
            }
        }
        if (this.doneCallback != null) {
            this.doneCallback.run();
        }
    }

    @SuppressWarnings("rawtypes")
    protected TaskChain add0(TaskHolder<?,?> task) {
        synchronized (this) {
            if (!this.shared && this.executed) {
                throw new RuntimeException("TaskChain is executing and not shared");
            }
        }

        this.chainQueue.add(task);
        return this;
    }

    /**
     * Fires off the next task, and switches between Async/Sync as necessary.
     */
    private void nextTask() {
        synchronized (this) {
            this.currentHolder = this.chainQueue.poll();
            if (this.currentHolder == null) {
                this.done = true; // to ensure its done while synchronized
            }
        }

        if (this.currentHolder == null) {
            this.previous = null;
            // All Done!
            this.done();
            return;
        }


        Boolean isNextAsync = this.currentHolder.async;
        if (isNextAsync == null) {
            isNextAsync = this.async;
        }

        if (isNextAsync) {
            if (this.async) {
                this.currentHolder.run();
            } else {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    this.async = true;
                    this.currentHolder.run();
                });
            }
        } else {
            if (this.async) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    this.async = false;
                    this.currentHolder.run();
                });
            } else {
                this.currentHolder.run();
            }
        }
    }

    /**
     * Provides foundation of a task with what the previous task type should return
     * to pass to this and what this task will return.
     * @param <R> Return Type
     * @param <A> Argument Type Expected
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    private static class TaskHolder<R, A> {
        private final TaskChain<?> chain;
        private final Task<R, A> task;
        public final Boolean async;

        private boolean executed = false;
        private boolean aborted = false;

        private TaskHolder(TaskChain<?> chain, Boolean async, Task<R, A> task) {
            this.task = task;
            this.chain = chain;
            this.async = async;
        }


        /**
         * Called internally by Task Chain to facilitate executing the task and then the next task.
         */
        private void run() {
            final Object arg = this.chain.previous;
            this.chain.previous = null;
            final R res;
            try {
                if (this.task instanceof AsyncExecutingTask) {
                    ((AsyncExecutingTask<R, A>) this.task).runAsync((A) arg, this::next);
                } else {
                    next(this.task.run((A) arg));
                }
            } catch (AbortChainException ignored) {
                this.abort();
            }
        }

        /**
         * Abort the chain, and clear tasks for GC.
         */
        private synchronized void abort() {
            this.aborted = true;
            this.chain.previous = null;
            this.chain.chainQueue.clear();
            this.chain.done();
        }

        /**
         * Accepts result of previous task and executes the next
         */
        private void next(Object resp) {
            synchronized (this) {
                if (this.aborted) {
                    this.chain.done();
                    return;
                }
                if (this.executed) {
                    this.chain.done();
                    throw new RuntimeException("This task has already been executed.");
                }
                this.executed = true;
            }

            this.chain.async = !Bukkit.isPrimaryThread(); // We don't know where the task called this from.
            this.chain.previous = resp;
            this.chain.nextTask();
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public static class AbortChainException extends Throwable {}

    /**
     * Generic task with synchronous return (but may execute on any thread)
     * @param <R>
     * @param <A>
     */
    public interface Task <R, A> {
        R run(A input) throws AbortChainException;
    }

    public interface AsyncExecutingTask<R, A> extends Task<R, A> {
        @Override
        default R run(A input) throws AbortChainException {
            // unused
            return null;
        }

        void runAsync(A input, Consumer<R> next) throws AbortChainException;
    }
    public interface FirstTask <R> extends Task<R, Object> {
        @Override
        default R run(Object input) throws AbortChainException {
            return run();
        }

        R run() throws AbortChainException;
    }
    public interface AsyncExecutingFirstTask<R> extends AsyncExecutingTask<R, Object> {
        @Override
        default R run(Object input) throws AbortChainException {
            // Unused
            return null;
        }

        @Override
        default void runAsync(Object input, Consumer<R> next) throws AbortChainException {
            run(next);
        }

        void run(Consumer<R> next) throws AbortChainException;
    }
    public interface LastTask <A> extends Task<Object, A> {
        @Override
        default Object run(A input) throws AbortChainException {
            runLast(input);
            return null;
        }
        void runLast(A input) throws AbortChainException;
    }
    public interface GenericTask extends Task<Object, Object> {
        @Override
        default Object run(Object input) throws AbortChainException {
            runGeneric();
            return null;
        }
        void runGeneric() throws AbortChainException;
    }
    public interface AsyncExecutingGenericTask extends AsyncExecutingTask<Object, Object> {
        @Override
        default Object run(Object input) throws AbortChainException {
            return null;
        }
        @Override
        default void runAsync(Object input, Consumer<Object> next) throws AbortChainException {
            run(() -> next.accept(null));
        }

        void run(Runnable next) throws AbortChainException;
    }

    private static class SharedTaskChain<R>  extends TaskChain<R> {
        private final TaskChain<R> backingChain;
        private SharedTaskChain(TaskChain<R> backingChain) {
            this.backingChain = backingChain;
        }

        @Override
        public void execute() {
            synchronized (backingChain) {
                // This executes SharedTaskChain.execute(Runnable), which says execute
                // my wrapped chains queue of events, but pass a done callback for when its done.
                // We then use the backing chain callback method to not execute the next task in the
                // backing chain until the current one is fully done.
                SharedTaskChain<R> sharedChain = this;
                backingChain.currentCallback((next) -> {
                    sharedChain.execute(next::run);
                });
                backingChain.executeNext();
            }
        }

        @Override
        public void execute(Runnable done) {
            this.doneCallback = done;
            execute0();
        }
    }
}
