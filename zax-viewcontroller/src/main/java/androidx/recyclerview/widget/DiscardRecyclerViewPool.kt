package androidx.recyclerview.widget

class DiscardRecyclerViewPool (
  recyclerView: RecyclerView,
  var discardListener: ViewHolderDiscardListener
) : RecyclerView.RecycledViewPool() {


  interface ViewHolderDiscardListener {
    fun onViewHolderDiscard(viewHolder: RecyclerView.ViewHolder)
  }

  init {
    when (val origPool = recyclerView.mRecycler.mRecyclerPool) {
      null -> {
        recyclerView.setRecycledViewPool(this)
      }
      !is DiscardRecyclerViewPool -> {
        // 外部设置了自定义的RecycledViewPool或者配置了缓存池大小
        // 此处设置缓存池大小保持一致
        for (i in 0 until mScrap.size()) {
          val viewType = mScrap.keyAt(i)
          val scrapHeap = mScrap.valueAt(i)
          setMaxRecycledViews(viewType, scrapHeap.mMaxScrap)
        }
        recyclerView.setRecycledViewPool(this)
      }
      else -> {
        // 进入到此分支唯一的原因就是更换了adapter，此时只需要替换一下discardListener即可
        origPool.discardListener = discardListener
      }
    }
  }

  override fun putRecycledView(scrap: RecyclerView.ViewHolder) {
    super.putRecycledView(scrap)
    val viewType = scrap.itemViewType
    val scrapHeap = getScrapDataForType(viewType).mScrapHeap
    // 缓存池中不存在当前的viewHolder说明回收失败
    // 通知外部释放ViewHolder资源
    if (!scrapHeap.contains(scrap)) {
      discardListener.onViewHolderDiscard(scrap)
    }
  }

  private fun getScrapDataForType(viewType: Int): ScrapData {
    var scrapData = mScrap[viewType]
    if (scrapData == null) {
      scrapData = ScrapData()
      mScrap.put(viewType, scrapData)
    }
    return scrapData
  }
}