package kr.butterknife.talenthouse

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SettingRVAdapter(private val context : Context, val items : MutableList<SettingItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SettingItemType.TITLE.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rvsetting_title, parent, false)
                val vh = SettingTitleVH(view)
                vh
            }
            SettingItemType.SPINNER.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rvsetting_spinner, parent, false)
                val vh = SettingSpinnerVH(context, view)
                vh
            }
            SettingItemType.TEXT.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rvsetting_text, parent, false)
                val vh = SettingTextVH(view)
                vh
            }
            SettingItemType.SWITCH.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rvsetting_switch, parent, false)
                val vh = SettingSwitchVH(context, view)
                vh
            }
//            현재는 else부분이지만, 다른 항목이 추가된다면 아래 주석을 풀고 추가되는 것을 주석으로.
//            SettingItemType.BUTTON.ordinal -> {
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rvsetting_button, parent, false)
                val vh = SettingButtonVH(view)
                vh
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SettingTitleVH -> {
                holder.bind(items[position])
            }
            is SettingSpinnerVH -> {
                holder.bind(items[position])
            }
            is SettingTextVH -> {
                holder.bind(items[position])
            }
            is SettingSwitchVH -> {
                holder.bind(items[position])
            }
            is SettingButtonVH -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemViewType(position: Int) =
        when(items[position].type) {
            "title" -> SettingItemType.TITLE.ordinal
            "spinner" -> SettingItemType.SPINNER.ordinal
            "text" -> SettingItemType.TEXT.ordinal
            "switch" -> SettingItemType.SWITCH.ordinal
            "button" -> SettingItemType.BUTTON.ordinal
            else -> -1
        }

    override fun getItemCount() = items.size

    fun addItem(item : SettingItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun clearItem() {
        items.clear()
        notifyDataSetChanged()
    }

}

data class SettingItem(val type: String, val name: String, var strValue: String = "", val listValue: List<String> = listOf(), val onClick: OnItemClickListener? = null)

enum class SettingItemType(num : Int) {
    TITLE(0), SPINNER(1), TEXT(2), SWITCH(3), BUTTON(4)
}