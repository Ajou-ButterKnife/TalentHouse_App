package kr.butterknife.talenthouse

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SettingRVAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<SettingItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = items.size

}

data class SettingItem(val type : String, val name : String, var value : String)