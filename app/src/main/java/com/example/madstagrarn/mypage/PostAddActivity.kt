package com.example.madstagrarn.mypage

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.madstagrarn.dataclass.Post
import com.example.madstagrarn.R
import com.example.madstagrarn.dataclass.User
import com.example.madstagrarn.network.DataService
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class PostAddActivity : AppCompatActivity() {
    private lateinit var currentUser: User
    private var dataService = DataService()

    private var tempFile: File? = null
    private val PICK_FROM_ALBUM = 1
    private var isPermissionGranted = false

    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_add)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.hide()
        currentUser = intent.extras?.get("User") as User
        tedPermission()

        val addButton = findViewById<Button>(R.id.add_post_button)
        val cancelButton = findViewById<ImageButton>(R.id.add_cancel_button)

        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        addButton.setOnClickListener {
            addPost()
            Toast.makeText(this@PostAddActivity, tempFile.toString(), Toast.LENGTH_SHORT).show()
        }

        image = findViewById(com.example.madstagrarn.R.id.addpost_image)
        image.setOnClickListener {
            if(isPermissionGranted) {
                goToAlbum()
            }
        }
    }


    private fun addPost() {
        if(tempFile == null) {
            Toast.makeText(this, "Image must not be null!", Toast.LENGTH_SHORT).show()
            return
        }

        val description: EditText = findViewById(R.id.description_edit)
        val imageFile: File = tempFile as File

//        val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.madstagrarn_logo)
//        val imageFile = convertBitmapToFile("sample.jpg", bitmapImage)
        val reqFile = imageFile.asRequestBody("image/${imageFile.extension}".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, reqFile)
        val userIdPart = currentUser.userId.toRequestBody(MultipartBody.FORM)
        val descriptionPart = description.text.toString().toRequestBody(MultipartBody.FORM)

        dataService.service.uploadPost(userIdPart, imagePart, descriptionPart).enqueue(object: Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(response.isSuccessful) {
                    val post = response.body()!!
                    val intent = Intent()
                    intent.putExtra("postId", post._id)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FROM_ALBUM) {

            val photoUri:Uri? = data?.data
            var cursor:Cursor? =null;
            try {
                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                if(photoUri != null) {
                    cursor = contentResolver.query(photoUri, arrayOf(MediaStore.Images.Media.DATA), null, null, null);
                }

                val columnIndex:Int? = cursor?.getColumnIndexOrThrow (MediaStore.Images.Media.DATA);
                cursor?.moveToFirst();

                if(columnIndex != null) {
                    tempFile = File(cursor?.getString(columnIndex))

                    if (Build.VERSION.SDK_INT >= 29) {
                        val source = ImageDecoder.createSource(
                            this.contentResolver, Uri.fromFile(tempFile)
                        )
                        image.setImageBitmap(ImageDecoder.decodeBitmap(source))
                    } else {
                        image.setImageBitmap(BitmapFactory.decodeFile(tempFile!!.absolutePath))
                    }
                }
            } finally {
                cursor?.close()
            }
        }
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
                // 권한 요청 실패
                isPermissionGranted = false
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleMessage(resources.getString(R.string.permission_2))
            .setDeniedMessage(resources.getString(R.string.permission_1))
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()

        return true
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}