package com.example.viewpager2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class GalleryFragment : Fragment() {

    private lateinit var imageList: ArrayList<Image>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageList = arrayListOf(
            Image(R.drawable.img1),
            Image(R.drawable.img2),
            Image(R.drawable.img3),
            Image(R.drawable.img4),
            Image(R.drawable.img5),
            Image(R.drawable.img6),
            Image(R.drawable.img7),
            Image(R.drawable.img8),
            Image(R.drawable.img9),
            Image(R.drawable.img10),
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        var gallery_rv = view.findViewById<RecyclerView>(R.id.gallery)
        gallery_rv.adapter = ImageAdapter(imageList)
        gallery_rv.layoutManager = GridLayoutManager(activity, 2)
        gallery_rv.setHasFixedSize(true)

        return view
    }

}