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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.butterknife.talenthouse.LoginInfo.logout
import kr.butterknife.talenthouse.Util.postSetting
import kr.butterknife.talenthouse.network.ButterKnifeApi
import kr.butterknife.talenthouse.network.response.MyPageRes
import kr.butterknife.talenthouse.network.response.PostRes

class MyPageFragment(var userId: String = "") : Fragment() {

    private lateinit var rvAdapter : MainRVAdapter
    private lateinit var posts : ArrayList<PostItem>
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var loginInfo : Array<String>
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

        loginInfo = LoginInfo.getLoginInfo(requireContext())
        if(userId == "") {
            userId = loginInfo[0]
            setNickname(loginInfo[1])
        }
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
        rvAdapter.setOnSettingListener { v: View, postId: String ->
            postSetting(requireActivity(), requireContext(), v, postId, posts,
                { item: PostItem ->
                (activity as MainActivity?)!!.replaceFragment(WriteFragment(), "Write", item)
                true
                },
                { idx: Int ->
                    posts.removeAt(idx as Int)
                    rvAdapter.notifyItemRemoved(idx)
                    true
                })
        }
        mypage_rv.adapter = rvAdapter

        mypage_btn_menu.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.my_page, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.my_page_setting -> {
                        val intent = Intent(requireContext(), SettingActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.my_page_logout -> {
                        logout(requireContext())
                        startActivity(Intent(requireContext(), SplashActivity::class.java))
                        (requireActivity() as MainActivity).finish()
                    }
                }
                true
            }
            popup.show()
        }
        if(userId != loginInfo[0]) {
            coroutineScope.launch {
                getUserInfo()
                setVisibility(userId == loginInfo[0])
                Log.d(MyPageFragment::class.java.simpleName, loginInfo[0] + "\n" + userId);
                rvAdapter.doItemReload()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(userId == loginInfo[0]) {
            coroutineScope.launch {
                getUserInfo()
                setVisibility(userId == loginInfo[0])
                Log.d(MyPageFragment::class.java.simpleName, loginInfo[0] + "\n" + userId)
                posts.clear()
                rvAdapter.setPage(0)
                rvAdapter.doItemReload()
            }
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
                LoadingDialog.onLoadingDialog(activity)
                userInfoRes = ButterKnifeApi.retrofitService.getUserNickname(userId)
                userInfoRes?.let {
                    if(it.data == null) {
                        Toast.makeText(context, it.detail, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        setNickname(it.data.nickname)
                        if(it.data.profile != "")
                            Glide.with(requireContext())
                                .load(it.data.profile)
                                .into(mypage_image_profile)
                    }
                }
                LoadingDialog.offLoadingDialog()
            }
            catch (e: Exception) {
                e.printStackTrace()
                LoadingDialog.offLoadingDialog()
            }
        }
    }

    private fun getUserPosts() {
        coroutineScope.launch {
            try {
                LoadingDialog.onLoadingDialog(activity)
                postsRes = ButterKnifeApi.retrofitService.getMyPagePosts(userId, rvAdapter.pageNum)
                postsRes?.data?.let {
                    for(p in it) {
                        when {
                            p.videoUrl != null -> posts.add(PostItem(p._id, p.title, p.writerNickname, p.writerId, p.updateTime, p.description, p.videoUrl!!, p.likeCnt, p.likeIDs, p.category, p.comments, p.profile))
                            p.imageUrl?.isNotEmpty()!! -> posts.add(PostItem(p._id, p.title, p.writerNickname, p.writerId, p.updateTime, p.description, p.imageUrl!!, p.likeCnt, p.likeIDs, p.category, p.comments, p.profile))
                            else -> posts.add(PostItem(p._id, p.title, p.writerNickname, p.writerId, p.updateTime, p.description, p.likeCnt, p.likeIDs, p.category, p.comments, p.profile))
                        }
                    }

                    rvAdapter.notifyDataSetChanged()
                }
                //평소에는 게시물이 없습니다. 이런거 표시해주기
                LoadingDialog.offLoadingDialog()
            }
            catch (e: Exception) {
                LoadingDialog.offLoadingDialog()
            }
        }
    }
}
