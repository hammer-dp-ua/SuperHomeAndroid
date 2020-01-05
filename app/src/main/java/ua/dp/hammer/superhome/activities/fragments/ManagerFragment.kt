package ua.dp.hammer.superhome.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dp.hammer.superhome.databinding.FragmentManagerBinding
import ua.dp.hammer.superhome.models.ManagerViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class ManagerFragment : Fragment() {
    private val viewModel: ManagerViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                ManagerViewModel(LocalSettingsRepository.getInstance(currentContext)) as T
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
            val dialog = CameraRecordingSettingsDialog()

            dialog.show(this.parentFragmentManager, "camera_settings")

            true
        }

        binding.fanButton.setOnLongClickListener {
            viewModel.onFanLongButtonClick(it as ImageButton)
            true
        }

        viewModel.projectorsButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.projectorsButton.isSelected = isSelected
        })

        viewModel.cameraButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.cameraRecordingButton.isSelected = isSelected
        })

        viewModel.fanButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.fanButton.isSelected = isSelected
        })

        // For button disabling while fun is already turned on
        viewModel.fanButtonEnabled.observe(viewLifecycleOwner, Observer<Boolean> { isEnabled ->
            binding.fanButton.isEnabled = isEnabled
        })

        viewModel.roomShutterButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.roomShutterButton.isSelected = isSelected
        })

        viewModel.kitchen1ShutterButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.kitchenShutter1Button.isSelected = isSelected
        })

        viewModel.kitchen2ShutterButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.kitchenShutter2Button.isSelected = isSelected
        })

        return binding.root
    }
}