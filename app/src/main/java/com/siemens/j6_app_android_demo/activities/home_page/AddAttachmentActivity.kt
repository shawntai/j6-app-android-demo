package com.siemens.j6_app_android_demo.activities.home_page

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Attachment
import com.siemens.j6_app_android_demo.adapters.AttachmentAdapter


class AddAttachmentActivity : AppCompatActivity() {
    // One Button
    var BSelectImage: Button? = null

    // One Preview Image
    var IVPreviewImage: ImageView? = null

    // constant to compare
    // the activity result code
    var SELECT_PICTURE = 200

    private val CAMERA_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100

    private val attachmentList = ArrayList<Attachment>()
    lateinit var daRecyclerView: RecyclerView
    val dAdapter = AttachmentAdapter(attachmentList)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_attachment)

        daRecyclerView = findViewById(R.id.attachment_recyclerview)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        daRecyclerView.layoutManager = layoutManager
        daRecyclerView.itemAnimator = DefaultItemAnimator()
        daRecyclerView.adapter = dAdapter

        findViewById<ImageView>(R.id.add_new_attachment).setOnClickListener {
            findViewById<LinearLayout>(R.id.attachment_type_selection).visibility = View.VISIBLE
        }
        findViewById<TextView>(R.id.select_photo).setOnClickListener {
            imageChooser()
            findViewById<LinearLayout>(R.id.attachment_type_selection).visibility = View.INVISIBLE
        }
        findViewById<TextView>(R.id.camera).setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
            findViewById<LinearLayout>(R.id.attachment_type_selection).visibility = View.INVISIBLE
        }
        findViewById<TextView>(R.id.cancel).setOnClickListener {
            findViewById<LinearLayout>(R.id.attachment_type_selection).visibility = View.INVISIBLE
        }

        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("attach_list", Gson().toJson(attachmentList))
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        findViewById<ImageView>(R.id.back).setOnClickListener {
            val resultIntent = Intent()
//            resultIntent.putExtra("attach_list", attachmentList)
            val temp: ArrayList<Attachment>? = null
            resultIntent.putExtra("attach_list", temp)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    // this function is triggered when
    // the Select Image Button is clicked
    private fun imageChooser() {

        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // CAMERA
            val photoTaken = data!!.extras!!["data"] as Bitmap
            attachmentList.add(
                Attachment(
                    photoTaken,
                    Attachment.CAMERA,
                    "Attachment: photo taken",
                    "jpg"
                )
            )
            dAdapter.notifyDataSetChanged()
            //findViewById<ImageView>(R.id.imageView).setImageBitmap(photoTaken)
        } else {
            // PHOTO
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == RESULT_OK) {

                // compare the resultCode with the
                // SELECT_PICTURE constant
                if (requestCode == SELECT_PICTURE) {
                    // Get the url of the image from data
                    val selectedImageUri = data!!.data
                    if (null != selectedImageUri) {
                        // update the preview image in the layout
                        //IVPreviewImage!!.setImageURI(selectedImageUri)
                        attachmentList.add(
                            Attachment(
                                selectedImageUri,
                                Attachment.IMAGE,
                                getFileName(selectedImageUri),
                                MimeTypeMap.getSingleton().getExtensionFromMimeType(
                                    contentResolver.getType(
                                        selectedImageUri
                                    )
                                )
                            )
                        )
                        dAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("Recycle")
    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme.equals("content")) {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }
}