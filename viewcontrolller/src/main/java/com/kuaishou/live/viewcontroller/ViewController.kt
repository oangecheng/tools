package com.kuaishou.live.viewcontroller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import kotlin.IllegalStateException

/**
 *
 * 在ViewController嵌套的场景，生命周期的回调顺序
 * 1 onCreate,  parent -> children
 * 2 onStart,  parent -> children
 * 3 onResume,  parent -> children
 * 4 onStop,   children -> parent
 * 5 onPause   children -> parent
 * 6 onDestroy children -> parent
 */
abstract class ViewController : LifecycleOwner, ViewModelStoreOwner {

    interface Checker<T> {
        fun valid(data: CheckerData<T>) : Boolean
    }

    data class CheckerData<T>(
        val view: View,
        val initState : T
    )

    companion object {
        private val VISIBLE_CHECKER = object : Checker<Int> {
            override fun valid(data: CheckerData<Int>): Boolean {
                return data.view.visibility == data.initState
            }
        }
    }

    private var container: ViewGroup? = null
    internal var contentView: View? = null
    protected lateinit var context: Context
        private set

    protected lateinit var activity: Activity
        private set

    internal lateinit var controllerManager: ViewControllerManager
    private lateinit var childControllerManager: ViewControllerManagerImpl
    private val viewModelStore = ViewModelStore()


    protected val viewControllerManager: ViewControllerManager
        get() {
            if (!this::controllerManager.isInitialized) {
                throw IllegalStateException("must call viewControllerManager inside/after onCreate")
            }

            return childControllerManager
        }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    @Suppress("LeakingThis")
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }


    private var visibilityData : CheckerData<Int>? = null


    init {
        // VC自己的生命周期，一定是第一个observer
        // 因为先注册的Lifecycle的Scope更大
        lifecycleRegistry.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> onCreate()
                    Lifecycle.Event.ON_RESUME -> onResume()
                    Lifecycle.Event.ON_START -> onStart()
                    Lifecycle.Event.ON_PAUSE -> onPause()
                    Lifecycle.Event.ON_STOP -> performStop()
                    Lifecycle.Event.ON_DESTROY -> performDestroy()
                    else -> {}
                }
            }
        })
    }

    internal fun attach(
        controllerManager: ViewControllerManager,
        context: Context,
        activity: Activity,
        container: ViewGroup?
    ) {
        if (this::controllerManager.isInitialized) {
            throw IllegalStateException("ViewController{${javaClass.simpleName}} already attached, can not attach again")
        }

        this.controllerManager = controllerManager
        this.context = context
        this.activity = activity
        this.container = container
        childControllerManager = ViewControllerManagerImpl(this, activity, ViewHost.forViewProvider { contentView })
    }

    internal fun setCurrentState(state: Lifecycle.State) {
        while (true) {
            val current = lifecycleRegistry.currentState
            if (current == state) {
                break
            }

            val forward = current < state
            val target = if (forward) {
                // forward lifecycle
                when (current) {
                    Lifecycle.State.INITIALIZED -> Lifecycle.State.CREATED
                    Lifecycle.State.CREATED -> Lifecycle.State.STARTED
                    Lifecycle.State.STARTED -> Lifecycle.State.RESUMED
                    else -> throw IllegalStateException("Invalid lifecycle $current -> $state")
                }
            } else {
                // backward lifecycle
                when (current) {
                    Lifecycle.State.CREATED -> Lifecycle.State.DESTROYED
                    Lifecycle.State.STARTED -> Lifecycle.State.CREATED
                    Lifecycle.State.RESUMED -> Lifecycle.State.STARTED
                    else -> throw IllegalStateException("Invalid lifecycle $current -> $state")
                }
            }
            lifecycleRegistry.currentState = target
        }
    }

    protected open fun reuseView(): Boolean {
        return false
    }

    /**
     * 设置ViewController的contentView，不操作ViewTree的ViewController可以不调用
     * 对于不操作ViewTree的ViewController，其子ViewController不能调用setContentView
     * @param layoutId 布局layout文件
     * @throws IllegalStateException
     *         如果VC自己有调用setContentView，那么意味着父VC在add自己的时候，必须指定containerId，或者container
     *         否则视图无法attach
     */
    protected open fun setContentView(@LayoutRes layoutId: Int) {
        if (container == null) {
            throw IllegalStateException("ViewController{${javaClass.simpleName}} does not attach to a container, can not call setContentView")
        }

        setContentView(
            LayoutInflater.from(context).inflate(
                layoutId,
                container,
                false)
        )
    }

    /**
     * 设置内容view，需要有父布局
     * @param id view的id，使用
     */
    protected fun setContentView(@IdRes id: Int, view: () -> View) {
        if (container == null) {
           throw IllegalStateException("ViewController{${javaClass.simpleName}} does not attach to a container, can not call setContentView")
        }
        val v = container?.findViewById<View>(id)
        if (v == null) {
            container?.let {
                contentView = view()
                it.addView(contentView)
            }
        } else {
            contentView = v
        }
        visibilityData = CheckerData(contentView!!, contentView!!.visibility)

    }

    protected fun setContentView(@IdRes id: Int, @LayoutRes layout: Int) {
        if (container == null) {
            throw IllegalStateException("ViewController{${javaClass.simpleName}} does not attach to a container, can not call setContentView")
        }
        setContentView(id) {
            LayoutInflater.from(context).inflate(
                layout,
                container,
                false)
        }
    }

    /**
     * @throws IllegalStateException
     *       如果VC自己有调用setContentView，那么意味着父VC在add自己的时候，必须指定containerId，或者container
     *       否则视图无法attach
     */
    protected open fun setContentView(view: View) {
        if (container == null) {
            throw IllegalStateException("ViewController{${javaClass.simpleName}} does not attach to a container, can not call setContentView")
        }

        container?.let {
            contentView = view
            it.addView(contentView)
        }
    }

    /**
     * 获取ContentView，一定要明确通过setContentView设置了视图
     */
    protected fun requireContentView(): View {
        return contentView!!
    }

    /**
     * ViewHost implements
     */
    @Suppress("UNCHECKED_CAST")
    protected fun <T : View?> findViewById(resId: Int): T {
        return contentView?.findViewById<View>(resId) as T
    }

    /**
     * 添加子ViewController
     * @param containerId 如果此ViewController不需要向ViewTree中添加View，layoutId可传0
     *                 或者使用上面的简化版本
     */
    protected fun addViewController(@IdRes containerId: Int, viewController: ViewController) {
        childControllerManager.addViewController(containerId, viewController)
    }

    /**
     * 添加子ViewController
     * @param container viewController容器
     */
    protected fun addViewController(container: ViewGroup, viewController: ViewController) {
        childControllerManager.addViewController(container, viewController)
    }

    /**
     * 添加子ViewController，此ViewController不会操作ViewTree
     */
    protected fun addViewController(viewController: ViewController) {
        addViewController(0, viewController)
    }

    /**
     * 移除子ViewController
     */
    protected fun removeViewController(viewController: ViewController) {
        childControllerManager.removeViewController(viewController)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun performStop() {
        onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            contentView?.cancelPendingInputEvents()
        }
    }

    private fun performDestroy() {
        // 1. 清空ViewModelStore，数据一定要清掉
        viewModelStore.clear()
        // 2. 回调onDestroy
        onDestroy()

        if (reuseView()) {
            Log.d("ViewController", "perform destroy reuse view: ")
            contentView?.let {
                if (it.id == View.NO_ID) {
                    throw IllegalStateException("ViewController ${javaClass.simpleName} reused view's id can't be empty")
                }
            }

            visibilityData?.let {
                if (!VISIBLE_CHECKER.valid(it)) {
                    Toast.makeText(context, "可见性未清理", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            // 3. 清空ContentView
            contentView?.let {
                container?.removeView(it)
            }
        }
    }

    @CallSuper
    protected open fun onCreate() { }

    @CallSuper
    protected open fun onResume() { }

    @CallSuper
    protected open fun onPause() { }

    @CallSuper
    protected open fun onStart() { }

    @CallSuper
    protected open fun onStop() { }

    @CallSuper
    protected open fun onDestroy() { }

    private fun reuseViewCheck() {

    }
}
