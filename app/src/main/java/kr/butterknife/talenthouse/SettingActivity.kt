package kr.butterknife.talenthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingActivity : AppCompatActivity() {
    private lateinit var items : MutableList<SettingItem>
    private lateinit var rvAdapter : SettingRVAdapter
    private var flag = -1
    private val INTENT_KEY = "SettingKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        flag = intent.getIntExtra(INTENT_KEY, -1)

        items = mutableListOf()
        rvAdapter = SettingRVAdapter()
        rvAdapter.items = items

    }
}