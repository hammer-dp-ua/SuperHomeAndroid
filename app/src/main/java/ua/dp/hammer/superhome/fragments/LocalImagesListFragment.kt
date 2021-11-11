package ua.dp.hammer.superhome.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.adapters.LocalImagesListAdapter
import ua.dp.hammer.superhome.data.ImageItem
import ua.dp.hammer.superhome.databinding.FragmentLocalImagesListBinding
import ua.dp.hammer.superhome.models.AlarmsDisplayedOrderViewModel
import ua.dp.hammer.superhome.models.LocalImagesViewModel
import ua.dp.hammer.superhome.utilities.isExternalStorageReadable

class LocalImagesListFragment : Fragment() {
    val viewModel by activityViewModels<LocalImagesViewModel>()
    val alarmsDisplayedOrderViewModel by activityViewModels<AlarmsDisplayedOrderViewModel>()

    val navArgs: LocalImagesListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentLocalImagesListBinding.inflate(inflater, container, false)

        val images = mutableListOf<ImageItem>()

        addImagesFromResources(images)
        viewModel.images.value = images

        val adapter = LocalImagesListAdapter(this)
        viewModel.images.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.localImagesList.adapter = adapter

        return binding.root
    }

    private fun addImagesFromResources(images: MutableList<ImageItem>) {
        val drawableFields = R.drawable::class.java.fields

        for (drawableField in drawableFields) {
            val id = drawableField.getInt(null)
            val resourceName = this.context?.resources?.getResourceEntryName(id)

            if (resourceName != null) {
                images.add(ImageItem(resourceName, null))
            }
        }
    }

    //binding.displayedIconImageButton.setImageDrawable(Drawable.createFromPath("/storage/emulated/0/Android/data/ua.dp.hammer.superhome/files/clion.ico"))
    //binding.displayedIconImageButton.setImageResource(R.drawable.street_yard_md)
    private fun addImagesFromFileSystem(images: MutableList<ImageItem>) {
        if (isExternalStorageReadable()) {
            val path = Environment.getExternalStoragePublicDirectory("superhome")

            if (path.exists() && path.isDirectory) {
                for (file in path.listFiles().orEmpty()) {
                    val createdDrawable = Drawable.createFromPath(file.path)

                    if (createdDrawable != null) {
                        images.add(ImageItem(null, file))
                    }
                }
            }
        }
    }
}