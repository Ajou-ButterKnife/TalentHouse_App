package kr.butterknife.talenthouse

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*

object SpinnerUtil {
    fun setCategorySpinner(spinner: Spinner, chipGroup: ChipGroup, context: Context) {
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val str = parent.getItemAtPosition(position).toString()
                if (str == "카테고리" == false) {
                    var alreadySelected = false
                    // 이미 선택되었는지 확인
                    for (i in 0 until chipGroup.getChildCount()) {
                        val category = (chipGroup.getChildAt(i) as Chip).text.toString()
                        if (category == str) {
                            alreadySelected = true
                            Toast.makeText(context, "이미 선택된 카테고리입니다.", Toast.LENGTH_SHORT).show()
                            parent.setSelection(0)
                            break
                        }
                    }
                    if (alreadySelected == false) {
                        // Chip 인스턴스 생성
                        val chip = Chip(view.context)
                        chip.text = str
                        // chip icon 표시 여부
                        chip.isCloseIconVisible = true
                        chip.setBackgroundColor(Color.BLUE)
                        // chip group 에 해당 chip 추가
                        chipGroup.addView(chip)
                        parent.setSelection(0)
                        // chip 인스턴스 클릭 리스너
                        chip.setOnCloseIconClickListener { v -> chipGroup.removeView(v) }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}

object Util {
    fun unixTime2String(timemillis : Long) : String {
        val sdf = SimpleDateFormat("yyyy.MM.dd.hh.mm")
        val date = sdf.format(timemillis)
        return date
    }
}