package kr.butterknife.talenthouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.butterknife.talenthouse.network.ButterKnifeApi
import kr.butterknife.talenthouse.network.response.MyPageRes
import kr.butterknife.talenthouse.network.response.PostRes

class MyPageFragment(var userId: String = "") : Fragment() {

    private lateinit var rvAdapter : MainRVAdapter
    private lateinit var posts : ArrayList<PostItem>
    private lateinit var coroutineScope : CoroutineScope
    private var userInfoRes : MyPageRes? = null
    private var postsRes : PostRes? = null
    private val INTENT_KEY = "SettingKey"


    // 들어오는 spf의 id에 따라서 메뉴 보여줄지 말지
    // 마이페이지로도 작동할 수 있고, 딴사람의 이름을 눌러서 들어오면 다른사람의 화면이므로 메뉴같은게 안보인다!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroutineScope = CoroutineScope(Dispatchers.Main + Job())

        val loginInfo = LoginInfo.getLoginInfo(requireContext())
        posts = ArrayList()
        rvAdapter = MainRVAdapter(requireContext(), posts)
        rvAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(v: View?, pos: Int) {
                (requireActivity() as MainActivity).replaceFragment(
                    ContentFragment(posts[pos]),
                    "Content"
                )
            }

        })
        rvAdapter.initScrollListener(mypage_rv)
        rvAdapter.setOnItemReloadListener {
            getUserPosts()
        }
        mypage_rv.adapter = rvAdapter

        mypage_btn_menu.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.my_page, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                val intent = Intent(requireContext(), SettingActivity::class.java)
                when(menuItem.itemId) {
                    R.id.my_page_privacy -> {
                        intent.putExtra(INTENT_KEY, 0)
                        Toast.makeText(requireContext(), "개인 정보", Toast.LENGTH_SHORT).show()
                    }
                    R.id.my_page_setting -> {
                        val intent = Intent(requireContext(), SettingActivity::class.java)
                        intent.putExtra(INTENT_KEY, 1)
                        Toast.makeText(requireContext(), "설정", Toast.LENGTH_SHORT).show()
                    }
                }
                startActivity(intent)
                true
            }
            popup.show()
        }
        coroutineScope.launch {
            if(userId == "") {
                userId = loginInfo[0]
                setNickname(loginInfo[1])
                setVisibility(true)
            }
            else {
                getUserInfo()
                setVisibility(false)
            }
            Log.d(MyPageFragment::class.java.simpleName, loginInfo[0] + "\n" + userId);
            rvAdapter.doItemReload()
        }
    }

    private fun setNickname(name: String) {
        mypage_tv_nickname.text = name + "님"
    }

    private fun setVisibility(flag: Boolean) {
        mypage_btn_menu.visibility = if(flag) View.VISIBLE else View.GONE
    }

    private fun getUserInfo() {
        coroutineScope.launch {
            try {
                userInfoRes = ButterKnifeApi.retrofitService.getUserInfo(userId)
                userInfoRes?.let {
                    if(it.data == null) {
                        Toast.makeText(context, it.detail, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        setNickname(it.data.nickname)
                    }
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getUserPosts() {
        coroutineScope.launch {
            try {
                postsRes = ButterKnifeApi.retrofitService.getMyPagePosts(userId, rvAdapter.pageNum)
                postsRes?.data?.let {
                    for(p in it) {
                        when {
                            p.videoUrl != null -> posts.add(PostItem(p._id, p.title, p.writerNickname, p.writerId, p.updateTime, p.description, p.videoUrl!!, p.likeCnt, p.category, p.comments))
                            p.imageUrl != null -> posts.add(PostItem(p._id, p.title, p.writerNickname, p.writerId, p.updateTime, p.description, p.imageUrl!!, p.likeCnt, p.category, p.comments))
                            else -> posts.add(PostItem(p._id, p.title, p.writerNickname, p.writerId, p.updateTime, p.description, p.likeCnt, p.category, p.comments))
                        }
                    }

                    rvAdapter.notifyDataSetChanged()
                }
                //평소에는 게시물이 없습니다. 이런거 표시해주기
            }
            catch (e: Exception) {

            }
        }
    }
}
