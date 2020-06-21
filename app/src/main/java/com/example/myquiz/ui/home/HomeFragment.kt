package com.example.myquiz.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Output
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myquiz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream
import androidx.core.content.ContextCompat.checkSelfPermission as checkSelfPermission

class HomeFragment : Fragment() {
    private lateinit var myContext: Context
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var img_pick_btn: Button
    private val storageRef = Firebase.storage.getReference("images")
    private val mAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        img_pick_btn = root.findViewById(R.id.img_pick_btn)
        myContext = root.context

        getImage()
        imageButton()
        return root
    }
    //getting user's image from the firestorage
    private fun getImage(){
        val ref = storageRef.child(mAuth.currentUser?.uid.toString())
        val data: Long = 1024 * 1024;
        ref.getBytes(data).addOnSuccessListener {
            val bm:Bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
            image_view.setImageBitmap(bm)
        }
    }
    //when the user press the button it sends a request for pression to get access to the gallery on the user's phone
    private fun imageButton(){
        img_pick_btn.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(this.myContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }
    //selecting a image form the gallery
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this.myContext, getString(R.string.permission_de), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image_view.setImageURI(data?.data)
            val bm:Bitmap = image_view.drawable.toBitmap()
            val stream = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG,100, stream)
            val bytes = stream.toByteArray()


            val name = mAuth.currentUser?.uid.toString()
            val uploadTask = storageRef.child(name).putBytes(bytes)
            uploadTask.addOnSuccessListener {
            }.addOnFailureListener {
            }
        }
    }
}



