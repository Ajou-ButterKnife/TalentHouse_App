package kr.butterknife.talenthouse

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SettingTitleVH(view : View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.rvsetting_title)

    fun bind(item : SettingItem) {
        title.text = item.name
    }
}

class SettingButtonVH(view : View) : RecyclerView.ViewHolder(view) {
    private var onItemClickListener: OnItemClickListener? = null
    val button = view.findViewById<Button>(R.id.rvsetting_button)

    fun bind(item : SettingItem) {
        button.text = item.name
        onItemClickListener = item.onClick

        onItemClickListener?.let {
            button.setOnClickListener { view ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION)
                    it.onItemClick(view, pos)
            }
        }
    }
}

class SettingTextVH(view : View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.rvsetting_title_text)
    val til = view.findViewById<TextInputLayout>(R.id.rvsetting_til_text)
    val et = view.findViewById<TextInputEditText>(R.id.rvsetting_et_text)

    fun bind(item : SettingItem) {
//        if(item.name == "password")
//            et.inputType = InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD

        title.text = item.name
        et.hint = item.name
        et.setText(item.strValue)
    }
}

class SettingSwitchVH(private val context : Context, view : View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.rvsetting_title_switch)
    val switch = view.findViewById<SwitchCompat>(R.id.rvsetting_switch)

    fun bind(item : SettingItem) {
        title.text = item.name
        switch.text = item.strValue
        switch.isChecked = LoginInfo.getNotiPermission(context)

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            LoginInfo.setNotiPermission(context, isChecked)
        }
    }
}

class SettingSpinnerVH(private val context : Context, private val view : View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.rvsetting_title_spinner)
    val spinner = view.findViewById<Spinner>(R.id.rvsetting_spinner)
    val chipGroup = view.findViewById<ChipGroup>(R.id.rvsetting_chipgroup)

    fun bind(item : SettingItem) {
        title.text = item.name

        val arr = context.resources.getStringArray(R.array.category_spinner)
        val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, arr)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(!findChip(arr[position]) && position != 0) {

                    addChip(arr[position])
                    parent?.setSelection(0)
                }
                else if(position != 0) {
                    Toast.makeText(context, "이미 선택된 카테고리입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        for(category in item.listValue)
            addChip(category)
    }

    fun addChip(str : String) {
        val chip = Chip(view.context)
        chip.text = str
        chip.isCloseIconVisible = true
        chip.setBackgroundColor(Color.BLUE)
        chipGroup.addView(chip)
        chip.setOnCloseIconClickListener { v ->
            chipGroup.removeView(v)
        }
    }

    // 동일한 칩 찾으면 true, 아니면 false
    fun findChip(str : String) : Boolean {
        for(idx in 0 until chipGroup.childCount)
            if((chipGroup.getChildAt(idx) as Chip).text.toString() == str)
                return true
        return false
    }
}

class SettingImageVH(private val context : Context, view : View) : RecyclerView.ViewHolder(view) {
    val profileImage = view.findViewById<ImageView>(R.id.rvsetting_profile)
    private var onItemClickListener: OnItemClickListener? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    fun bind(item : SettingItem) {
        if(item.name == "profile") {
            Glide.with(context)
                .load(
                    if(item.strValue != "") item.strValue
                    else context.resources.getDrawable(R.drawable.no_image, null)
                )
                .into(profileImage)

            onItemClickListener = item.onClick
            onItemClickListener?.let {
                profileImage.setOnClickListener { view ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION)
                        it.onItemClick(view, pos)
                }
            }
        }
    }
}