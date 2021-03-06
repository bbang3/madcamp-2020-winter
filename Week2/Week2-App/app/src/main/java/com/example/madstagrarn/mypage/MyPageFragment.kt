package com.example.madstagrarn.mypage

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madstagrarn.MainActivity
import com.example.madstagrarn.dataclass.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import java.io.File

class MyPageFragment: Fragment() {
    private val dataService: DataService = DataService()
    private lateinit var currentUser: User

    private var postList: ArrayList<Post> = ArrayList()
    private lateinit var adapter: PostAdapter

    private var tempFile: File? = null
    private val PICK_FROM_ALBUM = 1
    private lateinit var profileImageView: ImageView
    private lateinit var profileImageViewPost: ImageView
    private var isPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getSerializable("User") as User
        adapter = PostAdapter(postList, currentUser, dataService)
        loadUserPosts()
        tedPermission()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.mypage_fragment, container, false)

        view.findViewById<TextView>(R.id.mypage_name).text = currentUser.name

        val logoutButton = view.findViewById<ImageView>(R.id.logout_button)
        logoutButton.setOnClickListener {
            val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.logout_popup, null)
            val acceptButton = view.findViewById<Button>(R.id.accept_button)
            val cancelButton = view.findViewById<Button>(R.id.cancel_button)

            val logoutPopup = AlertDialog.Builder(view.context)
                .create()

            logoutPopup?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            acceptButton.setOnClickListener {
                logoutPopup.dismiss()
                com.facebook.login.LoginManager.getInstance().logOut();
                startActivity(Intent(this.requireActivity(), MainActivity::class.java))
                this.requireActivity().finish()
            }

            cancelButton.setOnClickListener {
                logoutPopup.dismiss()
            }

            logoutPopup.setView(view)
            logoutPopup.show()
        }

        profileImageView = view.findViewById(R.id.mypage_profile_image)
        profileImageViewPost = view.findViewById(R.id.mypage_post_profile_image)
        profileImageView.setOnClickListener { modifyProfileImage() }


        if(currentUser.profileImage.isNullOrEmpty() || currentUser.profileImage == "default_user_profile.png") {
            Glide.with(view)
                .load(R.drawable.profile)
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(profileImageView)
            Glide.with(view)
                .load(R.drawable.profile)
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(profileImageViewPost)
        } else {
            Glide.with(view)
                .load(dataService.BASE_URL + "image/${currentUser.profileImage}")
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(profileImageView)

            Glide.with(view)
                .load(dataService.BASE_URL + "image/${currentUser.profileImage}")
                .circleCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(profileImageViewPost)
        }

        val addPostTextView: TextView = view.findViewById(R.id.add_post_text)
        addPostTextView.setOnClickListener {
            val intent = Intent(view.context, PostAddActivity::class.java)
            intent.putExtra("User", currentUser)
            startActivityForResult(intent, 0)
        }

        val rvPost = view.findViewById<RecyclerView>(R.id.rv_mypost)
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        rvPost.adapter = adapter

        return view
    }

    private fun modifyProfileImage() {
//        tedPermission()
        goToAlbum()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                val postId = data?.getStringExtra("postId")
                if (postId != null) {
                    Log.i("loadNewPost", postId)
                    loadNewPost(postId)
                }
            }
        } else if (requestCode == PICK_FROM_ALBUM) {
            val photoUri: Uri? = data?.data
            var cursor: Cursor? =null;
            try {
                /*
                 *  Uri ????????????
                 *  content:/// ?????? file:/// ???  ????????????.
                 */
                if(photoUri != null) {
                    cursor = context!!.contentResolver.query(photoUri, arrayOf(MediaStore.Images.Media.DATA), null, null, null);
                }

                val columnIndex:Int? = cursor?.getColumnIndexOrThrow (MediaStore.Images.Media.DATA);
                cursor?.moveToFirst();

                if(columnIndex != null) {
                    tempFile = File(cursor?.getString(columnIndex))
                    Glide.with(profileImageView)
                        .load(dataService.BASE_URL + "image/${currentUser.profileImage}")
                        .thumbnail()
                        .circleCrop()
                        .into(profileImageView)

                    val profileImageFile: File = tempFile as File
                    val reqFile = profileImageFile.asRequestBody("image/${profileImageFile.extension}".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", profileImageFile.name, reqFile)
                    dataService.service.updateUser(currentUser.userId, imagePart).enqueue(object: Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if(response.isSuccessful) {
                                Log.i("updateUser", response.body()!!.toString())
                                currentUser = response.body()!!
                                adapter.currentUser = currentUser
                                adapter.notifyDataSetChanged()

                                Glide.with(profileImageView)
                                    .load(dataService.BASE_URL + "image/${currentUser.profileImage}")
                                    .thumbnail()
                                    .circleCrop()
                                    .into(profileImageView)

                                Glide.with(profileImageViewPost)
                                    .load(dataService.BASE_URL + "image/${currentUser.profileImage}")
                                    .thumbnail()
                                    .circleCrop()
                                    .into(profileImageViewPost)
                            }
                        }
                        override fun onFailure(call: Call<User>, t: Throwable) { t.printStackTrace() }
                    })
                }
            } finally {
                cursor?.close()
            }
        }
    }

    private fun loadUserPosts() {
        dataService.service.getUserPosts(currentUser.userId).enqueue(object: Callback<ArrayList<Post>>{
            override fun onResponse(
                call: Call<ArrayList<Post>>,
                response: Response<ArrayList<Post>>
            ) {
                if(response.isSuccessful) {
                    postList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()

                    Log.i("loadUserPosts", "${postList.size}")
                } else {
                    Toast.makeText(context!!, "Failed to load posts", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private fun loadNewPost(postId: String) {
        dataService.service.getPost(postId).enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.i("loadNewPost", response.body()!!.toString())
                if(response.isSuccessful) {
                    postList.add(0, response.body()!!)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private fun goToAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }


    private fun tedPermission() : Boolean {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                isPermissionGranted = true
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String?>?) {
                // ?????? ?????? ??????
                isPermissionGranted = false
            }
        }

        TedPermission.with(context!!)
            .setPermissionListener(permissionListener)
            .setRationaleMessage(resources.getString(R.string.permission_2))
            .setDeniedMessage(resources.getString(R.string.permission_1))
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .check()

        return true
    }
}