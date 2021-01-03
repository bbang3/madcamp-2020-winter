package com.example.viewpager2

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : Fragment() {
    private val imageList: ArrayList<Image> = initalImageList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        var gallery_rv = view.findViewById<RecyclerView>(R.id.gallery)
        gallery_rv.adapter = ImageAdapter(imageList)
        gallery_rv.layoutManager = GridLayoutManager(activity, 3)
        gallery_rv.setHasFixedSize(true)

        return view
    }
}
