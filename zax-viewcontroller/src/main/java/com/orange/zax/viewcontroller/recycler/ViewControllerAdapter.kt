package com.orange.zax.viewcontroller.recycler

import ViewController
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiscardRecyclerViewPool
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.orange.zax.viewcontroller.ViewControllerManager

/**
 * Time: 2023/12/8
 * Author: chengzhi@kuaishou.com
 * 用于列表使用vc封装的adapter
 */
abstract class ViewControllerAdapter<T>(
  protected val parentLifecycleOwner: LifecycleOwner,
  protected val activity: Activity
) : RecyclerView.Adapter<ViewControllerAdapter.ViewHolder<T>>(),
  LifecycleObserver,DiscardRecyclerViewPool.ViewHolderDiscardListener{

  private val holderCache = hashSetOf<ViewHolder<T>>()
  private val handler = Handler(Looper.getMainLooper())

  var data = listOf<T>()
    private set

  init {
    parentLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_DESTROY) {
        destroy()
      }
    })
  }

  @SuppressLint("NotifyDataSetChanged")
  @MainThread
  fun update(list: List<T>) {
    data = list.toList()
    this.notifyDataSetChanged()
  }

  @JvmOverloads
  fun diffUpdateList(
    list: List<T>,
    diffCallback: DiffUtil.Callback,
    updateCallback: ListUpdateCallback = AdapterListUpdateCallback(this),
    detectMoves: Boolean = true
  ) {
    val diffResult = DiffUtil.calculateDiff(diffCallback, detectMoves)
    val dispatchUpdates = {
      data = list.toList()
      diffResult.dispatchUpdatesTo(updateCallback)
    }

    if (Looper.myLooper() == Looper.getMainLooper()) {
      // 主线程直接执行
      dispatchUpdates()
    }
    else {
      handler.post(dispatchUpdates)
    }
  }

  open fun getItem(position: Int): T {
    return data[position]
  }

  override fun getItemCount(): Int {
    return data.size
  }

  override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
    holder.bind(getItem(position))
  }


  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    val layoutManager = recyclerView.layoutManager
    if (layoutManager is LinearLayoutManager) {
      layoutManager.recycleChildrenOnDetach = true
    }
    // 替换RecycledViewPool，监听因为缓存池满无法回收的情况，释放对应的ViewHolder。
    DiscardRecyclerViewPool(recyclerView, this)
  }

  /**
   *  RecycledViewPool缓存满了，无法成功回收，此时主动destroy ViewHolder
   */
  override fun onViewHolderDiscard(viewHolder: RecyclerView.ViewHolder) {
    if (viewHolder is ViewHolder<*>) {
      viewHolder.destroy()
      holderCache.remove(viewHolder)
    }
  }

  /**
   * 主动释放ViewHolder，三种情况下可能会需要调用
   * 1 外部逻辑，需要移除RecyclerView的时候，
   * 2 parentLifecycleOwner destroy时，fragment、Activity销毁
   * 3 RecyclerView切换adapter时
   */
  fun destroy() {
    parentLifecycleOwner.lifecycle.removeObserver(this)

    holderCache.forEach {
      it.destroy()
    }
    holderCache.clear()
  }

  /**
   * RecyclerView切换Adapter，释放ViewHolder
   */
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    destroy()
  }

  /**
   * ViewHolder进入可见状态时回调，此时驱动生命周期进入START状态
   */
  override fun onViewAttachedToWindow(holder: ViewHolder<T>) {
    if (!holderCache.contains(holder)) {
      // 缓存
      holderCache.add(holder)
    }
    holder.start()
  }

  /**
   * ViewHolder进入不可见状态时回调，此时驱动生命周期进入STOP状态
   */
  override fun onViewDetachedFromWindow(holder: ViewHolder<T>) {
    holder.stop()
  }

  /**
   * 内置ViewControllerManager，支持ViewHolder场景使用ViewController
   * ViewHolder的生命周期依赖Adapter的回调，还有parentLifecycleOwner, 取两个状态的较小值
   */
  @Suppress("LeakingThis")
  abstract class ViewHolder<T>(
    itemView: View,
    parentLifecycleOwner: LifecycleOwner,
    activity: Activity
  ) : RecyclerView.ViewHolder(itemView), ViewControllerManager {
    private val manager: ViewControllerManagerImpl
    private val _dataProvider = MutableLiveData<T>()

    /**
     * ViewHolder的item数据，通过LiveData的形式提供给ViewModel
     */
    protected val dataProvider: LiveData<T> = _dataProvider

    /**
     * ViewHolder自己的生命周期
     */
    private val innerLifecycleOwner = SimpleLifecycleOwner()

    init {
      // ViewHolder 与 parentLifecycleOwner合并后的lifecycle来驱动ViewControllerManager
      val mergedLifecycleOwner = merge(innerLifecycleOwner, parentLifecycleOwner)
      innerLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
      manager = ViewControllerManagerImpl(
        hostLifecycleOwner = mergedLifecycleOwner,
        activity = activity,
        context = itemView.context,
        viewHost = ViewHost.forView(itemView)
      )
    }

    /**
     * 支持在ViewHolder中使用ViewController
     */
    override fun addViewController(@IdRes containerId: Int, viewController: ViewController) {
      manager.addViewController(containerId, viewController)
    }

    override fun addViewController(container: ViewGroup, viewController: ViewController) {
      manager.addViewController(container, viewController)
    }

    override fun removeViewController(viewController: ViewController) {
      manager.removeViewController(viewController)
    }

    /**
     * ViewHolder即将可见时调用，对应onViewAttachedToWindow
     */
    fun start() {
      innerLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    /**
     * ViewHolder即将不可见时调用，对应onViewDetachedFromWindow
     */
    fun stop() {
      innerLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    /**
     * 当ViewHolder无法被回收时调用，
     * see {@link NotifyDiscardRecycledViewPool}
     */
    fun destroy() {
      innerLifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun bind(data: T) {
      _dataProvider.value = data
    }
  }
}


class SimpleLifecycleOwner: LifecycleOwner {
  private val lifecycleRegistry = LifecycleRegistry(this)

  override fun getLifecycle(): LifecycleRegistry {
    return lifecycleRegistry
  }
}