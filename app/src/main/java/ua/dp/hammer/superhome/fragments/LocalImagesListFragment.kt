package ua.dp.hammer.superhome.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.adapters.LocalImagesListAdapter
import ua.dp.hammer.superhome.data.ImageItem
import ua.dp.hammer.superhome.databinding.FragmentLocalImagesListBinding
import ua.dp.hammer.superhome.models.LocalImagesViewModel
import ua.dp.hammer.superhome.utilities.isExternalStorageReadable

class LocalImagesListFragment : Fragment() {
    val viewModel by activityViewModels<LocalImagesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentLocalImagesListBinding.inflate(inflater, container, false)
        val validContext = context ?: throw IllegalStateException("Can't be null")

        val images = mutableListOf<ImageItem>()

        if (isExternalStorageReadable()) {
            val externalFilesDir = validContext.getExternalFilesDir(null)
            if (externalFilesDir != null) {
                for (file in externalFilesDir.listFiles().orEmpty()) {
                    val createdDrawable = Drawable.createFromPath(file.path)

                    if (createdDrawable != null) {
                        images.add(ImageItem(file.name, -1, createdDrawable))
                    }
                }
            }
        }

        val drawableFields = R.drawable::class.java.fields
        for (drawableField in drawableFields) {
            val id = drawableField.getInt(null)
            images.add(ImageItem(id.toString(), id, null))
        }
        viewModel.images.value = images

        context ?: return binding.root

        val adapter = LocalImagesListAdapter(this)
        viewModel.images.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.localImagesList.adapter = adapter

        return binding.root
    }
}