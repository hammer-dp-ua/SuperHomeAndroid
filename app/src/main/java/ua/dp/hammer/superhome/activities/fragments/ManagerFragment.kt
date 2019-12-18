package ua.dp.hammer.superhome.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dp.hammer.superhome.databinding.FragmentManagerBinding
import ua.dp.hammer.superhome.models.ManagerViewModel

class ManagerFragment : Fragment() {
    private val viewModel: ManagerViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                ManagerViewModel() as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: created")
    }

    override fun onStart() {
        super.onStart()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: started")
    }

    override fun onResume() {
        super.onResume()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: resumed")
    }

    override fun onPause() {
        super.onPause()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: paused")
    }

    override fun onStop() {
        super.onStop()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: stopped")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentManagerBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.projectorsButton.setOnLongClickListener() {
            viewModel.onProjectorsLongButtonClick(it as ImageButton)
            true
        }

        binding.cameraRecordingButton.setOnLongClickListener() {
            viewModel.onCameraRecordingLongButtonClick(it as ImageButton)
            true
        }

        return binding.root
    }
}