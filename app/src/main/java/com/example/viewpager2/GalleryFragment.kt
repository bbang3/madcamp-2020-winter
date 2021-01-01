package com.example.viewpager2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager2.GalleryViewModel
import com.example.viewpager2.ImageAdapter
import com.example.viewpager2.R

class GalleryFragment : Fragment() {
    private val galleryViewModel : GalleryViewModel by activityViewModels()

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
        gallery_rv.adapter = ImageAdapter(galleryViewModel.getImageList())
        gallery_rv.layoutManager = GridLayoutManager(activity, 2)
        gallery_rv.setHasFixedSize(true)

//        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        return view
    }

}